package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.document.ProductDocument;
import com.yoimiya.onlinemall.entity.Product;
import com.yoimiya.onlinemall.service.ProductSearchService;
import com.yoimiya.onlinemall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSearchService productSearchService;

    @GetMapping
    public Result<List<Product>> list(@RequestParam(required = false) String category,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Double minPrice,
                                    @RequestParam(required = false) Double maxPrice) {
        List<ProductDocument> docs = productSearchService.searchList(keyword, category, minPrice, maxPrice, sort);
        return Result.success(
                docs.stream().map(this::toProduct).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        return Result.success(productService.getProductDetail(id));
    }

    @GetMapping("/search")
    public Result<List<Product>> search(@RequestParam String keyword) {
        return Result.success(
                productSearchService.searchByKeyword(keyword).stream()
                        .map(this::toProduct)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/categories")
    public Result<List<String>> categories() {
        return Result.success(productService.getCategories());
    }

    @GetMapping("/hot")
    public Result<List<Product>> hot(@RequestParam(defaultValue = "view_count") String sortBy) {
        return Result.success(productService.getHotProducts(sortBy));
    }

    @GetMapping("/sale")
    public Result<List<Product>> sale(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(productService.getSaleProducts(limit));
    }

    @GetMapping("/latest")
    public Result<List<Product>> latest(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(productService.getLatestProducts(limit));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<Product>> getAllProducts() {
        return Result.success(productService.getAllProducts());
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Product> createProduct(@RequestBody Product product) {
        return Result.success(productService.createProduct(product));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return Result.success(productService.updateProduct(product));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }

    private Product toProduct(ProductDocument doc) {
        Product p = new Product();
        p.setId(doc.getId());
        p.setName(doc.getName());
        p.setDescription(doc.getDescription());
        p.setCategory(doc.getCategory());
        p.setPrice(doc.getPrice());
        p.setOriginalPrice(doc.getOriginalPrice());
        p.setStock(doc.getStock());
        p.setImageUrl(doc.getImageUrl());
        p.setIsOnSale(doc.getIsOnSale());
        p.setViewCount(doc.getViewCount());
        p.setSalesCount(doc.getSalesCount());
        p.setCreatedAt(doc.parseCreatedAt());
        return p;
    }
}
