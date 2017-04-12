package me.diax.srv.stubs.model;

/**
 * Represents an identifiable object
 * @param <T>
 */
public interface Identifiable<T> {

    T getId();

    void setId(T id);

}
