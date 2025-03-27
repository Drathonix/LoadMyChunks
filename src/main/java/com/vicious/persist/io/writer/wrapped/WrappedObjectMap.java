package com.vicious.persist.io.writer.wrapped;

import java.util.HashMap;
import java.util.Map;

/**
 * A map that maps WrappedObjects to arbitrary key types.
 * @author Jack Andersen
 * @since 1.0
 */
public class WrappedObjectMap extends HashMap<Object,WrappedObject> implements IWrapped<Map<Object,Object>> {
    public Map<Object,Object> unwrap(){
        Map<Object,Object> out = new HashMap<>();
        for (Object key : keySet()) {
            Object value = get(key).object;
            if(value instanceof IWrapped<?>) {
                value = ((IWrapped<?>) value).unwrap();
            }
            out.put(key,value);
        }
        return out;
    }
}
