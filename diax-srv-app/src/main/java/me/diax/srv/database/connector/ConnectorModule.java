package me.diax.srv.database.connector;

import com.google.inject.AbstractModule;
import me.diax.srv.database.dao.ProfileDao;

public class ConnectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProfileDao.class).to(ProfileConnector.class);
    }
}
