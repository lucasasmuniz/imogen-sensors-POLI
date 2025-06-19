package com.poli.embarcados.imogen.services;

import com.poli.embarcados.imogen.domain.dtos.LotDTO;
import com.poli.embarcados.imogen.domain.dtos.SensorDTO;
import com.poli.embarcados.imogen.domain.dtos.StationDTO;
import com.poli.embarcados.imogen.domain.entities.Lot;
import com.poli.embarcados.imogen.domain.entities.Sensor;
import com.poli.embarcados.imogen.domain.entities.Station;
import com.poli.embarcados.imogen.projections.StationProjection;
import com.poli.embarcados.imogen.repositories.LotRepository;
import com.poli.embarcados.imogen.repositories.SensorRepository;
import com.poli.embarcados.imogen.repositories.StationRepository;
import com.poli.embarcados.imogen.services.exceptions.InvalidDataException;
import com.poli.embarcados.imogen.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.poli.embarcados.imogen.specifications.LotSpecification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotRepository repository;
    private final StationRepository stationRepository;
    private final SensorRepository sensorRepository;

    @Transactional
    public LotDTO insert(LotDTO dto) {
        dto.stations().forEach(stationDTO -> {
            if(stationDTO.sensors() == null){
                throw new InvalidDataException("Station " + stationDTO.name() + " must have at least one sensor");
            }
        });
        if (dto.stations() == null || dto.stations().isEmpty()){
            throw new InvalidDataException("No stations provided");
        }

        Lot lotEntity = new Lot();
        lotEntity.setTimestamp(dto.timestamp());
        lotEntity = repository.save(lotEntity);

        Map<String, Station> stationEntityMap = processAndGetStationMap(dto.stations());
        Map<String, List<Sensor>> sensorEntityMap = new HashMap<>();

        List<Sensor> sensorsToSave = createSensorEntitiesFromDTOs(dto.stations(), stationEntityMap, sensorEntityMap, lotEntity);
        if (!sensorsToSave.isEmpty()) {
            sensorRepository.saveAll(sensorsToSave);
        }

        Set<Station> stationSetReturn = new HashSet<>();
        stationEntityMap.forEach((key, stationValue) -> {
            stationValue.getSensors().clear();
            stationValue.getSensors().addAll(sensorEntityMap.get(key));
            stationSetReturn.add(stationValue);
        });

        return new LotDTO(lotEntity, stationSetReturn);
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

    public List<Sensor> createSensorEntitiesFromDTOs(List<StationDTO> stationDtoList,
                                                     Map<String, Station> stationEntityMap,
                                                     Map<String, List<Sensor>> sensorEntityMap,
                                                     Lot lotEntity){
        List<Sensor> result = new ArrayList<>();

        for(StationDTO stationDto : stationDtoList){
            List<Sensor> sensorPerStation = new ArrayList<>();
            Station currentStationEntity = stationEntityMap.get(stationDto.name());

            if (currentStationEntity != null) {
                for(SensorDTO sensorDto : stationDto.sensors()) {
                    Sensor sensor = copySensorDtoToEntity(sensorDto, currentStationEntity, lotEntity);
                    sensorPerStation.add(sensor);
                    result.add(sensor);
                }
                sensorEntityMap.put(currentStationEntity.getName(), sensorPerStation);
            }
        }
        return result;
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

    @Transactional(readOnly = true)
    public Page<LotDTO> findAllPaged(LocalDate startDate, LocalDate endDate,Pageable pageable) {
        Instant startDateInstant = startDate == null ? null : startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endDateInstant = endDate == null ? null : endDate.plusDays(1L).atStartOfDay().toInstant(ZoneOffset.UTC);

        Specification<Lot> spec = LotSpecification.filterLotByDataRange(startDateInstant, endDateInstant);

        return repository.findAll(spec, pageable).map(LotDTO::new);
    }

    @Transactional(readOnly = true)
    public LotDTO findById(String id) {
        Lot lot = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lot with id " + id + " not found"));
        List<StationProjection> stations = stationRepository.searchByLotId(id);
        List<String> list = stations.stream().map(StationProjection::getId).toList();
        List<Station> stationEntities = stationRepository.searchByIdInList(list);
        stationEntities.forEach(station -> {
            List<Sensor> filteredSensors = station.getSensors()
                    .stream()
                    .filter(sensor -> sensor.getLot().getId().equals(id))
                    .toList(); // ou .collect(Collectors.toList()) em versões mais antigas do Java

            // 3. Substitui a lista original de sensores pela lista filtrada
            station.setSensors(new HashSet<>(filteredSensors));
        });


        return new LotDTO(lot, new HashSet<>(stationEntities));
    }
}