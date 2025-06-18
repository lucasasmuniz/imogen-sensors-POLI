package com.poli.embarcados.imogen.services;

import com.poli.embarcados.imogen.domain.dtos.StationDTO;
import com.poli.embarcados.imogen.domain.entities.Station;
import com.poli.embarcados.imogen.repositories.StationRepository;
import com.poli.embarcados.imogen.services.exceptions.ExistingDataException;
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
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class StationServiceTests {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository repository;

    private String existingId;
    private String nonExistingId;
    private String existingStationName;
    private String nonExistingStationName;
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
        dto = Factory.createStationDTO();
        dtoList = List.of(station);

        when(repository.findAll()).thenReturn(dtoList);

        when(repository.findById(existingId)).thenReturn(Optional.of(station));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(repository.findByName(existingStationName)).thenThrow(ExistingDataException.class);
        when(repository.findByName(nonExistingStationName)).thenReturn(station);

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
}
