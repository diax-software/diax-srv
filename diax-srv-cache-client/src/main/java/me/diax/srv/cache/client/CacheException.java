package me.diax.srv.cache.client;

import me.diax.srv.stubs.service.ServiceException;

/**
 * Represents an exception thrown by the cache
 */
class CacheException extends ServiceException {

    /**
     * Creates a new exception
     *
     * @param cause the cause
     */
    CacheException(Throwable cause) {
        super(cause, 0);
    }
}
