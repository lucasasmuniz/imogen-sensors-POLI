package com.poli.embarcados.imogen.services;

import com.poli.embarcados.imogen.domain.dtos.LotDTO;
import com.poli.embarcados.imogen.domain.entities.Lot;
import com.poli.embarcados.imogen.repositories.LotRepository;
import com.poli.embarcados.imogen.repositories.SensorRepository;
import com.poli.embarcados.imogen.repositories.StationRepository;
import com.poli.embarcados.imogen.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class LotServiceTests {

    @InjectMocks
    private LotService service;

    @Mock
    private LotRepository repository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private SensorRepository sensorRepository;

    private String existingId, nonExistingId;
    private Lot lotEmptyStation;
    private LotDTO lotDTO;
    private PageImpl<Lot> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = String.valueOf(UUID.randomUUID());
        nonExistingId = String.valueOf(UUID.randomUUID());
        lotEmptyStation = Factory.createLotEmptyStation();
        lotDTO = new LotDTO(lotEmptyStation);
        page = new PageImpl<>(List.of(lotEmptyStation));

        when(repository.findAll((Specification<Lot>) any(), (Pageable)any())).thenReturn(page);
    }

    @Test
    void findAllShouldReturnPage(){
        PageRequest pageRequest = PageRequest.of(0, 12);

        Page<LotDTO> result = service.findAllPaged(LocalDate.now(), LocalDate.now(), pageRequest);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertEquals(lotEmptyStation.getId() ,result.getContent().getFirst().id());
    }
}
