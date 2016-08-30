package org.maxur.ldoc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Link.
 */
@Repeatable(Links.class)
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Link {

    /**
     * Related string.
     *
     * @return the string
     */
    String related();

    /**
     * Label string.
     *
     * @return the string
     */
    String label() default "";
}
