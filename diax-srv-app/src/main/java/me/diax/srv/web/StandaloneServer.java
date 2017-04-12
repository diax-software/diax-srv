package me.diax.srv.web;

import com.google.inject.Injector;
import me.diax.srv.DiaxServiceProperties;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class StandaloneServer {

    private final Server server;

    public StandaloneServer(DiaxServiceProperties properties, Injector injector) {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(properties.getPort()).build();
        ResourceConfig config = new ServiceConfig(injector);
        server = JettyHttpContainerFactory.createServer(baseUri, config);
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
