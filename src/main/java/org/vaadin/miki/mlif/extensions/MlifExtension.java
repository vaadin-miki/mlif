package org.vaadin.miki.mlif.extensions;

import com.marklogic.client.admin.MethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that describes MarkLogic REST extension so that it can be easily installed.
 * @author miki
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MlifExtension {

    /** File name **/
    String file();

    /** Title of the extension **/
    String title();

    /** The end point **/
    String endPoint();

    /**
     * Supported method types.
     */
    MethodType[] methods();

    /**
     * Name-type pairs for parameters (GET).
     */
    String[] getParams() default {};

    /**
     * Name-type pairs for parameters (POST).
     */
    String[] postParams() default {};

    /**
     * Name-type pairs for parameters (PUT).
     */
    String[] putParams() default {};

    /**
     * Name-type pairs for parameters (DELETE).
     */
    String[] deleteParams() default {};

    String provider() default "(none)";

    String version() default "0.1";

    /**
     * No language means it will be deducted from file extension (.xq and .xqy is XQUERY, .js is JAVASCRIPT, everything else is an error).
     * Allowed values are - anything that starts with X will be XQUERY, anything that starts with J will be JAVASCRIPT.
     * Everything else is an error.
     */
    String language() default "";

}
