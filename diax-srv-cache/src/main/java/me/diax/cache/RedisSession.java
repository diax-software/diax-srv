package me.diax.cache;

import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Single connection implementation for Redis. It provides get and set methods for most common redis operations
 */
class RedisSession implements Redis {

    private final Jedis connection;
    private int index;

    /**
     * Creates a new session
     *
     * @param connection the connection to wrap
     * @param index      the default index
     * @throws IllegalArgumentException if index is negative
     */
    RedisSession(Jedis connection, int index) {
        this.connection = connection;
        select(index);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String get(String key) {
        return connection.get(key);
    }

    @Override
    public void set(String key, String value) {
        connection.set(key, value);
    }

    @Override
    public void select(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
        connection.select(index);
        this.index = index;
    }

    @Override
    public void hmset(String key, Map<String, String> data) {
        connection.hmset(key, data);
    }

    @Override
    public Map<String, String> hgetall(String key) {
        return connection.hgetAll(key);
    }

    @Override
    public void expireAt(String key, long expireDate) {
        connection.expireAt(key, expireDate);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
