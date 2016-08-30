package org.maxur.ldoc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Binder.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>03.08.2016</pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Concept {

    /**
     * Name string.
     *
     * @return the string
     */
    String name();

    /**
     * Description string.
     *
     * @return the string
     */
    String description();
}
