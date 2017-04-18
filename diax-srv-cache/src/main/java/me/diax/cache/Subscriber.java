package me.diax.cache;

/**
 * Represents a Redis PubSub subscriber
 */
public interface Subscriber {

    /**
     * On message
     *
     * @param channel the channel, or channel pattern when using psubscribe
     * @param data    the data
     */
    void onMessage(String channel, String data);
}