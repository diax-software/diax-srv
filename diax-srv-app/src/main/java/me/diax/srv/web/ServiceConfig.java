package me.diax.srv.web;

import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

final class ServiceConfig extends ResourceConfig {

    ServiceConfig(final Injector injector) {
        register(new ContainerLifecycleListener() {
            @Override
            public void onStartup(Container container) {
                ServiceLocator serviceLocator = container.getApplicationHandler().getServiceLocator();
                GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
                GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
                guiceBridge.bridgeGuiceInjector(injector);
            }

            @Override
            public void onReload(Container container) {
            }

            @Override
            public void onShutdown(Container container) {
            }
        });
        packages(getClass().getPackage().getName() + ".service");
    }
}
