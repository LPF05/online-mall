package com.yoimiya.onlinemall.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.yoimiya.onlinemall.document.ProductDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品搜索服务（基于 Elasticsearch）
 */
@Service
public class ProductSearchService {

    private static final String INDEX = "product";

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    /**
     * 商品列表查询（支持关键词、分类、价格区间、排序）
     */
    public List<ProductDocument> searchList(String keyword, String category,
                                             Double minPrice, Double maxPrice,
                                             String sort) {
        try {
            BoolQuery.Builder bool = new BoolQuery.Builder();

            // 关键词：在 name 和 description 中做 should，权重 name > description
            boolean hasKeyword = StringUtils.hasText(keyword);
            if (hasKeyword) {
                final String kw = keyword.trim();
                bool.should(
                    MatchQuery.of(m -> m.field("name").query(kw).boost(3.0f))._toQuery());
                bool.should(
                    MatchQuery.of(m -> m.field("description").query(kw).boost(1.0f))._toQuery());
            }

            // 过滤：分类（category 在 ES 中动态推断为 text 字段，使用 match 查询实现精确匹配）
            if (StringUtils.hasText(category)) {
                bool.filter(
                    MatchQuery.of(m -> m.field("category").query(category))._toQuery());
            }

            // 过滤：价格区间
            if (minPrice != null || maxPrice != null) {
                bool.filter(
                    RangeQuery.of(r -> {
                        r.field("price");
                        if (minPrice != null) r.gte(co.elastic.clients.json.JsonData.of(minPrice));
                        if (maxPrice != null) r.lte(co.elastic.clients.json.JsonData.of(maxPrice));
                        return r;
                    })._toQuery());
            }

            // 排序
            SearchRequest.Builder req = new SearchRequest.Builder()
                    .index(INDEX)
                    .size(500);

            // 若无任何关键词与过滤条件，直接走 match_all + 排序
            boolean hasAnyCondition = hasKeyword
                    || StringUtils.hasText(category)
                    || minPrice != null
                    || maxPrice != null;

            if (hasAnyCondition) {
                req.query(bool.build()._toQuery());
            } else {
                req.query(q -> q.matchAll(m -> m));
            }

            // 排序策略：
            //   - 有关键词 + 用户没显式选 sort（包括"createdAt"这种默认值） → 按相关性分数(_score)倒序
            //   - 有显式 sort（priceAsc/priceDesc/salesCount/viewCount） → 按传入字段排序
            //   - 其他情况 → 按 createdAt 倒序（时间最新优先）
            boolean isExplicitSort = StringUtils.hasText(sort)
                    && !"createdAt".equalsIgnoreCase(sort)
                    && !"created_at".equalsIgnoreCase(sort);

            if (StringUtils.hasText(keyword) && !isExplicitSort) {
                req.sort(s -> s.score(f -> f.order(SortOrder.Desc)));
            } else if (isExplicitSort) {
                applySort(req, sort);
            } else {
                req.sort(s -> s.field(f -> f.field("createdAt").order(SortOrder.Desc)));
            }

            SearchResponse<ProductDocument> resp =
                    elasticsearchClient.search(req.build(), ProductDocument.class);

            return resp.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // ES 查询失败，打印日志但不打断主流程
            System.err.println("[ES] searchList 查询失败: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * 纯关键词搜索（用于 /search 接口
     */
    public List<ProductDocument> searchByKeyword(String keyword) {
        return searchList(keyword, null, null, null, null);
    }

    /**
     * 按销量/浏览量/创建时间取 topN
     */
    public List<ProductDocument> findTopN(String sortField, int limit) {
        try {
            SearchRequest.Builder req = new SearchRequest.Builder()
                    .index(INDEX)
                    .query(q -> q.matchAll(m -> m))
                    .size(limit);

            applySort(req, sortField);
            applySortByField(req, sortField);

            SearchResponse<ProductDocument> resp =
                    elasticsearchClient.search(req.build(), ProductDocument.class);

            return resp.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("[ES] findTopN 查询失败: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * 特卖商品 top N（isOnSale=1
     */
    public List<ProductDocument> findOnSale(int limit) {
        try {
            SearchResponse<ProductDocument> resp = elasticsearchClient.search(s -> s
                    .index(INDEX)
                    .query(q -> q.term(t -> t.field("isOnSale").value(1)))
                    .sort(so -> so.field(f -> f.field("salesCount").order(SortOrder.Desc)))
                    .size(limit), ProductDocument.class);
            return resp.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("[ES] findOnSale 查询失败: " + e.getMessage());
            return List.of();
        }
    }

    // ----------- 辅助方法 -----------

    private void applySort(SearchRequest.Builder req, String sort) {
        if (sort == null || sort.isEmpty()) {
            req.sort(s -> s.field(f -> f.field("createdAt").order(SortOrder.Desc)));
            return;
        }
        switch (sort) {
            case "priceAsc":
            case "price_asc":
                req.sort(s -> s.field(f -> f.field("price").order(SortOrder.Asc)));
                break;
            case "priceDesc":
            case "price_desc":
                req.sort(s -> s.field(f -> f.field("price").order(SortOrder.Desc)));
                break;
            case "salesCount":
            case "sales_count":
                req.sort(s -> s.field(f -> f.field("salesCount").order(SortOrder.Desc)));
                break;
            case "viewCount":
            case "view_count":
                req.sort(s -> s.field(f -> f.field("viewCount").order(SortOrder.Desc)));
                break;
            case "createdAt":
            case "created_at":
                req.sort(s -> s.field(f -> f.field("createdAt").order(SortOrder.Desc)));
                break;
            default:
                // 默认按创建时间倒序
                req.sort(s -> s.field(f -> f.field("createdAt").order(SortOrder.Desc)));
                break;
        }
    }

    private void applySortByField(SearchRequest.Builder req, String sortField) {
        if (sortField == null) return;
        switch (sortField) {
            case "sales_count":
                req.sort(s -> s.field(f -> f.field("salesCount").order(SortOrder.Desc)));
                break;
            case "view_count":
                req.sort(s -> s.field(f -> f.field("viewCount").order(SortOrder.Desc)));
                break;
            case "price":
                req.sort(s -> s.field(f -> f.field("price").order(SortOrder.Asc)));
                break;
            default:
                req.sort(s -> s.field(f -> f.field("createdAt").order(SortOrder.Desc)));
                break;
        }
    }
}
