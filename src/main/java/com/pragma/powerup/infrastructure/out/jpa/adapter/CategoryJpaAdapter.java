package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {
    private final ICategoryRepository categoryRepository;

    @Override
    public boolean existsCategoryById(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public boolean existByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
