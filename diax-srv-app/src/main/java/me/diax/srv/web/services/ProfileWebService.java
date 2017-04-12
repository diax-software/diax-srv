package me.diax.srv.web.services;

import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ProfileService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("profile")
@Produces(MediaType.APPLICATION_JSON)
public class ProfileWebService implements ProfileService {

    @GET
    @Path("{id}")
    @Override
    public Profile getById(@PathParam("id") long id) {
        Profile profile = new Profile();
        profile.setId(id);
        return profile;
    }

    @PUT
    @Override
    public long save(Profile profile) {
        return profile.getId();
    }
}
