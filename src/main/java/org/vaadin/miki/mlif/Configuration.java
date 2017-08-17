package org.vaadin.miki.mlif;

/**
 * Interface for db connection configuration.
 */
public interface Configuration {
    String getHost();

    int getPort();

    String getUsername();

    String getPassword();

    String getDatabase();

    String getAuthentication();
}
