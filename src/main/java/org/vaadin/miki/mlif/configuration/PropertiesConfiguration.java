package org.vaadin.miki.mlif.configuration;

import java.util.Properties;

/**
 * Database configuration based on properties.
 */
public class PropertiesConfiguration extends SimpleConfiguration {

    public static final String HOST = "mlif.host";
    public static final String PORT = "mlif.port";
    public static final String USER = "mlif.user";
    public static final String AUTH = "mlif.auth";
    public static final String PASSWORD = "mlif.password";
    public static final String DATABASE = "mlif.database";

    public static final String DEFAULT_HOST = "localhost";
    public static final String DEFAULT_PORT = "8000";
    public static final String DEFAULT_USER = "user";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_DATABASE = "Documents";
    public static final String DEFAULT_AUTH = "digest";

    /**
     * Creates new instance of configuration from given properties.
     * @param props Properties.
     */
    public PropertiesConfiguration(Properties props) {
        super();
        this.load(props);
    }

    /**
     * Loads the configuration from given properties. Overwrites existing properties.
     *
     * @param properties Properties. Reasonable defaults are used in case of missing properties ({@code localhost:8000/Documents} and {@code user/password}).
     */
    public void load(Properties properties) {
        this.setHost(properties.getProperty(HOST, DEFAULT_HOST));
        this.setPort(Integer.valueOf(properties.getProperty(PORT, DEFAULT_PORT)));
        this.setPassword(properties.getProperty(PASSWORD, DEFAULT_PASSWORD));
        this.setUsername(properties.getProperty(USER, DEFAULT_USER));
        this.setDatabase(properties.getProperty(DATABASE, DEFAULT_DATABASE));
        this.setAuthentication(properties.getProperty(AUTH, DEFAULT_AUTH));
    }

}
