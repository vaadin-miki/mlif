package org.vaadin.miki.mlif;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;

/**
 * Makes new clients based on configuration.
 */
public class MlifClientFactory implements ClientFactory {

    /**
     * Makes new security context based on current configuration.
     * @return SecurityContext.
     */
    private DatabaseClientFactory.SecurityContext constructSecurityContext(Configuration cfg) {
        if("digest".equals(cfg.getAuthentication()))
            return new DatabaseClientFactory.DigestAuthContext(cfg.getUsername(), cfg.getPassword());
        else return new DatabaseClientFactory.BasicAuthContext(cfg.getUsername(), cfg.getPassword());
    }

    @Override
    public DatabaseClient constructClient(Configuration cfg) {
        return DatabaseClientFactory.newClient(
                cfg.getHost(), cfg.getPort(), cfg.getDatabase(), this.constructSecurityContext(cfg)
        );
    }

}
