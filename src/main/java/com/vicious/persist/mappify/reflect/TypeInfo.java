package com.vicious.persist.mappify.reflect;

import com.vicious.persist.except.InvalidSavableElementException;
import com.vicious.persist.mappify.registry.Stringify;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * An interface used to get information about the something with a type.
 *
 * @since 1.0
 * @author Jack Andersen
 */
public interface TypeInfo {
    /**
     * Creates a temporary TypeInfo representing an arbitrary value type and retains the typing array.
     * @param info the original info
     * @param valueType the new type of the current value.
     * @return an anonymous CastedInfo instance.
     */
    static TypeInfo cast(TypeInfo info, Class<?> valueType) {
        return new TypeInfo() {
            @Override
            public Class<?> getType() {
                return valueType;
            }

            @Override
            public Class<?>[] getTyping() {
                return info.getTyping();
            }

            @Override
            public String toString() {
                return "CastedInfo{"+ valueType + ", " + Arrays.toString(info.getTyping()) + "}";
            }
        };
    }

    /**
     * @return The class being represented.
     */
    Class<?> getType();

    /**
     * @return An array of internal generic typings. see {@link com.vicious.persist.annotations.Typing}
     */
    Class<?>[] getTyping();

    /**
     * @return whether the type class is a Map.
     */
    default boolean isMap(){
        return Map.class.isAssignableFrom(getType());
    }

    /**
     * @return whether the type class is a collection.
     */
    default boolean isCollection(){
        return Collection.class.isAssignableFrom(getType());
    }

    /**
     * @return whether the type class is {@link Class}
     */
    default boolean isClass(){
        return Class.class.isAssignableFrom(getType());
    }

    /**
     * @return whether the type class is a primitive.
     */
    default boolean isPrimitive(){
        return getType().isPrimitive();
    }

    /**
     * @return whether the type class can be converted to and from a String.
     */
    default boolean canBeStringified(){
        return Stringify.present(getType());
    }

    /**
     * @return whether the type class is an array.
     */
    default boolean isArray(){
        return getType().isArray();
    }

    /**
     * Gets the typing at a specific index.
     * @param index the index
     * @return a typing Class.
     */
    default Class<?> getTyping(int index) {
        Class<?>[] typing = getTyping();
        if(typing.length <= index){
            throw new InvalidSavableElementException("Typing is of length " + typing.length + " needs to be at least " + (index+1));
        }
        return typing[index];
    }

    /**
     * @return Whether the type class is a common data structure.
     */
    default boolean isDataStructure(){
        return isMap() || isCollection() || isArray();
    }

    /**
     * @return Whether the type class is an enum.
     */
    default boolean isEnum(){
        return getType().isEnum();
    }
}
