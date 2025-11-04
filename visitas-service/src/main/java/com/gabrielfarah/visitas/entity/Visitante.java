package com.gabrielfarah.visitas.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitantes")
public class Visitante extends PanacheEntity {

    @Column(name = "nome", nullable = false, length = 200)
    public String nome;

    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    public String cpf;

    @Column(name = "telefone", length = 15)
    public String telefone;

    @Column(name = "relacao_prisioneiro", length = 100)
    public String relacaoPrisioneiro;

    @Column(name = "is_advogado", nullable = false)
    public Boolean isAdvogado = false;

    @Column(name = "codigo_autorizacao")
    public Integer codigoAutorizacao;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
