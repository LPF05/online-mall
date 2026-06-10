package com.yoimiya.onlinemall.sync;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.yoimiya.onlinemall.document.ProductDocument;
import com.yoimiya.onlinemall.entity.Product;
import com.yoimiya.onlinemall.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用启动时，将 MySQL product 表数据全量同步到 Elasticsearch（幂等：ES 已有数据则跳过）
 */
@Component
public class ProductSyncRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductSyncRunner.class);
    private static final String INDEX = "product";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ElasticsearchClient esClient;

    @Override
    public void run(ApplicationArguments args) {
        try {
            // 1. 检查 ES 中 product 索引是否已有数据（幂等保护）
            long esCount = 0;
            try {
                esCount = esClient.count(c -> c.index(INDEX)).count();
            } catch (Exception e) {
                // 索引可能不存在，count 也会失败，视为 0
                esCount = 0;
            }

            if (esCount > 0) {
                log.info("[ES] 已有 {} 条商品数据，跳过全量同步", esCount);
                return;
            }

            // 2. 从 MySQL 全量拉取
            List<Product> products = productMapper.findAll();
            if (products == null || products.isEmpty()) {
                log.info("[ES] MySQL 中无商品数据");
                return;
            }

            // 3. 批量写入 ES（bulk 操作，性能好）
            List<BulkOperation> ops = products.stream()
                    .map(ProductDocument::fromEntity)
                    .map(doc -> BulkOperation.of(b -> b
                            .index(i -> i.index(INDEX)
                                    .id(String.valueOf(doc.getId()))
                                    .document(doc))))
                    .collect(Collectors.toList());

            esClient.bulk(b -> b.operations(ops));
            log.info("[ES] 全量同步完成，共 {} 条商品", ops.size());

        } catch (Exception e) {
            log.error("[ES] 启动同步失败: {}", e.getMessage(), e);
        }
    }
}
