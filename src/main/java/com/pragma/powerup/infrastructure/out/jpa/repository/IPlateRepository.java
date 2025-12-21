package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IPlateRepository extends JpaRepository<PlateEntity, Long> {
    boolean existsByName(String name);
    Page<PlateEntity> findByRestaurantIdAndActiveTrue(Long idRestaurant, Pageable pageable);
    Page<PlateEntity> findByRestaurantIdAndCategoryNameAndActiveTrue(Long idRestaurant, String category,  Pageable pageable);

    @Query("SELECT p.id FROM PlateEntity p WHERE p.restaurant.id = :restaurantId AND p.active = true")
    List<Long> findAllIdsByRestaurantId(@Param("restaurantId") Long restaurantId);
}
