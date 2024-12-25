package persist.io.writer.wrapped;

import com.vicious.persist.io.writer.wrapped.IWrapped;
import com.vicious.persist.io.writer.wrapped.WrappedObject;

import java.util.ArrayList;
import java.util.List;

public class WrappedObjectList extends ArrayList<com.vicious.persist.io.writer.wrapped.WrappedObject> implements com.vicious.persist.io.writer.wrapped.IWrapped<List<Object>> {
    public List<Object> unwrap(){
        List<Object> out = new ArrayList<>();
        for (WrappedObject wrappedObject : this) {
            Object value = wrappedObject.object;
            if(value instanceof com.vicious.persist.io.writer.wrapped.IWrapped<?>) {
                value = ((IWrapped<?>) value).unwrap();
            }
            out.add(value);
        }
        return out;
    }
}
