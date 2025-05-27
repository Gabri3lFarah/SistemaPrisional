package org.acme.visits;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;

@RegisterForReflection
public class visitRequest {

    @NotNull(message = "É necessário selecionar um preso antes de cadastrar um visitante.")
    private Long prisioneiroId;

    private Boolean lawyer;
    private Integer authorizationCode;

    public Long getPrisioneiroId() {
        return prisioneiroId;
    }

    public void setPrisioneiroId(Long prisioneiroId) {
        this.prisioneiroId = prisioneiroId;
    }

    public Boolean getLawyer() {
        return lawyer;
    }

    public void setLawyer(Boolean lawyer) {
        this.lawyer = lawyer;
    }

    public Integer getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(Integer authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}
