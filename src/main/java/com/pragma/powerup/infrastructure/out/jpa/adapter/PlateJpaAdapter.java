package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPlateEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPlateRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlateJpaAdapter implements IPlatePersistencePort {

    private final IPlateRepository plateRepository;
    private final IPlateEntityMapper plateEntityMapper;
    private final IRestaurantRepository  restaurantRepository;

    @Override
    public void savePlate(Plate plate) {
        PlateEntity plateEntity = plateEntityMapper.toEntity(plate);
        restaurantRepository.findById(plate.getIdRestaurant())
                .ifPresent(plateEntity::setRestaurant);
        plateRepository.save(plateEntity);
    }

    @Override
    public Plate getPlateById(Long id) {
        Optional<PlateEntity> plateEntity = plateRepository.findById(id);
        return plateEntity.map(plateEntityMapper::toPlate).orElse(null);

    }

    @Override
    public Plate getPlateByName(String name) {
        Optional<PlateEntity> plateEntity = plateRepository.findByName(name);
        return plateEntity.map(plateEntityMapper::toPlate).orElse(null);
    }

    @Override
    public void updatePlate(Plate plate) {
        savePlate(plate);
    }

    @Override
    public List<Plate> getPlatesByRestaurant(Long idRestaurant, int page, int size, String category) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<PlateEntity> platePage;

        if (category != null && !category.isEmpty()) {
            platePage = plateRepository.findByRestaurantIdAndCategoryName(idRestaurant, category, pageable);
        } else {
            platePage = plateRepository.findByRestaurantId(idRestaurant, pageable);
        }

        return plateEntityMapper.toPlateList(platePage.getContent());
    }
}
