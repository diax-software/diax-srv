package me.diax.srv.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Pooled implementation for Redis
 */
@Singleton
class RedisProviderImpl implements RedisProvider {

    private final Map<Subscriber, JedisAdapter> subscribers;
    private final JedisPool pool;

    @Inject
    RedisProviderImpl(RedisProperties properties) {
        subscribers = new HashMap<>();
        pool = new JedisPool(
                new JedisPoolConfig(),
                properties.getHost(),
                properties.getPort(),
                properties.getTimeout(),
                properties.getPassword()
        );
    }

    @Override
    public Redis getSession(int index) {
        Jedis connection = pool.getResource();
        try {
            return new RedisSession(connection, index);
        } catch (IllegalArgumentException e) {
            connection.close();
            throw e;
        }
    }

    @Override
    public void subscribe(Subscriber subscriber, String channel) throws SubscriberException {
        checkStatus(subscriber);
        JedisAdapter adapter = new JedisAdapter(subscriber);
        subscribers.put(subscriber, adapter);
        doVoidCall(jedis -> jedis.subscribe(adapter, channel));
    }

    @Override
    public void pSubscribe(Subscriber subscriber, String pattern) throws SubscriberException {
        checkStatus(subscriber);
        JedisAdapter adapter = new JedisAdapter(subscriber);
        subscribers.put(subscriber, adapter);
        doVoidCall(jedis -> jedis.psubscribe(adapter, pattern));
    }

    @Override
    public void unSubscribe(Subscriber subscriber) {
        JedisAdapter adapter = subscribers.get(subscriber);
        if (adapter != null && adapter.isSubscribed()) {
            adapter.unsubscribe();
            adapter.punsubscribe();
        }
        subscribers.remove(subscriber);
    }

    @Override
    public void publish(String channel, String data) {
        doVoidCall(jedis -> jedis.publish(channel, data));
    }

    /**
     * Checks if the specified subscriber is already registered with this {@code RedisProviderImpl}
     *
     * @param subscriber the subscriber
     * @throws SubscriberException if the subscriber has already been subscribed with this provider
     */
    private void checkStatus(Subscriber subscriber) throws SubscriberException {
        if (subscribers.get(subscriber) != null)
            throw new SubscriberException("Subscriber already subscribed");
    }

    /**
     * Opens a jedis connection and performs a command
     *
     * @param call the command to execute
     */
    private void doVoidCall(Consumer<Jedis> call) {
        try (Jedis jedis = pool.getResource()) {
            call.accept(jedis);
        }
    }

    /**
     * Opens a jedis connection and performs a command that returns something
     *
     * @param call the command to execute
     * @param <R>  the return type
     * @return the returned database
     */
    private <R> R doCall(Function<Jedis, R> call) {
        try (Jedis jedis = pool.getResource()) {
            return call.apply(jedis);
        }
    }
}
