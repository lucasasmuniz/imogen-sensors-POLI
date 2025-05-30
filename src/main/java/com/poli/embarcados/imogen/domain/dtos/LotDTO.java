package com.poli.embarcados.imogen.domain.dtos;

import com.poli.embarcados.imogen.domain.entities.Lot;
import com.poli.embarcados.imogen.domain.entities.Station;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public record LotDTO(String id, Instant timestamp, List<StationDTO> stations) {
    public LotDTO(Lot entity){
        this(entity.getId(), entity.getTimestamp(), Collections.emptyList());
    }

    public LotDTO(Lot entity, Set<Station> stations){
        this(entity.getId(),
                entity.getTimestamp(),
                stations.stream().map(x -> new StationDTO(x, x.getSensors())).toList());
    }
}
