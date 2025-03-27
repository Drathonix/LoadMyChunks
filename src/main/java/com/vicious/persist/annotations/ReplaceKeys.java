package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a list of key replacements to convert keys within a Map to.
 * When unmapping an object, the list of replacements will be applied to all keys within the map.
 * Values can also be relocated to and from nested Maps given the nested Map exists.
 * Only supports hard-replacement as of now.
 * This allows refactoring config structure retroactively.
 * Transformations will only occur if the transformation version is detected as changed.
 * Example Transformations:
 * {oldKey,newKey} - Replaces any map entries in the current map with key 'oldKey' with 'newKey'
 * {oldKey,subMap/newKey} - Removes any map entries in the current map with 'oldKey' and adds an entry to 'subMap' with key of 'newKey' and the value associated with 'oldKey'
 * {subMap/oldKey,newKey} - Removes any map entries in the 'subMap 'with 'oldKey' and adds an entry to the current map with key of 'newKey' and the value associated with 'subMap/oldKey'
 * @author Jack Andersen
 * @since 1.2.4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ReplaceKeys {
    /**
     * @return Array of replacement pairs in the non-static context.
     */
    Pair[] nonStaticReplacements() default {};

    /**
     * @return Array of replacement pairs in the static context.
     */
    Pair[] staticReplacements() default {};

    /**
     * Will update a field in the file to prevent transformations from occurring multiple times unnecessarily.
     * Must be greater than 0.
     * @return the transformation version.
     */
    int transformerVersion();

    /**
     * Represents a replacement transformation pair.
     */
    @interface Pair {
        /**
         * @return The key target(s) to replace.
         */
        String[] target();

        /**
         * @return The replacement String.
         */
        String replacement();
    }
}
