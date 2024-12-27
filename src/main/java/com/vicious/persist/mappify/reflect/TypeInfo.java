package com.vicious.persist.mappify.reflect;

import com.vicious.persist.except.InvalidSavableElementException;
import com.vicious.persist.mappify.registry.Stringify;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public interface TypeInfo {
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

    Class<?> getType();
    Class<?>[] getTyping();

    default boolean isMap(){
        return Map.class.isAssignableFrom(getType());
    }

    default boolean isCollection(){
        return Collection.class.isAssignableFrom(getType());
    }

    default boolean isClass(){
        return Class.class.isAssignableFrom(getType());
    }

    default boolean isPrimitive(){
        return getType().isPrimitive();
    }

    default boolean canBeStringified(){
        return Stringify.present(getType());
    }

    default boolean isArray(){
        return getType().isArray();
    }

    default Class<?> getTyping(int index) {
        Class<?>[] typing = getTyping();
        if(typing.length <= index){
            throw new InvalidSavableElementException("Typing is of length " + typing.length + " needs to be at least " + (index+1));
        }
        return typing[index];
    }

    default boolean isDataStructure(){
        return isMap() || isCollection() || isArray();
    }

    default boolean isEnum(){
        return getType().isEnum();
    }
}
