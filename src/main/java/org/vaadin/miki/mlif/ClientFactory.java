package org.vaadin.miki.mlif;

import com.marklogic.client.DatabaseClient;

/**
 * Interface for parameterless client factories.
 */
@FunctionalInterface
public interface ClientFactory {

    /**
     * Builds new client.
     * @param configuration Configuration.
     * @return Client.
     */
    DatabaseClient constructClient(Configuration configuration);
}
