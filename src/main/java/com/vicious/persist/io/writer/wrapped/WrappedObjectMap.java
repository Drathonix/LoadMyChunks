package com.vicious.persist.io.writer.wrapped;

import com.vicious.persist.io.writer.wrapped.IWrapped;
import com.vicious.persist.io.writer.wrapped.WrappedObject;

import java.util.HashMap;
import java.util.Map;

public class WrappedObjectMap extends HashMap<Object, WrappedObject> implements com.vicious.persist.io.writer.wrapped.IWrapped<Map<Object,Object>> {
    public Map<Object,Object> unwrap(){
        Map<Object,Object> out = new HashMap<>();
        for (Object key : keySet()) {
            Object value = get(key).object;
            if(value instanceof com.vicious.persist.io.writer.wrapped.IWrapped<?>) {
                value = ((IWrapped<?>) value).unwrap();
            }
            out.put(key,value);
        }
        return out;
    }
}
