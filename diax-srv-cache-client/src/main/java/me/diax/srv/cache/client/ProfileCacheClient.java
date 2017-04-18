package me.diax.srv.cache.client;

import me.diax.srv.cache.Redis;
import me.diax.srv.cache.RedisProvider;
import me.diax.srv.common.DiaxJsonContext;
import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ServiceException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ProfileCacheClient implements ProfileCache {

    private final RedisProvider provider;
    private final DiaxJsonContext context;

    @Inject
    ProfileCacheClient(RedisProvider provider) {
        this.provider = provider;
        try {
            this.context = DiaxJsonContext.getInstance();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Profile get(long id) throws ServiceException {
        try (Redis redis = provider.getSession(DiaxCacheIndex.PROFILE.getIndex())) {
            String data = redis.get(Long.toString(id));
            return data == null ? null : context.deserialize(data, Profile.class);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void set(Profile profile) throws ServiceException {
        if (profile == null || profile.isNew()) {
            throw new IllegalArgumentException("Cannot store profile without an ID");
        }

        try (Redis redis = provider.getSession(DiaxCacheIndex.PROFILE.getIndex())) {
            String data = context.serialize(profile);
            redis.set(profile.getId().toString(), data);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }
}
