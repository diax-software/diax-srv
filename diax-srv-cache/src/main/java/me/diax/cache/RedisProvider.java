package me.diax.cache;

/**
 * @author Sven Olderaan (s.olderaan@i-real.nl)
 */
public interface RedisProvider {

    /**
     * Gets an index by id, and opens a session. This session must be closed!
     *
     * @param index the index
     * @return the session
     * @throws IllegalArgumentException if index is negative
     */
    Redis getSession(int index);

    /**
     * Subscribes a subscriber to a channel
     *
     * @param subscriber subscriber
     * @param channel    the channel
     * @throws SubscriberException if subscriber is already subscribed
     */
    void subscribe(Subscriber subscriber, String channel) throws SubscriberException;

    /**
     * <p>
     * Subscribes a subscriber to a channel matching a wildcard
     * </p>
     * <p>
     * The pattern wildcard symbol is a {@code *}
     *
     * @param subscriber the subscriber
     * @param pattern    the pattern
     * @throws SubscriberException if subscriber is already subscribed
     */
    void pSubscribe(Subscriber subscriber, String pattern) throws SubscriberException;

    /**
     * Unsubscribes a subscriber from all channels
     *
     * @param subscriber the subscriber
     */
    void unSubscribe(Subscriber subscriber);

    /**
     * Publishes a message to a channel
     *
     * @param channel the channel
     * @param data    the data
     */
    void publish(String channel, String data);

}
