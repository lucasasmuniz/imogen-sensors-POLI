package com.poli.embarcados.imogen.repositories;

import com.poli.embarcados.imogen.domain.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
}
