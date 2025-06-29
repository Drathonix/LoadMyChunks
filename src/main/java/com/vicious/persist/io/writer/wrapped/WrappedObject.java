package com.vicious.persist.io.writer.wrapped;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A holder object that stores an Object-String object-comment pair.
 *
 * @author Jack Andersen
 * @since 1.0
 */
public class WrappedObject {
    public final @Nullable Object object;
    public final @NotNull String comment;

    public WrappedObject(@Nullable Object object) {
        this.object = object;
        comment = "";
    }

    public WrappedObject(@Nullable Object object, @Nullable String comment) {
        this.object = object;
        if(comment == null){
            comment = "";
        }
        this.comment = comment;
    }

    public static WrappedObject of(@Nullable Object object) {
        return new WrappedObject(object);
    }

    public static WrappedObject of(@Nullable Object object, @Nullable String comment) {
        return new WrappedObject(object, comment);
    }

    public static Object unwrap(@Nullable Object o){
        if(o instanceof WrappedObject){
            return ((WrappedObject) o).object;
        }
        else{
            return o;
        }
    }

    public static @NotNull String unwrapComment(@Nullable Object o){
        if(o instanceof WrappedObject){
            return ((WrappedObject) o).comment;
        }
        else{
            return "";
        }
    }

    public static WrappedObject nullified() {
        return of(null);
    }

    @Override
    public String toString() {
        String s = object instanceof String ? "\"" : (object instanceof Character ? "'" : "");
        return "(" + s + object.toString() + s + ")" + (!comment.isEmpty() ? "[" + comment + "]" : "");
    }
}
