package me.diax.srv.stubs.service;

import me.diax.srv.stubs.model.Profile;

/**
 * Represents the service for profile information
 */
public interface ProfileService {

    /**
     * Gets a profile by its internal id
     *
     * @param id the id
     * @return the profile
     * @throws ServiceException if something failed
     */
    Profile getById(long id) throws ServiceException;

    /**
     * Saves a profile
     *
     * @param profile the profile
     * @throws ServiceException if somethign failed
     */
    void save(Profile profile) throws ServiceException;

    /**
     * Gets a profile by its discord id
     *
     * @param discordId the discord id
     * @return the profile
     * @throws ServiceException if something went wrong
     */
    Profile getByDiscordId(long discordId) throws ServiceException;
}
