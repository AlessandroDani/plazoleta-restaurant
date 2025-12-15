package com.pragma.powerup.infrastructure.out.http.mapper;

import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.infrastructure.out.http.response.TraceabilityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITraceabilityResponseMapper {
    Traceability toModel(TraceabilityResponseDto traceabilityResponseDto);
    List<Traceability> toModelList(List<TraceabilityResponseDto> traceabilityResponseDtoList);
}
