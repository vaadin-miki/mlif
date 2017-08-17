package org.vaadin.miki;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.Transaction;
import com.marklogic.client.document.DocumentWriteSet;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JacksonDatabindHandle;
import com.marklogic.client.pojo.PojoRepository;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.miki.mlif.ClientFactory;
import org.vaadin.miki.mlif.Configuration;
import org.vaadin.miki.mlif.MlifClientFactory;
import org.vaadin.miki.mlif.configuration.PropertiesConfiguration;
import org.vaadin.miki.mlif.extensions.ExtensionInstaller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests.
 * A working connection to the database is required.
 */
public class MlifIntegrationTest {

    private ClientFactory factory;
    private Configuration configuration;
    private Testable testable;

    @Before
    public void setup() throws IOException {
        Properties properties = new Properties();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        properties.load(stream);
        stream.close();
        this.configuration = new PropertiesConfiguration(properties);
        this.factory = new MlifClientFactory();

        this.testable = new Testable("something something", 42);
    }

    @Test
    public void checkConnection() {
        DatabaseClient client = factory.constructClient(configuration);
        assertNotNull(client);

        Transaction transaction = client.openTransaction();
        assertNotNull(transaction.getTransactionId());
        transaction.rollback();

        client.release();
    }

    @Test
    public void checkDataUploadAndDelete() {
        DatabaseClient client = factory.constructClient(configuration);
        assertNotNull(client);

        final Transaction transaction = client.openTransaction();

        PojoRepository<Testable, String> repository = client.newPojoRepository(Testable.class, String.class);

        repository.write(testable, transaction, "data://test/collection");

        transaction.commit();

        assertEquals(1, repository.count());

        final Transaction transaction2 = client.openTransaction();
        repository.deleteAll(transaction2);
        transaction2.commit();
        assertEquals(0, repository.count());

        client.release();
    }

    @Test
    public void checkDocumentsUploadAndDelete() {
        // preconfigure
        DatabaseClientFactory.getHandleRegistry().register(
                JacksonDatabindHandle.newFactory(Testable.class)
        );
        DatabaseClient client = factory.constructClient(configuration);
        assertNotNull(client);

        Transaction transaction = client.openTransaction();

        JSONDocumentManager manager = client.newJSONDocumentManager();
        DocumentWriteSet set = manager.newWriteSet();
        List<String> documentUris = new ArrayList<>();

        String testUri = "data://test/"+testable.getNumber();
        DocumentMetadataHandle meta = new DocumentMetadataHandle();
        meta.getCollections().add("data://test/collection");
        documentUris.add(testUri);
        set.addAs(testUri, testable);

        System.out.println("writing data - "+set.size());
        manager.write(set, transaction);
        transaction.commit();

        manager.delete(documentUris.toArray(new String[0]));

        client.release();

    }

    @Test
    public void testExtension() {
        // make sure that the user you log in as has the permissions: rest-extension-user and rest-admin
        DatabaseClient client = factory.constructClient(configuration);
        assertNotNull(client);

        ReverseStringExtension extension = new ReverseStringExtension(client);

        ExtensionInstaller installer = new ExtensionInstaller(client);
        installer.install(extension);

        Set<String> installed = installer.list();
        assertEquals(1, installed.size());

        String input = "hello, world";
        String output = extension.reverse(input);

        // it is 2017 and there is no string.reverse()... wtf, java?!
        String expected = new StringBuilder(input).reverse().toString();

        assertEquals(expected, output);

        installer.uninstall(extension);

        installed = installer.list();
        assertTrue(installed.isEmpty());
    }

}
