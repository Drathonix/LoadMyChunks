package com.vicious.persist.mappify.reflect;

import com.vicious.persist.annotations.PersistentPath;
import com.vicious.persist.mappify.Context;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;

/**
 * Stores a Field or Method marked with {@link PersistentPath}
 *
 * @since 1.0
 * @author Jack Andersen
 * @param <T> the getter element type.
 */
public class PathFieldData<T extends AccessibleObject & Member> {
    /**
     * A method or field returning a String.
     */
    public final T getterElement;
    public final PersistentPath path;

    public PathFieldData(T element) {
        this.getterElement = element;
        element.setAccessible(true);
        this.path = element.getAnnotation(PersistentPath.class);
    }

    public boolean matchesStaticness(boolean isStatic) {
        return (isStatic && Modifier.isStatic(getterElement.getModifiers())) || (!isStatic && !Modifier.isStatic(getterElement.getModifiers()));
    }

    /**
     * Returns the relative path String.
     *
     * @param context the context.
     * @return The relative path String.
     */
    public @Nullable Object get(Context context) {
        try {
            if(getterElement instanceof Field) {
                return ((Field) getterElement).get(context.source);
            }
            else if(getterElement instanceof Method){
                return ((Method) getterElement).invoke(context.source);
            }
            throw new IllegalStateException("Impossible state.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access field ",e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Could not invoke field getter method",e);
        }
    }
}
