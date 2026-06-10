package com.yoimiya.onlinemall.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.yoimiya.onlinemall.document.ProductDocument;
import com.yoimiya.onlinemall.entity.Product;
import com.yoimiya.onlinemall.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 商品服务类
 */
@Service
public class ProductService {

    private static final String ES_INDEX = "product";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private ProductDocument toDocument(Product p) { return ProductDocument.fromEntity(p); }

    public List<Product> getProductList(String category, String sort, String keyword,
                                         Double minPrice, Double maxPrice) {
        // Convert frontend sort values to SQL ORDER BY clause
        String sortClause;
        if (sort == null || sort.isEmpty() || sort.equals("created_at")) {
            sortClause = "created_at DESC";
        } else if (sort.equals("price_asc")) {
            sortClause = "price ASC";
        } else if (sort.equals("price_desc")) {
            sortClause = "price DESC";
        } else if (sort.equals("sales_count")) {
            sortClause = "sales_count DESC";
        } else if (sort.equals("view_count")) {
            sortClause = "view_count DESC";
        } else {
            sortClause = sort;
        }
        return productMapper.findList(category, sortClause, keyword, minPrice, maxPrice);
    }

    public Product getProductDetail(Long id) {
        Product product = productMapper.findById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        productMapper.incrementViewCount(id);
        product.setViewCount(product.getViewCount() + 1);
        return product;
    }

    public List<Product> searchProducts(String keyword) {
        return productMapper.searchByKeyword(keyword);
    }

    public List<Product> getHotProducts(String sortBy) {
        String sort = (sortBy == null || sortBy.isEmpty()) ? "view_count" : sortBy;
        return productMapper.findList(null, sort, null, null, null);
    }

    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    public Product createProduct(Product product) {
        productMapper.insert(product);
        try {
            ProductDocument doc = toDocument(product);
            elasticsearchClient.index(IndexRequest.of(i -> i
                    .index(ES_INDEX)
                    .id(String.valueOf(doc.getId()))
                    .document(doc)));
        } catch (Exception e) {
            System.err.println("[ES] 创建商品同步失败: " + e.getMessage());
        }
        return product;
    }

    public Product updateProduct(Product product) {
        productMapper.update(product);
        try {
            Product fresh = productMapper.findById(product.getId());
            if (fresh != null) {
                ProductDocument doc = toDocument(fresh);
                elasticsearchClient.index(IndexRequest.of(i -> i
                        .index(ES_INDEX)
                        .id(String.valueOf(doc.getId()))
                        .document(doc)));
            }
        } catch (Exception e) {
            System.err.println("[ES] 更新商品同步失败: " + e.getMessage());
        }
        return product;
    }

    public List<String> getCategories() {
        return productMapper.findCategories();
    }

    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
        try {
            elasticsearchClient.delete(d -> d.index(ES_INDEX).id(String.valueOf(id)));
        } catch (Exception e) {
            System.err.println("[ES] 删除商品同步失败: " + e.getMessage());
        }
    }

    public List<Product> getSaleProducts(int limit) {
        return productMapper.findOnSale(limit);
    }

    public List<Product> getLatestProducts(int limit) {
        return productMapper.findLatest(limit);
    }
}
