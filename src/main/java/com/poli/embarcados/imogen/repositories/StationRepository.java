package com.poli.embarcados.imogen.repositories;

import com.poli.embarcados.imogen.domain.entities.Station;
import com.poli.embarcados.imogen.projections.StationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {

    Station findByName(String name);
    List<Station> findAllByNameIn(Collection<String> names);

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT s.id FROM station s INNER JOIN sensor sen on sen.station_id = s.id WHERE sen.lot_id = :lotId")
    List<StationProjection> searchByLotId(@Param("lotId") String lotId);

    @Query("SELECT s FROM Station s JOIN FETCH s.sensors WHERE s.id IN :ids")
    List<Station> searchByIdInList(@Param("ids") List<String> ids);
}
