package me.diax.srv.database;

import com.google.inject.AbstractModule;
import com.knockturnmc.api.util.sql.SqlDatasource;
import me.diax.srv.database.connector.ConnectorModule;

import java.io.File;

import static com.knockturnmc.api.util.ConfigurationUtils.loadConfiguration;

public class DatabaseModule extends AbstractModule {

    private final DatabaseProperties properties;

    public DatabaseModule(String file, File directory) {
        ClassLoader cl = getClass().getClassLoader();
        properties = loadConfiguration(cl, file, directory, DatabaseProperties.class);
    }

    @Override
    protected void configure() {
        bind(DatabaseProperties.class).toInstance(properties);
        bind(SqlDatasource.class).to(SqlConnectionPool.class);

        install(new ConnectorModule());
    }
}
