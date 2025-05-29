package com.poli.embarcados.imogen.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lot")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Instant timestamp;

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Sensor> sensors = new HashSet<>();

    public Lot(String id, Instant timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }
}
