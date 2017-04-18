package me.diax.srv;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.knockturnmc.api.util.sql.SqlDatasource;
import me.diax.srv.cache.RedisModule;
import me.diax.srv.cache.client.CacheClientModule;
import me.diax.srv.database.DatabaseModule;
import me.diax.srv.web.StandaloneServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.knockturnmc.api.util.ConfigurationUtils.getDataFolder;
import static com.knockturnmc.api.util.ConfigurationUtils.loadConfiguration;

public class DiaxService implements Module {

    private static final Logger logger = LoggerFactory.getLogger(DiaxService.class);
    private final Injector injector;
    private final DiaxServiceProperties properties;
    private final File configurationDirectory;

    private DiaxService() {
        configurationDirectory = new File(getDataFolder(), "conf");
        if (!configurationDirectory.exists() && !configurationDirectory.mkdir()) {
            throw new RuntimeException("Unable to create configuration directory");
        }
        ClassLoader cl = getClass().getClassLoader();
        properties = loadConfiguration(cl, "service.properties", configurationDirectory, DiaxServiceProperties.class);
        injector = Guice.createInjector(this);
    }

    public static void main(String[] args) throws Exception {
        new DiaxService().init();
    }

    private void init() throws Exception {
        try {
            //Initializes Database
            injector.getInstance(SqlDatasource.class);

            StandaloneServer server = new StandaloneServer(properties, injector);
            server.start();
        } catch (Exception e) {
            logger.error("Failed to initialize application", e);
        }
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(DiaxServiceProperties.class).toInstance(properties);
        binder.install(new DatabaseModule("database.properties", configurationDirectory));
        binder.install(new RedisModule("redis.properties", configurationDirectory));
        binder.install(new CacheClientModule());
    }
}