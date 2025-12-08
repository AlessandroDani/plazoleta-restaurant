package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPlateRepository extends JpaRepository<PlateEntity, Long> {

    Optional<PlateEntity> findByName(String name);

    Page<PlateEntity> findByRestaurantIdAndActiveTrue(Long idRestaurant, Pageable pageable);

    Page<PlateEntity> findByRestaurantIdAndCategoryNameAndActiveTrue(Long idRestaurant, String category,  Pageable pageable);
}
