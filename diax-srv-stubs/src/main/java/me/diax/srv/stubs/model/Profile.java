package me.diax.srv.stubs.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "profile")
@XmlAccessorType(XmlAccessType.FIELD)
public class Profile extends IdentifiableModel {

}
