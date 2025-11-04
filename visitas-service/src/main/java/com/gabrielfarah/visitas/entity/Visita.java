package com.gabrielfarah.visitas.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitas")
public class Visita extends PanacheEntity {

    @Column(name = "prisioneiro_id", nullable = false)
    public Long prisioneiroId;

    @Column(name = "visitante_id", nullable = false)
    public Long visitanteId;

    @Column(name = "data_visita", nullable = false)
    public LocalDateTime dataVisita;

    @Column(name = "status", nullable = false, length = 50)
    public String status; // AUTORIZADA, NEGADA, PENDENTE

    @Column(name = "motivo_negacao", length = 500)
    public String motivoNegacao;

    @Column(name = "observacoes", length = 1000)
    public String observacoes;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "PENDENTE";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
