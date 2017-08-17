package org.vaadin.miki;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.admin.MethodType;
import com.marklogic.client.extensions.ResourceManager;
import com.marklogic.client.extensions.ResourceServices;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.util.RequestParameters;
import org.vaadin.miki.mlif.extensions.MlifExtension;

@MlifExtension(
        file="reverse-string.xqy",
        title="Reverse String",
        endPoint = "reverse-string",
        methods={MethodType.GET},
        getParams = {"string", "xs:string"},
        version = "1.0",
        provider = "Miki"
)
public class ReverseStringExtension extends ResourceManager {

    public ReverseStringExtension(DatabaseClient client) {
        super();
        client.init("reverse-string", this);
    }

    /**
     * Reverses the string.
     * @param input Input string. May not be {@code null}.
     * @return Reversed string.
     */
    public String reverse(String input) {
        RequestParameters parameters = new RequestParameters();
        parameters.add("string", input);

        ResourceServices services = this.getServices();
        String result = services.get(parameters, new StringHandle().withFormat(Format.TEXT)).get();

        return result;
    }

}
