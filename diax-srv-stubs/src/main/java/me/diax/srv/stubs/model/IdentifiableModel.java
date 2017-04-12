package me.diax.srv.stubs.model;

import lombok.Data;

@Data
public abstract class IdentifiableModel implements Model, Identifiable<Long> {

    private Long id;

    @Override
    public boolean isNew() {
        return getId() == null;
    }
}
