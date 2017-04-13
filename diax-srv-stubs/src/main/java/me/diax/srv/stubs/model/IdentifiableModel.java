package me.diax.srv.stubs.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class IdentifiableModel implements Model, Identifiable<Long> {

    @Getter
    @Setter
    @XmlElement(name = "id")
    private Long id;

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public String toString() {
        return "IdentifiableModel{" +
                "id=" + id +
                '}';
    }
}
