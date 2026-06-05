package com.yoimiya.onlinemall.service;

import com.yoimiya.onlinemall.entity.Review;
import com.yoimiya.onlinemall.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    public Review addReview(Review review) {
        int exists = reviewMapper.existsByUserOrderProduct(review.getUserId(), review.getOrderNo(), review.getProductId());
        if (exists > 0) {
            throw new RuntimeException("该商品已评价");
        }
        reviewMapper.insert(review);
        return review;
    }

    public List<Review> getProductReviews(Long productId) {
        return reviewMapper.findByProductId(productId);
    }

    public List<Review> getUserReviews(Long userId) {
        return reviewMapper.findByUserId(userId);
    }
}
