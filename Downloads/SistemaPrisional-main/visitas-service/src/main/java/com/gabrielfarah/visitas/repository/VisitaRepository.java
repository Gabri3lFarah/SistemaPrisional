package com.gabrielfarah.visitas.repository;

import com.gabrielfarah.visitas.entity.Visita;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class VisitaRepository implements PanacheRepository<Visita> {

    public List<Visita> findByPrisioneiroId(Long prisioneiroId) {
        return list("prisioneiroId", prisioneiroId);
    }

    public List<Visita> findByVisitanteId(Long visitanteId) {
        return list("visitanteId", visitanteId);
    }

    public List<Visita> findByStatus(String status) {
        return list("status", status);
    }
}
