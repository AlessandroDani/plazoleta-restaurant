package com.pragma.powerup.domain.spi;

public interface ICategoryPersistencePort {
    boolean existsCategoryById(Long id);
}
