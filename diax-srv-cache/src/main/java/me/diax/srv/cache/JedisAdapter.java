package me.diax.srv.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * Adapter class to adapt the JedisPubSub to the API's Subscriber
 */
class JedisAdapter extends JedisPubSub implements Subscriber {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Subscriber subscriber;

    /**
     * Creates a new Adapter instance using a subscriber
     *
     * @param subscriber the subscriber
     * @throws IllegalArgumentException if subscriber is null
     */
    JedisAdapter(Subscriber subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("subscriber cannot be null");
        }
        this.subscriber = subscriber;
    }

    /**
     * Called when a message is passed to the subscriber
     *
     * @param channel the channel
     * @param message the message
     */
    @Override
    public void onMessage(String channel, String message) {
        try {
            subscriber.onMessage(channel, message);
        } catch (Exception e) {
            logger.error("Failed to process message", e);
        }
    }

    /**
     * Called when a message is passed to the subscriber
     *
     * @param pattern the pattern
     * @param channel the channel
     * @param message the message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        try {
            subscriber.onMessage(channel, message);
        } catch (Exception e) {
            logger.error("Failed to process message", e);
        }
    }
}