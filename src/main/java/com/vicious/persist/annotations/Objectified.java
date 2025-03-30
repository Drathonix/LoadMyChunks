package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Savable fields marked with this annotation will store Enums and Classes in Object format rather in Reference format. Whereby "reference" format involves storing a reference to the enum instance and "object" format involves storing the enum reference and its object data.
 *
 * @author Jack Andersen
 * @since 1.3.6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
public @interface Objectified {}
