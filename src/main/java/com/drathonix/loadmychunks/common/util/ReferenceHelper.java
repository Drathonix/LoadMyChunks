package com.drathonix.loadmychunks.common.util;

import java.util.concurrent.atomic.AtomicReference;

public class ReferenceHelper {
    public static <T> boolean isReferenceType(Class<T> type, Object object){
        if(type.isInstance(object)){
            return true;
        }
        else if(object instanceof AtomicReference<?>){
            object = ((AtomicReference<?>) object).get();
            return type.isInstance(object);
        }
        return false;
    }

    public static <T> void assertIsType(Class<T> type, Object object){
        if(!isReferenceType(type, object)){
            throw new IllegalArgumentException(object + " is not of the required type: " + type + ".");
        }
    }

    public static <T> T get(Class<T> type, Object object){
        if(type.isInstance(object)){
            return type.cast(object);
        }
        else if(object instanceof AtomicReference<?>){
            object = ((AtomicReference<?>) object).get();
            if(type.isInstance(object)){
                return type.cast(object);
            }
        }
        throw new IllegalArgumentException(object + " is not of the required type: " + type + ".");

    }
}
