package com.poli.embarcados.imogen.domain.dtos;

import com.poli.embarcados.imogen.domain.entities.Lot;
import com.poli.embarcados.imogen.domain.entities.Station;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public record LotDTO(String id, Instant timestamp, Set<StationDTO> stations) {
    public LotDTO(Lot entity){
        this(entity.getId(), entity.getTimestamp(), Collections.emptySet());
    }

    public LotDTO(Lot entity, Set<Station> stations){
        this(entity.getId(),
                entity.getTimestamp(),
                stations.stream().map(x -> new StationDTO(x, x.getSensors())).collect(Collectors.toSet()));
    }
}
