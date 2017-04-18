package me.diax.srv.client;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import me.diax.srv.stubs.service.ProfileService;

public class ServiceModule extends AbstractModule {

    private final String endpoint;
    private final boolean caching;

    public ServiceModule(String endpoint, boolean caching) {
        this.endpoint = endpoint;
        this.caching = caching;
    }

    public ServiceModule(String endpoint) {
        this(endpoint, false);
    }

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("endpoint")).toInstance(endpoint);

        if (caching) {
            bind(ProfileService.class).to(ProfileServiceClientCache.class);
        } else {
            bind(ProfileService.class).to(ProfileServiceClient.class);
        }
    }
}
