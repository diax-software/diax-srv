package me.diax.srv.cache;

/**
 * Thrown when a subscriber could not be subscribed
 */
public class SubscriberException extends Exception {

    /**
     * Creates a new subscriber exception
     *
     * @param reason the reason for this exception
     */
    public SubscriberException(String reason) {
        super(reason);
    }

}
