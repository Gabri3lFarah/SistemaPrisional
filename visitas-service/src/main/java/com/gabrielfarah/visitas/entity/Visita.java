package com.gabrielfarah.visitas.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitas")
public class Visita extends PanacheEntity {

    @Column(name = "prisioneiro_id", nullable = false)
    public Long prisioneiroId;

    @Column(name = "visitante_id")
    public Long visitanteId;

    @Column(name = "data_visita", nullable = false)
    public LocalDateTime dataVisita;

    @Column(name = "nome_visitante", length = 255)
    public String nomeVisitante;

    @Column(name = "status", length = 50)
    public String status;
}