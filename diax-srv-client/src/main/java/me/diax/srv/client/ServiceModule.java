package me.diax.srv.client;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import me.diax.srv.stubs.service.ProfileService;

public class ServiceModule extends AbstractModule {

    private final String endpoint;

    public ServiceModule(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("endpoint")).toInstance(endpoint);
        bind(ProfileService.class).to(ProfileServiceClient.class);
    }
}
