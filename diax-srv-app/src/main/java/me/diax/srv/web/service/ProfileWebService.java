package me.diax.srv.web.service;

import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ProfileService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Random;

@Path("profile")
public class ProfileWebService implements ProfileService {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Profile getById(@PathParam("id") long id) {
        Profile profile = new Profile();
        profile.setId(id);
        return profile;
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Override
    public long save(Profile profile) {
        if (profile.isNew()) {
            profile.setId(Math.abs(new Random().nextLong()));
        }

        return profile.getId();
    }
}
