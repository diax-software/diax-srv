package me.diax.srv.cache.client;

import com.google.inject.AbstractModule;

/**
 * Represents a module that can be used for client caching
 */
public class CacheClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProfileCache.class).to(ProfileCacheClient.class);
    }
}
