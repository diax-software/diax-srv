package me.diax.cache;

import com.google.inject.AbstractModule;

import java.io.File;

import static com.knockturnmc.api.util.ConfigurationUtils.loadConfiguration;

/**
 * Represents a module to communicate with a redis server
 */
public class RedisModule extends AbstractModule {

    private final RedisProperties properties;

    /**
     * Creates a new RedisModule with the specified configuration
     *
     * @param file      the config file name
     * @param directory the directory of the config file
     */
    public RedisModule(String file, File directory) {
        ClassLoader cl = getClass().getClassLoader();
        properties = loadConfiguration(cl, file, directory, RedisProperties.class);
    }

    @Override
    protected void configure() {
        bind(RedisProperties.class).toInstance(properties);
        bind(RedisProvider.class).to(RedisProviderImpl.class);
    }
}
