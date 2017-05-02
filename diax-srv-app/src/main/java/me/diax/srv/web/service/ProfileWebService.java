package me.diax.srv.web.service;

import me.diax.srv.cache.client.ProfileCache;
import me.diax.srv.database.dao.ProfileDao;
import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ProfileService;
import me.diax.srv.stubs.service.ServiceException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("profile")
public class ProfileWebService implements ProfileService {

    private final ProfileDao profileDao;
    private final ProfileCache cache;

    @Inject
    public ProfileWebService(ProfileDao profileDao, ProfileCache cache) {
        this.profileDao = profileDao;
        this.cache = cache;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Profile getById(@PathParam("id") long id) throws ServiceException {
        try {
            Profile profile = profileDao.get(id);
            if (profile == null) {
                throw new NotFoundException("No profile was found with id: " + id);
            }
            cache.set(profile);
            return profile;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public Profile getByDiscordId(long discordId) throws ServiceException {
        try {
            Profile profile = profileDao.getByDiscordId(discordId);
            if (profile == null) {
                throw new NotFoundException("No profile was found with discordId: " + discordId);
            }
            cache.set(profile);
            return profile;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public void save(Profile profile) throws ServiceException {
        if (profile.isNew()) {
            throw new BadRequestException("Profile needs to have an ID");
        }

        try {
            profileDao.save(profile);
            cache.set(profile);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
