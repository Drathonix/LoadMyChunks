package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a savable element to have multiple reference names. If an altName is already used by another savable element or its name is {@link com.vicious.persist.mappify.registry.Reserved} it will be ignored.
 * When using a {@link com.vicious.persist.mappify.Mappifier} to unmap, this will allow the savable element to be referenced by its {@link Save} name or any of the provided alt names.
 * AltNames will only be referenced in the current map meaning parent and child maps are inaccessible. For more complicated transformations see {@link ReplaceKeys}
 * This can be used to update old files automatically.
 * @author Jack Andersen
 * @since 1.2.4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface AltName {
    String[] value();
}
