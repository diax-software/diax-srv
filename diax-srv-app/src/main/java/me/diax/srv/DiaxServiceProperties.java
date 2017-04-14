package me.diax.srv;

import com.knockturnmc.api.util.NamedProperties;
import com.knockturnmc.api.util.Property;
import lombok.Getter;

import java.io.Serializable;

public class DiaxServiceProperties extends NamedProperties implements Serializable {

    private static final long serialVersionUID = -1915389966719628551L;

    @Getter
    @Property(value = "service.port", defaultvalue = "8080")
    private int port;
}
