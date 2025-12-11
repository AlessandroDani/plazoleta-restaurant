package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlateEntityMapper{
    @Mapping(source = "idCategory", target = "category.id")
    @Mapping(source = "idRestaurant", target = "restaurant.id")
    PlateEntity toEntity(Plate plate);

    @Mapping(source = "restaurant.id", target = "idRestaurant")
    @Mapping(source = "category.id", target = "idCategory")
    Plate toPlate(PlateEntity plateEntity);


    List<Plate> toPlateList(List<PlateEntity> plateEntities);

}
