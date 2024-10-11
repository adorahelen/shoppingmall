package com.programmers.shoppingmall;

import com.programmers.shoppingmall.dto.ProductDto;
import com.programmers.shoppingmall.entity.Category;
import com.programmers.shoppingmall.entity.Product;
import com.programmers.shoppingmall.entity.User;
import com.programmers.shoppingmall.repository.CategoryRepository;
import com.programmers.shoppingmall.repository.ProductRepository;
import com.programmers.shoppingmall.repository.ReviewRepository;
import com.programmers.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
// import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private static final Logger log = LoggerFactory.getLogger(RestApiController.class);
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

// 문제 0 유저 리스트 전부 조회하기
    @GetMapping("/user")
   // @ResponseBody vs ResponseEntity<T> : http 응답바디에 담는 두가지 방법
    public ResponseEntity<List<String>> getUserList() {
        // 스트링이 아닌, 유저 엔티티를 사용하게 되면, 유저의 모든 객체가 반환
        // -> 이처럼 String 을 쓰거나,
        // ->이후에 작성된 코드처럼 dto 를 통해 객체에서 필요한 필드만 선택적으로 반환 가능함
        List<User> users = userRepository.findAll();  // 전체 유저 조회
        List<String> names = new ArrayList<>();

        // 유저의 이름만 추출하여 리스트에 추가
        for (User user : users) {
            names.add(user.getName());
        }

        // 이름 리스트를 오름차순으로 정렬
        Collections.sort(names);

        // 정렬된 이름 리스트를 반환 *
        return ResponseEntity.ok(names);
        // 헤더에는 딱히 뭐 없고, 맨 앞 상태코드는 자동 지정(200) + 바디(데이터)

    }// 예외처리는 => try catch & if/else 사용

    // 문제 1 전체 제품 리스트를 조회
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProductList() {

       try {


           List<Product> products = productRepository.findAll();

           List<ProductDto> Dtos = new ArrayList<>();
           for (Product product : products) {

               ProductDto productDto = new ProductDto();
               productDto.setName(product.getName());
               productDto.setPrice(product.getPrice());
               Dtos.add(productDto);

           }
           return ResponseEntity.ok(Dtos);
       } catch (Exception e) {
           log.error(e.getMessage());
           return ResponseEntity.badRequest().build();
       }
    }

    // 문제 2
    // DTO 사용하여 JSON data 반환
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        try {

            Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();

        }
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        return ResponseEntity.ok(productDto);
    }
        catch (Exception e) {
        return ResponseEntity.notFound().build();
        }
    }


    // 문제 5: 카테고리 ID로 카테고리 조회
    //  DTO 사용 없이 직접 JSON 응답 생성
    @GetMapping("/category/{id}")
//    public ResponseEntity<String> getCategoryById(@PathVariable Long id) {
    public ResponseEntity<Object> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryRepository.findById(id).orElse(null);
            if (category == null) {
                return ResponseEntity.status(404).body("카테고리를 찾을 수 없습니다.");
            }

            Map<String, String> response = new HashMap<>();
            response.put("name", category.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

    // 문제 4: 카테고리 목록 조회
//@GetMapping("/categories")
//public ResponseEntity<List<String>> getCategoryList() {
//    List<Category> categories = categoryRepository.findAll();
//    List<String> categoryNames = new ArrayList<>();
//
//    for (Category category : categories) {
//        categoryNames.add(category.getName());
//    }
//
//    Collections.sort(categoryNames);
//
//
//    return ResponseEntity.ok(categoryNames);
//}
// 문제 조건에 나와 있는, 앞에 스트링을 찍으려면, 마트료시카 처럼 담고 또 담아야함
@GetMapping("/categories")
public ResponseEntity<Map<String, List<String>>> getCategoryList() {

    List<Category> categories = categoryRepository.findAll();
    List<String> categoryNames = new ArrayList<>();

    // 카테고리 이름 리스트 생성
    for (Category category : categories) {
        categoryNames.add(category.getName());
    }

    // 이름을 오름차순으로 정렬
    Collections.sort(categoryNames);

    // 응답 데이터 구성
    Map<String, List<String>> response = new HashMap<>();
    response.put("categories", categoryNames);

    return ResponseEntity.ok(response);

}


    // 문제 3 : ://localhost:8080/products/category/1
    // 문제 3: 카테고리 ID로 제품 조회
    @GetMapping("/products/category/{id}")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable Long id) {
        try {
            List<Product> products = productRepository.findByCategoryId(id);
            if (products.isEmpty()) {
                return ResponseEntity.status(404).body("해당 카테고리에 제품이 없습니다.");
            }

            List<ProductDto> productDtos = new ArrayList<>();

            // Product -> ProductDto 변환
            for (Product product : products) {
                ProductDto productDto = new ProductDto();
                productDto.setName(product.getName());
                productDto.setPrice(product.getPrice());
                productDtos.add(productDto);
            }

            return ResponseEntity.ok(productDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

}
// orElse(null) : null <<<<< Optional : orElseThrow (디버깅 용이)
// 스트림으로 간결하고 표현 가능함
// try catch 등은 서비스단에서 작성이 맞음
// 상태코드 반환보다, 메시지 반환이 더 좋음(에러)
// 전역 예외 처리 클래스(예: @ControllerAdvice)를 사용할 것
