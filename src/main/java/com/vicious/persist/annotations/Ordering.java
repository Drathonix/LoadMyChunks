package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any field names listed here will be saved in the exact order they are listed before any unlisted fields.
 * Unlisted fields will always be ordered after the last ordered field.
 * This is critical as most compilers will reorder fields such that they appear in alphabetical order but some users may be interested in their own ordering scheme.
 * @since 1.4.7
 * @author Jack Andersen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Ordering {
    /**
     * The ordering of fields. This applies to both non-static and static fields, do note that the order of non-static fields is only determined
     * @return the field ordering for both the static and non-static ctx.
     */
    String[] value() default {};
}
