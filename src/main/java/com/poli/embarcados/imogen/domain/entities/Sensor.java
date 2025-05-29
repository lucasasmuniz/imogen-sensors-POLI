package com.poli.embarcados.imogen.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sensor")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String unit;
    @Column(name = "reading_value", nullable = false)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    @ToString.Exclude
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false)
    @ToString.Exclude
    private Lot lot;


    public Sensor(String id, String type, String unit, BigDecimal value) {
        this.id = id;
        this.type = type;
        this.unit = unit;
        this.value = value;
    }
}
