package com.gabrielfarah.visitas.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/prisoners")
@RegisterRestClient(configKey = "prisioneiro-api")
public interface PrisioneiroClient {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    PrisioneiroDTO getPrisioneiro(@PathParam("id") Long id);

    class PrisioneiroDTO {
        public Long id;
        public String name;
    }
}
