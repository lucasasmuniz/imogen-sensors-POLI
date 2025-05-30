package com.poli.embarcados.imogen.services;

import com.poli.embarcados.imogen.domain.dtos.StationDTO;
import com.poli.embarcados.imogen.domain.entities.Station;
import com.poli.embarcados.imogen.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StationService {

    private final StationRepository repository;

    public List<StationDTO> findAll(){
        return repository.findAll().stream().map(StationDTO::new).toList();
    }

    @Transactional
    public StationDTO insert(StationDTO dto){
        Station entity = new Station();
        entity.setName(dto.name());
        entity.setLatitude(dto.latitude());
        entity.setLongitude(dto.longitude());
        entity.setElevationM(dto.elevationM());
        entity = repository.save(entity);
        return new StationDTO(entity);
    }
}
