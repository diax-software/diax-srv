package me.diax.srv.client;

import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ProfileService;
import me.diax.srv.stubs.service.ServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
class ProfileServiceClient extends DiaxServiceClient implements ProfileService {

    @Inject
    ProfileServiceClient(@Named("endpoint") String endpoint) {
        super(endpoint);
    }

    @Override
    public Profile getById(long id) throws ServiceException {
        try {
            String response = client.doGet(endpoint + "profile/" + id, TYPE);
            return unmarshall(response, Profile.class);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(Profile profile) throws ServiceException {
        try {
            client.doPut(endpoint + "profile/", marshall(profile), TYPE);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
