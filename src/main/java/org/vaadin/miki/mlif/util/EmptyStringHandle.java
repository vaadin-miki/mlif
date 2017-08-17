package org.vaadin.miki.mlif.util;

import com.marklogic.client.io.OutputStreamSender;
import com.marklogic.client.io.StringHandle;

/**
 * A {@link StringHandle} capable of handling an empty string as a response from the db.
 * @author miki
 */
public class EmptyStringHandle extends StringHandle {

    @Override
    protected OutputStreamSender sendContent() {
        try {
            return super.sendContent();
        }
        catch(IllegalStateException ise) {
            this.set("");
            return super.sendContent();
        }
    }

}
