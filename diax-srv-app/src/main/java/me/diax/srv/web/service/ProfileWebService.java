package me.diax.srv.web.service;

import me.diax.srv.database.dao.ProfileDao;
import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ProfileService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("profile")
public class ProfileWebService implements ProfileService {

    private final ProfileDao profileDao;

    @Inject
    public ProfileWebService(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Profile getById(@PathParam("id") long id) {
        try {
            Profile profile = profileDao.get(id);
            if (profile == null) {
                throw new NotFoundException("No profile was found with id: " + id);
            }
            return profile;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public void save(Profile profile) {
        if (profile.isNew()) {
            throw new BadRequestException("Profile needs to have an ID");
        }

        try {
            profileDao.save(profile);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
