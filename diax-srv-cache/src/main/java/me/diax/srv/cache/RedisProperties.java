package me.diax.srv.cache;

import com.knockturnmc.api.util.NamedProperties;
import com.knockturnmc.api.util.Property;
import lombok.Getter;

/**
 * Properties for redis
 */
class RedisProperties extends NamedProperties {

    @Getter
    @Property(value = "redis.host", defaultvalue = "localhost")
    private String host;

    @Getter
    @Property(value = "redis.port", defaultvalue = "6379")
    private int port;

    @Getter
    @Property(value = "redis.password")
    private String password;

    @Getter
    @Property(value = "redis.timeout", defaultvalue = "10000")
    private int timeout;
}
