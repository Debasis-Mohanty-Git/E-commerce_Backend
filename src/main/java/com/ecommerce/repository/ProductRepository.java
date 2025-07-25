package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
           "WHERE (:category IS NULL OR p.category.name = :category) " +
           "AND (:minPrice IS NULL OR p.discountedPrice >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.discountedPrice <= :maxPrice) " +
           "AND (:minDiscount IS NULL OR p.discountPresent >= :minDiscount)")
    List<Product> filterProducts(
            @Param("category") String category,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("minDiscount") Integer minDiscount
    );
}
