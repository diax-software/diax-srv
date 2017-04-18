package me.diax.cache;

import java.util.Map;

/**
 * Redis interface.
 */
public interface Redis extends AutoCloseable {

    /**
     * Gets the current index
     *
     * @return index
     */
    int getIndex();

    /**
     * Gets an object from the redis
     *
     * @param key the key of the object
     * @return the object
     */
    String get(String key);

    /**
     * Sets a key in the redis to a specified value
     *
     * @param key   the key
     * @param value the value
     */
    void set(String key, String value);

    /**
     * Sets the redis index to operate from
     *
     * @param index the numeric index
     * @throws IllegalArgumentException if index is negative
     */
    void select(int index);

    /**
     * Set a hash field with multiple values
     *
     * @param key  the key which identifies the hash
     * @param data map with multiple keys and values representing items from an object
     */
    void hmset(String key, Map<String, String> data);

    /**
     * Set a hash field with multiple values
     *
     * @param key the key which identifies the hash
     * @return map with keys and values representing items from an object
     */
    Map<String, String> hgetall(String key);

    /**
     * Sets the expire date of a key in unix timestamp format
     *
     * @param key        the key which identifies the hash
     * @param expireDate unix timestamp expire date
     */
    void expireAt(String key, long expireDate);

}