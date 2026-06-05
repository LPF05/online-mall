package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.entity.Review;
import com.yoimiya.onlinemall.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Result<Review> addReview(@RequestBody Review review, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        review.setUserId(userId);
        return Result.success(reviewService.addReview(review));
    }

    @GetMapping("/product/{productId}")
    public Result<List<Review>> getProductReviews(@PathVariable Long productId) {
        return Result.success(reviewService.getProductReviews(productId));
    }

    @GetMapping("/my")
    public Result<List<Review>> getMyReviews(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(reviewService.getUserReviews(userId));
    }
}
