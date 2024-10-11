package com.programmers.shoppingmall.repository;

import com.programmers.shoppingmall.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
