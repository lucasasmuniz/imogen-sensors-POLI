package com.poli.embarcados.imogen.repositories;

import com.poli.embarcados.imogen.domain.entities.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<Lot, String> {
}
