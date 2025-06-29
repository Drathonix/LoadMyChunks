package com.vicious.persist.io.writer.wrapped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrappedObjectList extends ArrayList<WrappedObject> implements IWrapped<List<Object>> {
    public List<Object> unwrap(){
        List<Object> out = new ArrayList<>();
        for (WrappedObject wrappedObject : this) {
            Object value = wrappedObject.object;
            if(value instanceof IWrapped<?>) {
                value = ((IWrapped<?>) value).unwrap();
            }
            out.add(value);
        }
        return out;
    }
}
