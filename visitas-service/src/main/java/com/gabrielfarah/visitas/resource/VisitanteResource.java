package com.gabrielfarah.visitas.resource;

import com.gabrielfarah.visitas.entity.Visitante;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/visitantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisitanteResource {

    @GET
    public List<Visitante> listAll() {
        return Visitante.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Visitante visitante = Visitante.findById(id);
        if (visitante == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Visitante não encontrado")
                    .build();
        }
        return Response.ok(visitante).build();
    }

    @POST
    @Transactional
    public Response create(@Valid @NotNull VisitanteRequest request) {
        if (request.nome == null || request.nome.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nome é obrigatório")
                    .build();
        }
        if (request.cpf == null || request.cpf.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("CPF é obrigatório")
                    .build();
        }

        // Check if CPF already exists
        Visitante existing = Visitante.find("cpf", request.cpf).firstResult();
        if (existing != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("CPF já cadastrado")
                    .build();
        }

        Visitante visitante = new Visitante();
        visitante.nome = request.nome;
        visitante.cpf = request.cpf;
        visitante.telefone = request.telefone;
        visitante.relacaoPrisioneiro = request.relacaoPrisioneiro;
        visitante.isAdvogado = request.isAdvogado != null ? request.isAdvogado : false;
        visitante.codigoAutorizacao = request.codigoAutorizacao;
        
        visitante.persist();
        
        return Response.status(Response.Status.CREATED).entity(visitante).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid @NotNull VisitanteRequest request) {
        Visitante visitante = Visitante.findById(id);
        if (visitante == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Visitante não encontrado")
                    .build();
        }

        if (request.nome != null && !request.nome.trim().isEmpty()) {
            visitante.nome = request.nome;
        }
        if (request.telefone != null) {
            visitante.telefone = request.telefone;
        }
        if (request.relacaoPrisioneiro != null) {
            visitante.relacaoPrisioneiro = request.relacaoPrisioneiro;
        }
        if (request.isAdvogado != null) {
            visitante.isAdvogado = request.isAdvogado;
        }
        if (request.codigoAutorizacao != null) {
            visitante.codigoAutorizacao = request.codigoAutorizacao;
        }

        visitante.persist();
        
        return Response.ok(visitante).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Visitante visitante = Visitante.findById(id);
        if (visitante == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Visitante não encontrado")
                    .build();
        }
        visitante.delete();
        return Response.noContent().build();
    }

    public static class VisitanteRequest {
        @NotNull(message = "Nome é obrigatório")
        public String nome;
        
        @NotNull(message = "CPF é obrigatório")
        public String cpf;
        
        public String telefone;
        public String relacaoPrisioneiro;
        public Boolean isAdvogado;
        public Integer codigoAutorizacao;
    }
}
