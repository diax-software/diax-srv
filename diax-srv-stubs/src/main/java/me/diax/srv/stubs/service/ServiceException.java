package me.diax.srv.stubs.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@XmlRootElement(name = "exception")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceException extends Exception {
    @Getter
    @Setter
    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }
}
