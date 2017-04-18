package me.diax.srv.client;

import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ProfileService;
import me.diax.srv.stubs.service.ServiceException;

import javax.inject.Singleton;
import java.util.Objects;

@Singleton
class ProfileServiceClient extends DiaxServiceClient implements ProfileService {

    @Override
    public Profile getById(long id) throws ServiceException {
        String response = doGet(endpoint + "profile/" + id, context.getMediaType());
        return context.deserialize(response, Profile.class);
    }

    @Override
    public void save(Profile profile) throws ServiceException {
        String response = doPut(endpoint + "profile/", context.serialize(profile), context.getMediaType());
        if (!Objects.equals("", response)) {
            throw context.deserialize(doPut(endpoint + "profile/", context.serialize(profile), context.getMediaType()),
                    ServiceException.class);
        }
    }
}
