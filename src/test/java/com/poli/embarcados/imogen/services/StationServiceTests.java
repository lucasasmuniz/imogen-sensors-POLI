package com.poli.embarcados.imogen.services;

import com.poli.embarcados.imogen.domain.dtos.StationDTO;
import com.poli.embarcados.imogen.domain.entities.Station;
import com.poli.embarcados.imogen.repositories.StationRepository;
import com.poli.embarcados.imogen.services.exceptions.ExistingDataException;
import com.poli.embarcados.imogen.services.exceptions.ResourceNotFoundException;
import com.poli.embarcados.imogen.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class StationServiceTests {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository repository;

    private String existingId,nonExistingId,existingStationName,nonExistingStationName;
    private Station station;
    private StationDTO dto;
    private List<Station> dtoList;

    @BeforeEach
    void setUp() throws Exception{
        existingId = String.valueOf(UUID.randomUUID());
        nonExistingId = String.valueOf(UUID.randomUUID());
        existingStationName = "STN001";
        nonExistingStationName = "STN002";
        station = Factory.createStation();
        dto = new StationDTO(station);
        dtoList = List.of(station);

        when(repository.findAll()).thenReturn(dtoList);

        when(repository.findById(existingId)).thenReturn(Optional.of(station));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(repository.findByName(existingStationName)).thenReturn(station);
        when(repository.findByName(nonExistingStationName)).thenReturn(null);

        when(repository.save(any())).thenReturn(station);
    }

    @Test
    void findAllShouldReturnListOfStations(){
        List<StationDTO> result = stationService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(station.getId(), result.getFirst().id());
        assertEquals(station.getName(), result.getFirst().name());
        assertEquals(station.getLatitude(), result.getFirst().latitude());
        assertEquals(station.getLongitude(), result.getFirst().longitude());
        assertEquals(station.getElevationM(), result.getFirst().elevationM());
    }

    @Test
    void findByIdShouldReturnStationDTOWhenIdExists() {
        StationDTO result = stationService.findById(existingId);

        assertNotNull(result);
        assertEquals(station.getId(), result.id());
        assertEquals(station.getName(), result.name());
        assertEquals(station.getLatitude(), result.latitude());
        assertEquals(station.getLongitude(), result.longitude());
        assertEquals(station.getElevationM(), result.elevationM());
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            stationService.findById(nonExistingId);
        });

        verify(repository, times(1)).findById(nonExistingId);
    }

    @Test
    void insertShouldInsertStationWhenNameDoesNotExist() {
        station.setName(nonExistingStationName);
        dto = new StationDTO(station);

        StationDTO result = stationService.insert(dto);

        assertNotNull(result);
        assertEquals(dto.name(), result.name());
        assertEquals(dto.latitude(), result.latitude());
        assertEquals(dto.longitude(), result.longitude());
        assertEquals(dto.elevationM(), result.elevationM());
    }

    @Test
    void insertShouldThrowExistingDataExceptionWhenNameAlreadyExists() {
        assertThrows(ExistingDataException.class, () -> {
            stationService.insert(dto);
        });

        verify(repository, times(1)).findByName(existingStationName);
    }
}
