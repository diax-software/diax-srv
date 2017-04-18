package me.diax.srv.cache.client;

import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ServiceException;

/**
 * Represents the Profile store in the cache
 */
public interface ProfileCache {

    /**
     * Gets a profile from the cache. Returns {@code null} when profile does not exist in cache
     *
     * @param id the id
     * @return the profile or {@code null}
     * @throws ServiceException if querying failed
     */
    Profile get(long id) throws ServiceException;

    /**
     * Sets a profile in the cache
     *
     * @param profile the profile
     * @throws ServiceException         if saving failed
     * @throws IllegalArgumentException if the profile is new
     */
    void set(Profile profile) throws ServiceException;
}
