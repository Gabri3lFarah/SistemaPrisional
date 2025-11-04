package com.gabrielfarah.visitas.service;

import com.gabrielfarah.visitas.entity.Visita;
import com.gabrielfarah.visitas.entity.Visitante;
import com.gabrielfarah.visitas.repository.VisitaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class VisitaService {

    private static final Logger LOG = Logger.getLogger(VisitaService.class);

    @Inject
    VisitaRepository visitaRepository;

    @Inject
    @RestClient
    PrisioneiroClient prisioneiroClient;

    @Transactional
    public Visita createVisita(Long prisioneiroId, Long visitanteId, LocalDateTime dataVisita, String observacoes) {
        // Validate visitante exists
        Visitante visitante = Visitante.findById(visitanteId);
        if (visitante == null) {
            throw new IllegalArgumentException("Visitante não encontrado com ID: " + visitanteId);
        }

        // Validate prisioneiro exists via REST client (with fallback)
        boolean prisioneiroExists = validatePrisioneiroExists(prisioneiroId);
        if (!prisioneiroExists) {
            LOG.warnf("Prisioneiro %d não encontrado ou serviço indisponível, criando visita como PENDENTE", prisioneiroId);
        }

        // Business rules for special prisoner (ID 41 - "Velho Viril")
        String status = "PENDENTE";
        String motivoNegacao = null;

        if (prisioneiroId == 41L) {
            if (Boolean.TRUE.equals(visitante.isAdvogado)) {
                if (visitante.codigoAutorizacao != null && visitante.codigoAutorizacao == 666) {
                    status = "AUTORIZADA";
                } else {
                    status = "NEGADA";
                    motivoNegacao = "Só advogados com código 666 podem visitar o Velho Viril!";
                }
            } else {
                status = "NEGADA";
                motivoNegacao = "Só advogados podem visitar o Velho Viril!";
            }
        } else if (prisioneiroExists) {
            status = "AUTORIZADA";
        }

        Visita visita = new Visita();
        visita.prisioneiroId = prisioneiroId;
        visita.visitanteId = visitanteId;
        visita.dataVisita = dataVisita;
        visita.status = status;
        visita.motivoNegacao = motivoNegacao;
        visita.observacoes = observacoes;

        visitaRepository.persist(visita);

        // TODO: Publish event to Kafka outbox for distributed systems
        LOG.infof("Visita criada: ID=%d, Status=%s", visita.id, visita.status);

        return visita;
    }

    @Timeout(5000) // 5 seconds timeout
    @Fallback(fallbackMethod = "validatePrisioneiroExistsFallback")
    public boolean validatePrisioneiroExists(Long prisioneiroId) {
        try {
            PrisioneiroClient.PrisioneiroDTO prisioneiro = prisioneiroClient.getPrisioneiro(prisioneiroId);
            return prisioneiro != null && prisioneiro.id != null;
        } catch (Exception e) {
            LOG.warnf("Erro ao validar prisioneiro %d: %s", prisioneiroId, e.getMessage());
            throw e; // Let fallback handle it
        }
    }

    public boolean validatePrisioneiroExistsFallback(Long prisioneiroId) {
        LOG.warnf("Fallback: serviço de prisioneiros indisponível para ID %d, assumindo pendente", prisioneiroId);
        return false; // Service unavailable, will create visit as PENDENTE
    }

    public List<Visita> listAll() {
        return visitaRepository.listAll();
    }

    public Visita findById(Long id) {
        return visitaRepository.findById(id);
    }

    public List<Visita> findByPrisioneiroId(Long prisioneiroId) {
        return visitaRepository.findByPrisioneiroId(prisioneiroId);
    }

    public List<Visita> findByVisitanteId(Long visitanteId) {
        return visitaRepository.findByVisitanteId(visitanteId);
    }

    @Transactional
    public Visita updateStatus(Long id, String status, String motivoNegacao) {
        Visita visita = visitaRepository.findById(id);
        if (visita == null) {
            throw new IllegalArgumentException("Visita não encontrada com ID: " + id);
        }
        visita.status = status;
        visita.motivoNegacao = motivoNegacao;
        visitaRepository.persist(visita);
        return visita;
    }
}
