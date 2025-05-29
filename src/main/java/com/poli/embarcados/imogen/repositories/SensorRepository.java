package com.poli.embarcados.imogen.repositories;

import com.poli.embarcados.imogen.domain.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {
}
