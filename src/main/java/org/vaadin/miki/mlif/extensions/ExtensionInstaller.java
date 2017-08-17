package org.vaadin.miki.mlif.extensions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.admin.ExtensionLibrariesManager;
import com.marklogic.client.admin.ExtensionMetadata;
import com.marklogic.client.admin.MethodType;
import com.marklogic.client.admin.ResourceExtensionsManager;
import com.marklogic.client.extensions.ResourceManager;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.StringHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Installer of extensions to MarkLogic database.
 * @author Miki
 */
public class ExtensionInstaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionInstaller.class);

    private final ResourceExtensionsManager manager;
    private final ExtensionLibrariesManager librariesManager;

    public ExtensionInstaller(DatabaseClient dbClient) {
        this.manager = dbClient.newServerConfigManager().newResourceExtensionsManager();
        this.librariesManager = dbClient.newServerConfigManager().newExtensionLibrariesManager();
    }

    /**
     * Returns the set with names of installed exceptions.
     *
     * @return Set with names of extensions.
     */
    public Set<String> list() {
        String json = this.manager.listServices(new StringHandle().withFormat(Format.JSON)).get();
        try {
            JsonNode node = new ObjectMapper().readTree(json);
            JsonNode data = node.get("resources").get("resource");
            if (data != null) {
                Set<String> result = new HashSet<>();
                for (int zmp1 = 0; zmp1 < data.size(); zmp1++)
                    result.add(data.get(zmp1).get("name").asText());
                return result;
            } else
                return Collections.emptySet();

        } catch (IOException e) {
            throw new RuntimeException("cannot read json from server!", e);
        }
    }

    /**
     * Installs extensions.
     *
     * @param extensions
     *            Extension. Must be annotated with {@link MlifExtension}.
     * @throws IllegalArgumentException when the extension does not have an annotation MlifExtension.
     */
    public void install(ResourceManager... extensions) {
        for (ResourceManager extension : extensions) {
            // the annotation must be there
            MlifExtension meta = extension.getClass().getAnnotation(MlifExtension.class);

            if(meta == null)
                throw new IllegalArgumentException("Object of type "+extension.getClass().getName()+" does not have an annotation MlifExtension!");

            LOGGER.info("attempting to install MarkLogic REST Extension from object of type {}", extension.getClass().getName());

            ExtensionMetadata extensionMetadata = this.getMetaData(meta);

            Map<MethodType, String[]> parameterMap = new HashMap<>();
            parameterMap.put(MethodType.GET, meta.getParams());
            parameterMap.put(MethodType.POST, meta.postParams());
            parameterMap.put(MethodType.DELETE, meta.deleteParams());
            parameterMap.put(MethodType.PUT, meta.putParams());

            for (MethodType type : meta.methods()) {
                LOGGER.info("...installing for method "+type.toString());
                InputStream code = this.getClass().getClassLoader().getResourceAsStream(meta.file());
                InputStreamHandle handle = new InputStreamHandle(code);
                ResourceExtensionsManager.MethodParameters parameters = new ResourceExtensionsManager.MethodParameters(
                        type);
                // parameters are declared in pairs, name and type
                for (int zmp1 = 0; zmp1 < parameterMap.get(type).length; zmp1 += 2)
                    parameters.add(parameterMap.get(type)[zmp1], parameterMap.get(type)[zmp1 + 1]);
                this.manager.writeServices(meta.endPoint(), handle, extensionMetadata, parameters);
            }
            LOGGER.info("...successfully installed extension point {}", meta.endPoint());
        }
    }

    /**
     * Uninstalls extension(s).
     *
     * @param extensions
     *            Extensions to uninstall.
     */
    public void uninstall(ResourceManager... extensions) {
        for (ResourceManager ext : extensions) {
            LOGGER.info("uninstalling extension {}", ext.getName());
            this.manager.deleteServices(ext.getClass().getAnnotation(MlifExtension.class).endPoint());
        }
    }

    /**
     * Uninstalls specified extensions.
     *
     * @param extensionNames
     *            Names of extensions.
     */
    public void uninstall(String... extensionNames) {
        for (String name : extensionNames) {
            LOGGER.info("uninstalling extension {}", name);
            this.manager.deleteServices(name);
        }
    }

    /**
     * Uninstalls all installed extensions.
     */
    public final void uninstallAll() {
        LOGGER.info("uninstalling all extensions");
        this.uninstall(this.list().toArray(new String[this.list().size()]));
    }

    /**
     * Converts annotation to metadata.
     * @param annotation Annotation.
     * @return Metadata.
     */
    protected ExtensionMetadata getMetaData(MlifExtension annotation) {
        ExtensionMetadata result = new ExtensionMetadata();
        result.setTitle(annotation.title());
        result.setProvider(annotation.provider());
        result.setVersion(annotation.version());
        result.setScriptLanguage(ExtensionMetadata.ScriptLanguage.XQUERY);
        return result;
    }

    /**
     * Determines the scripting language of the extension.
     * @param annotation Annotation.
     * @return XQuery or JavaScript, depending on either the language definition or file extension (by default).
     * @throws IllegalArgumentException when neither extension nor language starts with 'x' or 'j'.
     */
    protected ExtensionMetadata.ScriptLanguage determineScriptLanguage(MlifExtension annotation) {
        char lang = annotation.language().isEmpty() ?
                // check the first character after the last dot in file path
                annotation.file().charAt(annotation.file().lastIndexOf('.')+1) :
                annotation.language().charAt(0);
        switch (lang) {
            case 'x':
            case 'X':
                return ExtensionMetadata.ScriptLanguage.XQUERY;
            case 'j':
            case 'J':
                return ExtensionMetadata.ScriptLanguage.JAVASCRIPT;
            default:
                throw new IllegalArgumentException("either MlifExtension.language or extension of file in MlifExtension.file must start with 'x' or 'j'!");
        }
    }

}
