package com.poli.embarcados.imogen.services;

import com.poli.embarcados.imogen.domain.dtos.LotDTO;
import com.poli.embarcados.imogen.domain.dtos.SensorDTO;
import com.poli.embarcados.imogen.domain.dtos.StationDTO;
import com.poli.embarcados.imogen.domain.entities.Lot;
import com.poli.embarcados.imogen.domain.entities.Sensor;
import com.poli.embarcados.imogen.domain.entities.Station;
import com.poli.embarcados.imogen.repositories.LotRepository;
import com.poli.embarcados.imogen.repositories.SensorRepository;
import com.poli.embarcados.imogen.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotRepository repository;
    private final StationRepository stationRepository;
    private final SensorRepository sensorRepository;

    @Transactional
    public void insert(LotDTO dto) {
        Lot lotEntity = new Lot();
        lotEntity.setTimestamp(dto.timestamp());
        lotEntity = repository.save(lotEntity);

        Map<String, Station> stationEntityMap = processAndGetStationMap(dto.stations());

        List<Sensor> sensorsToSave = createSensorEntitiesFromDTOs(dto.stations(), stationEntityMap, lotEntity);
        if (!sensorsToSave.isEmpty()) {
            sensorRepository.saveAll(sensorsToSave);
        }
    }

    private Map<String, Station> processAndGetStationMap(List<StationDTO> stationDTOs) {
        if (stationDTOs == null || stationDTOs.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<String> stationNames = stationDTOs.stream().map(StationDTO::name).collect(Collectors.toSet());
        List<Station> existingStations = stationRepository.findAllByNameIn(stationNames);

        Map<String, Station> stationMap = existingStations.stream()
                .collect(Collectors.toMap(Station::getName, station -> station));

        List<Station> newStationsToCreate = new ArrayList<>();
        for (StationDTO dto : stationDTOs) {
            if (!stationMap.containsKey(dto.name())) {
                newStationsToCreate.add(copyStationDtoToEntity(dto));
            }
        }

        if (!newStationsToCreate.isEmpty()) {
            List<Station> savedNewStations = stationRepository.saveAll(newStationsToCreate);
            savedNewStations.forEach(s -> stationMap.put(s.getName(), s));
        }
        return stationMap;
    }

    public List<Sensor> createSensorEntitiesFromDTOs(List<StationDTO> stationDtoList, Map<String, Station> stationEntityMap, Lot lotEntity){
        List<Sensor> list = new ArrayList<>();
        for(StationDTO stationDto : stationDtoList){
            Station currentStationEntity = stationEntityMap.get(stationDto.name());
            if (currentStationEntity != null && stationDto.sensors() != null) {
                for(SensorDTO sensorDto : stationDto.sensors()) {
                    list.add(copySensorDtoToEntity(sensorDto, currentStationEntity, lotEntity));
                }
            } else {
                // Need to throw something, provavelmente um badrequest
            }
        }
        return list;
    }

    public Station copyStationDtoToEntity(StationDTO dto){
        Station station = new Station();
        station.setName(dto.name());
        station.setLongitude(dto.longitude());
        station.setElevationM(dto.elevationM());
        station.setLatitude(dto.latitude());
        return station;
    }

    public Sensor copySensorDtoToEntity(SensorDTO sensorDto, Station stationEntity, Lot lotEntity){
        Sensor entity = new Sensor();
        entity.setUnit(sensorDto.unit());
        entity.setType(sensorDto.type());
        entity.setValue(sensorDto.value());
        entity.setStation(stationEntity); // Usa a entidade Station já carregada/criada
        entity.setLot(lotEntity);       // Usa a entidade Lot já carregada/criada
        return entity;
    }
}