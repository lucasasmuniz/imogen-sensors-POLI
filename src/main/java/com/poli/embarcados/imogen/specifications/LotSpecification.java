package com.poli.embarcados.imogen.specifications;

import com.poli.embarcados.imogen.domain.entities.Lot;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LotSpecification {

    public static Specification<Lot> filterLotByDataRange(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(startDate != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), startDate));
            }

            if(endDate != null){
                predicates.add(criteriaBuilder.lessThan(root.get("timestamp"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
