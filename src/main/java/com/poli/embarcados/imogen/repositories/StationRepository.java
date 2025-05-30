package com.poli.embarcados.imogen.repositories;

import com.poli.embarcados.imogen.domain.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {

    Station findByName(String name);
    List<Station> findAllByNameIn(Collection<String> names);
}
