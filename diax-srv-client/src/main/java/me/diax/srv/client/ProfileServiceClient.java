package me.diax.srv.client;

import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ProfileService;
import me.diax.srv.stubs.service.ServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
class ProfileServiceClient extends DiaxServiceClient implements ProfileService {

    @Inject
    ProfileServiceClient(@Named("endpoint") String endpoint) {
        super(endpoint);
    }

    @Override
    public Profile getById(long id) throws ServiceException {
        String response = doGet(endpoint + "profile/" + id, TYPE);
        return unmarshall(response, Profile.class);
    }

    @Override
    public void save(Profile profile) throws ServiceException {
        String response = doPut(endpoint + "profile/", marshall(profile), TYPE);
        if (!Objects.equals("", response)) {
            throw unmarshall(doPut(endpoint + "profile/", marshall(profile), TYPE), ServiceException.class);
        }
    }
}
