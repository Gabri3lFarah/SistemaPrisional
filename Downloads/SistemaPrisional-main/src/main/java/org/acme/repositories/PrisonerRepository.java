package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entities.Prisoner;

@ApplicationScoped
public class PrisonerRepository implements PanacheRepository<Prisoner> {

}
