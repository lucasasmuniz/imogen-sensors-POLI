package com.poli.embarcados.imogen.repositories;

import com.poli.embarcados.imogen.domain.entities.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<Lot, String>, JpaSpecificationExecutor<Lot> {

    @Query("SELECT obj FROM Lot obj JOIN FETCH obj.sensors WHERE obj.id = :lotId")
    Lot searchLotById(@Param("lotId") String lotId);

}
