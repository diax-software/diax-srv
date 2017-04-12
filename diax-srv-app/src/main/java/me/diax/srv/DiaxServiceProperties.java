package me.diax.srv;

import com.knockturnmc.api.util.NamedProperties;
import com.knockturnmc.api.util.Property;
import lombok.Getter;

public class DiaxServiceProperties extends NamedProperties {

    @Getter
    @Property(value = "service.port", defaultvalue = "8080")
    private int port;
}
