package com.vicious.loadmychunks.config;

import org.spongepowered.asm.mixin.Mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Range {
    double value();
    double min() default 0D;
    double max() default Integer.MAX_VALUE;
}
