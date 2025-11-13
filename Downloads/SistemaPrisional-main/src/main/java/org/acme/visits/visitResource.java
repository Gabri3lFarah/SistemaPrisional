package org.acme.visits;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entities.Prisoner;
import org.acme.repositories.PrisonerRepository;

@Path("/visits")
public class visitResource {

    @Inject
    PrisonerRepository prisonerRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleVisit(@Valid visitRequest request) {
        // Log para ver se prisioneiroId está chegando
        System.out.println("Validando prisioneiroId: " + request.getPrisioneiroId());

        // Verifica se um preso foi selecionado
        if (request == null || request.getPrisioneiroId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new visitResponse("Erro: É necessário selecionar um preso antes de cadastrar um visitante."))
                    .build();
        }

        // Busca o preso pelo ID
        Prisoner prisoner = prisonerRepository.findById(request.getPrisioneiroId());
        if (prisoner == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new visitResponse("Prisioneiro não encontrado."))
                    .build();
        }

        // Regras especiais para o preso ID 41
        if (request.getPrisioneiroId() == 41L) {
            if (Boolean.TRUE.equals(request.getLawyer())) {
                if (request.getAuthorizationCode() != null && request.getAuthorizationCode() == 666) {
                    return Response.ok(new visitResponse("Visita autorizada para advogado ao Velho Viril.")).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity(new visitResponse("Só advogados com código 666 podem visitar o Velho Viril!"))
                            .build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(new visitResponse("Só advogados podem visitar o Velho Viril!"))
                        .build();
            }
        }

        // Cadastro válido
        return Response.ok(new visitResponse("Visita registrada para " + prisoner.getName())).build();
    }
}
// visitResource moved/renamed to VisitResource.java
// Kept as placeholder to avoid breaking IDE references while refactoring.
