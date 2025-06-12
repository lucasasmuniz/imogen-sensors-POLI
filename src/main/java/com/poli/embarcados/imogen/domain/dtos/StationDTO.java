package com.poli.embarcados.imogen.domain.dtos;

import com.poli.embarcados.imogen.domain.entities.Sensor;
import com.poli.embarcados.imogen.domain.entities.Station;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


public record StationDTO(String id,
                         @NotBlank(message = "Required field") String name,
                         BigDecimal latitude,
                         BigDecimal longitude,
                         BigDecimal elevationM,
                         Set<SensorDTO> sensors) {

    public StationDTO(Station entity){
        this(entity.getId(),entity.getName(),entity.getLatitude(),entity.getLongitude(),entity.getElevationM(), Collections.emptySet());
    }

    public StationDTO(Station entity, Set<Sensor> sensors){
        this(entity.getId(),
                entity.getName(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getElevationM(),
                sensors.stream().map(SensorDTO::new).collect(Collectors.toSet()));
    }
}
