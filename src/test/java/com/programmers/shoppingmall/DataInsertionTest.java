package com.programmers.shoppingmall;

import com.programmers.shoppingmall.entity.Category;
import com.programmers.shoppingmall.entity.Product;
import com.programmers.shoppingmall.entity.Review;
import com.programmers.shoppingmall.entity.User;
import com.programmers.shoppingmall.repository.CategoryRepository;
import com.programmers.shoppingmall.repository.ProductRepository;
import com.programmers.shoppingmall.repository.ReviewRepository;
import com.programmers.shoppingmall.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataInsertionTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private List<User> users = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();

    @BeforeEach
    @Transactional
    public void insertTestData() {
        // 20명의 사용자 데이터 생성
        for (int i = 1; i <= 20; i++) {
            User user = new User();
            user.setName("User " + i);
            users.add(userRepository.save(user));  // 사용자 저장
        }

        // 20개의 카테고리 데이터 생성
        for (int i = 1; i <= 20; i++) {
            Category category = new Category();
            category.setName("Category " + i);
            categories.add(categoryRepository.save(category));  // 카테고리 저장
        }

        // 20개의 제품 데이터 생성
        for (int i = 1; i <= 20; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setPrice(i * 1000);
            product.setCategory(categories.get(i % categories.size()));  // 카테고리 설정
            products.add(productRepository.save(product));  // 제품 저장
        }

        // 20개의 리뷰 데이터 생성
        for (int i = 1; i <= 20; i++) {
            Review review = new Review();
            review.setContent("This is review " + i);
            review.setUser(users.get(i % users.size()));  // 사용자 설정
            review.setProduct(products.get(i % products.size()));  // 제품 설정
            reviews.add(reviewRepository.save(review));  // 리뷰 저장
        }
    }

    @Test
    public void testDataInsertion() {
        assert (userRepository.count() == 20);
        assert (categoryRepository.count() == 20);
        assert (productRepository.count() == 20);
        assert (reviewRepository.count() == 20);
    }
}