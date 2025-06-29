package com.vicious.persist.annotations;

import com.vicious.persist.shortcuts.NotationFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class String Field or Method as the path to save to.
 * See {@link com.vicious.persist.shortcuts.PersistShortcuts#saveAsFile(Object)} for further usage information.
 * @since 1.0
 * @author Jack Andersen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface PersistentPath {
    /**
     * The {@link com.vicious.persist.shortcuts.NotationFormat} to use for reading and writing.
     */
    NotationFormat value() default NotationFormat.JSON5;

    /**
     * When enabled automatically migrates file formats to the new notation format. This searches for the files by replacing extension.
     * Only will execute if the file is not found.
     */
    boolean autoMigrate() default true;
}
