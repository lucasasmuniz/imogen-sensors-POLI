package com.poli.embarcados.imogen.domain.dtos;

import com.poli.embarcados.imogen.domain.entities.Sensor;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record SensorDTO(String id,
                        @NotBlank(message = "Mandatory field") String type,
                        @NotBlank(message = "Mandatory field") String unit,
                        @NotBlank(message = "Mandatory field") BigDecimal value) {
    public SensorDTO(Sensor entity){
        this(entity.getId(), entity.getType(), entity.getUnit(), entity.getValue());
    }
}