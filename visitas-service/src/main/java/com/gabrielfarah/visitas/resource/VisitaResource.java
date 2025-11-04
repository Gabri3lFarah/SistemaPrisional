package com.gabrielfarah.visitas.resource;

import com.gabrielfarah.visitas.entity.Visita;
import com.gabrielfarah.visitas.service.VisitaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@Path("/api/visitas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisitaResource {

    @Inject
    VisitaService visitaService;

    @GET
    public List<Visita> listAll() {
        return visitaService.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Visita visita = visitaService.findById(id);
        if (visita == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Visita não encontrada")
                    .build();
        }
        return Response.ok(visita).build();
    }

    @GET
    @Path("/prisioneiro/{prisioneiroId}")
    public List<Visita> getByPrisioneiroId(@PathParam("prisioneiroId") Long prisioneiroId) {
        return visitaService.findByPrisioneiroId(prisioneiroId);
    }

    @GET
    @Path("/visitante/{visitanteId}")
    public List<Visita> getByVisitanteId(@PathParam("visitanteId") Long visitanteId) {
        return visitaService.findByVisitanteId(visitanteId);
    }

    @POST
    public Response create(@Valid @NotNull VisitaRequest request) {
        if (request.prisioneiroId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Prisioneiro ID é obrigatório")
                    .build();
        }
        if (request.visitanteId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Visitante ID é obrigatório")
                    .build();
        }
        if (request.dataVisita == null) {
            request.dataVisita = LocalDateTime.now();
        }

        try {
            Visita visita = visitaService.createVisita(
                    request.prisioneiroId,
                    request.visitanteId,
                    request.dataVisita,
                    request.observacoes
            );
            return Response.status(Response.Status.CREATED).entity(visita).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, @Valid @NotNull StatusUpdateRequest request) {
        if (request.status == null || request.status.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Status é obrigatório")
                    .build();
        }

        try {
            Visita visita = visitaService.updateStatus(id, request.status, request.motivoNegacao);
            return Response.ok(visita).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    public static class VisitaRequest {
        @NotNull(message = "Prisioneiro ID é obrigatório")
        public Long prisioneiroId;
        
        @NotNull(message = "Visitante ID é obrigatório")
        public Long visitanteId;
        
        public LocalDateTime dataVisita;
        public String observacoes;
    }

    public static class StatusUpdateRequest {
        @NotNull(message = "Status é obrigatório")
        public String status;
        
        public String motivoNegacao;
    }
}
