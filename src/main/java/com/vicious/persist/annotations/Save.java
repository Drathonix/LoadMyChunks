package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Field or Method as a savable element.
 * Methods annotated will act as a getter method and must return a value.
 * Fields annotated will act as a getter and/or setter unless overridden by a method.
 *
 * @since 1.0
 * @author Jack Andersen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD, ElementType.PARAMETER})
public @interface Save {
    /**
     * The name of the savable element. If left empty this defaults to the Method or Field name.
     * @return an arbitrary name String.
     */
    String value() default "";

    /**
     * The description of the savable element. This will be written as a comment when not empty.
     * @return the description String.
     */
    String description() default "";

    /**
     * A special sub-annotation that marks a Method as a setter method for a savable element.
     * The Setter annotation must use the same effective name as the getter.
     * Any method annotated with this will be called over the Field.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Setter {
        /**
         * @return The savable element's effective name.
         */
        String value();
    }
    /**
     * A special sub-annotation that marks a Constructor as an initializer.
     * The initializer should contain all {@link Save} fields as parameters.
     * @since 1.4.2
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.CONSTRUCTOR)
    @interface Constructor{
        /**
         * @return An array containing all the names of the {@link Save} fields each parameter corresponds to.
         */
        String[] value();
    }
}
