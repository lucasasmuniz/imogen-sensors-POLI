package com.poli.embarcados.imogen.domain.dtos;

import com.poli.embarcados.imogen.domain.entities.Sensor;

import java.math.BigDecimal;

public record SensorDTO(String id, String type, String unit, BigDecimal value) {
    public SensorDTO(Sensor entity){
        this(entity.getId(), entity.getType(), entity.getUnit(), entity.getValue());
    }
}