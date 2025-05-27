package org.acme.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "prisioneiros") // Nome correto da tabela no banco
public class Prisoner extends PanacheEntity { // Mantém PanacheEntity que já gerencia o ID automaticamente

    @Column(name = "nome", nullable = false)
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
