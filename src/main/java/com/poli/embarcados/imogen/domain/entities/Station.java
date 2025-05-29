package com.poli.embarcados.imogen.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "station")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "sensors")
@EqualsAndHashCode(of = "id")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(precision = 9, scale = 4, nullable = false)
    private BigDecimal latitude;
    @Column(precision = 9, scale = 4, nullable = false)
    private BigDecimal longitude;
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal elevationM;

    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY)
    private Set<Sensor> sensors = new HashSet<>();

    public Station(String id, String name, BigDecimal latitude, BigDecimal longitude, BigDecimal elevationM) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevationM = elevationM;
    }
}
