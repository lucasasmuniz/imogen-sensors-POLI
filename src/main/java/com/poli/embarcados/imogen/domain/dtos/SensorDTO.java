package com.poli.embarcados.imogen.domain.dtos;

import com.poli.embarcados.imogen.domain.entities.Lot;
import com.poli.embarcados.imogen.domain.entities.Sensor;
import com.poli.embarcados.imogen.domain.entities.Station;

import java.math.BigDecimal;

public record SensorDTO(String id,String type,String unit, BigDecimal value, StationDTO station, LotDTO sensor) {
    public SensorDTO(Sensor entity){
        this(entity.getId(), entity.getType(), entity.getUnit(), entity.getValue(), null, null);
    }

    public SensorDTO(Sensor entity, Station station, Lot lot){
        this(entity.getId(), entity.getType(), entity.getUnit(), entity.getValue(), new StationDTO(station), new LotDTO(lot));
    }
}