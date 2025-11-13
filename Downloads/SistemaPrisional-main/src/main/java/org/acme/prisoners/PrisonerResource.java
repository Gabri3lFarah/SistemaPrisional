package org.acme.prisoners;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entities.Prisoner;
import org.acme.repositories.PrisonerRepository;

import java.util.List;

@Path("/prisoners")
public class PrisonerResource {

    @Inject
    PrisonerRepository prisonerRepository;

    @GET
    @Path("/all") // Define um novo caminho para um dos métodos
    @Produces(MediaType.APPLICATION_JSON)
    public List<Prisoner> getAll() {
        return prisonerRepository.listAll();
    }

    @GET
    @Path("/list") // Define outro caminho para evitar conflito
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPrisioneiros() {
        List<Prisoner> prisioneiros = prisonerRepository.listAll();
        System.out.println("Prisioneiros encontrados: " + prisioneiros); // Debug
        return Response.ok(prisioneiros).build();
    }


}
