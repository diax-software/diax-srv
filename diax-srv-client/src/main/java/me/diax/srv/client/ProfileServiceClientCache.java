package me.diax.srv.client;

import me.diax.cache.DiaxCacheIndex;
import me.diax.cache.Redis;
import me.diax.cache.RedisProvider;
import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
class ProfileServiceClientCache extends ProfileServiceClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RedisProvider provider;

    @Inject
    ProfileServiceClientCache(@Named("endpoint") String endpoint, RedisProvider provider) {
        super(endpoint);
        this.provider = provider;
    }

    @Override
    public Profile getById(long id) throws ServiceException {
        Profile profile = get(id);
        return profile == null ? super.getById(id) : profile;
    }

    private Profile get(long id) throws ServiceException {
        try (Redis redis = provider.getSession(DiaxCacheIndex.PROFILE)) {
            String data = redis.get(Long.toString(id));
            return data == null ? null : unmarshall(data, Profile.class);
        } catch (Exception e) {
            logger.error("Failed to query redis", e);
            throw new ServiceException(e, 0);
        }
    }
}
