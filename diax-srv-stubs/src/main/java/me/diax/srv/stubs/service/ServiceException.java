package me.diax.srv.stubs.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@NoArgsConstructor
@XmlRootElement(name = "exception")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceException extends Exception implements Serializable {

    private static final long serialVersionUID = -3763714378518146049L;

    private String type = "exception";

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String message;

    public ServiceException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, int code) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }

    public ServiceException(Throwable cause, int code) {
        super(cause);
        this.message = cause.getMessage();
        this.code = code;
    }
}
