package com.poli.embarcados.imogen.domain.dtos;

import com.poli.embarcados.imogen.domain.entities.Lot;
import com.poli.embarcados.imogen.domain.entities.Sensor;
import com.poli.embarcados.imogen.domain.entities.Station;

import java.math.BigDecimal;

public record SensorInsertDTO(String id, String type, String unit, BigDecimal value, String stationId, String sensorId) {
    public SensorInsertDTO(Sensor entity, Station station, Lot lot){
        this(entity.getId(),
                entity.getType(),
                entity.getUnit(),
                entity.getValue(),
                station.getId(),
                lot.getId());
    }
}