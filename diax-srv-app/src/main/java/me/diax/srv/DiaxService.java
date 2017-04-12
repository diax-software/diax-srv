package me.diax.srv;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import me.diax.srv.web.StandaloneServer;

import static com.knockturnmc.api.util.ConfigurationUtils.loadConfiguration;

public class DiaxService implements Module {

    private final Injector injector;
    private final DiaxServiceProperties properties;

    public static void main(String[] args) throws Exception {
        new DiaxService().init();
    }

    private DiaxService() {
        ClassLoader cl = getClass().getClassLoader();
        properties = loadConfiguration(cl, "service.properties", DiaxServiceProperties.class);
        injector = Guice.createInjector(this);
    }

    private void init() throws Exception {
        StandaloneServer server = new StandaloneServer(properties, injector);
        server.start();
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(DiaxServiceProperties.class).toInstance(properties);
    }
}