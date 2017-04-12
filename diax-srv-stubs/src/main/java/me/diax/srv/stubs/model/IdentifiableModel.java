package me.diax.srv.stubs.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

@Data
public abstract class IdentifiableModel implements Model, Identifiable<Long> {

    @XmlElement(name = "id")
    private Long id;

    @Override
    public boolean isNew() {
        return getId() == null;
    }
}
