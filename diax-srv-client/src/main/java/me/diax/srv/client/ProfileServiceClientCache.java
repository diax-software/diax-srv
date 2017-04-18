package me.diax.srv.client;

import me.diax.srv.cache.client.ProfileCache;
import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
class ProfileServiceClientCache extends ProfileServiceClient {

    private final ProfileCache cache;

    @Inject
    ProfileServiceClientCache(@Named("endpoint") String endpoint, ProfileCache cache) {
        super(endpoint);
        this.cache = cache;
    }

    @Override
    public Profile getById(long id) throws ServiceException {
        Profile cached = cache.get(id);
        return cached == null ? super.getById(id) : cached;
    }
}
