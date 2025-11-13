package com.gabrielfarah.visitas.unit;

import com.gabrielfarah.visitas.entity.Visita;
import com.gabrielfarah.visitas.repository.VisitaRepository;
import com.gabrielfarah.visitas.service.VisitaService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class VisitaServiceTest {

    @Inject
    VisitaService visitaService;

    @InjectMock
    VisitaRepository visitaRepository;

    @Test
    void testListAll() {
        // Arrange
        List<Visita> visitas = new ArrayList<>();
        Visita visita = new Visita();
        visita.id = 1L;
        visitas.add(visita);
        
        when(visitaRepository.listAll()).thenReturn(visitas);

        // Act
        List<Visita> result = visitaService.listAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        // Arrange
        Visita visita = new Visita();
        visita.id = 1L;
        visita.status = "AUTORIZADA";
        
        when(visitaRepository.findById(1L)).thenReturn(visita);

        // Act
        Visita result = visitaService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("AUTORIZADA", result.status);
    }

    @Test
    void testFindByPrisioneiroId() {
        // Arrange
        List<Visita> visitas = new ArrayList<>();
        Visita visita = new Visita();
        visita.id = 1L;
        visita.prisioneiroId = 1L;
        visitas.add(visita);
        
        when(visitaRepository.findByPrisioneiroId(1L)).thenReturn(visitas);

        // Act
        List<Visita> result = visitaService.findByPrisioneiroId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).prisioneiroId);
    }

    @Test
    void testValidatePrisioneiroExistsFallback() {
        // Act
        boolean result = visitaService.validatePrisioneiroExistsFallback(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateStatus() {
        // Arrange
        Visita visita = new Visita();
        visita.id = 1L;
        visita.status = "PENDENTE";
        
        when(visitaRepository.findById(1L)).thenReturn(visita);

        // Act
        Visita result = visitaService.updateStatus(1L, "AUTORIZADA", null);

        // Assert
        assertNotNull(result);
        assertEquals("AUTORIZADA", result.status);
    }

    @Test
    void testUpdateStatus_NotFound() {
        // Arrange
        when(visitaRepository.findById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            visitaService.updateStatus(999L, "AUTORIZADA", null);
        });
    }
}
