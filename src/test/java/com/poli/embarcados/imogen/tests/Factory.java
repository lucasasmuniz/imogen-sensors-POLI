package com.poli.embarcados.imogen.tests;

import com.poli.embarcados.imogen.domain.dtos.StationDTO;
import com.poli.embarcados.imogen.domain.entities.Station;

import java.math.BigDecimal;

public class Factory {

    public static Station createStation() {
        Station station = new Station();
        station.setName("STN001");
        station.setLatitude(BigDecimal.valueOf(-12.34));
        station.setLongitude(BigDecimal.valueOf(56.78));
        station.setElevationM(BigDecimal.valueOf(100.0));
        return station;
    }

    public static StationDTO createStationDTO() {
        Station station = createStation();
        return new StationDTO(station);
    }
}
