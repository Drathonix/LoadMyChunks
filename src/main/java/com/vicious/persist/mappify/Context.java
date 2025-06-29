package com.vicious.persist.mappify;

import com.vicious.persist.annotations.ReplaceKeys;
import com.vicious.persist.mappify.reflect.ClassData;
import com.vicious.persist.mappify.reflect.FieldData;
import com.vicious.persist.mappify.registry.Reserved;
import com.vicious.persist.mappify.registry.Stringify;
import com.vicious.persist.shortcuts.NotationFormat;
import com.vicious.persist.util.StringTree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents the current state of a {@link com.vicious.persist.mappify.Mappifier}
 *
 * Every class has 2 possible Contexts.
 * The static Context applies to only elements with the static modifier.
 * The non-static Context applies to only elements without the static modifier.
 *
 * @author Jack Andersen
 * @since 1.0
 */
public class Context {
    /**
     * True if the source is a {@link java.lang.Class} instance.
     */
    public final boolean isStatic;

    /**
     * The type of the context's source.
     */
    public final Class<?> type;
    /**
     * Whether the source object is an enum instance.
     */
    public final boolean isEnum;
    /**
     * Whether the source object is an array instance.
     */
    public final boolean isArray;
    /**
     * The source object that the context applies to.
     */
    public final Object source;
    /**
     * The {@link com.vicious.persist.mappify.reflect.ClassData} of the source object.
     */
    public final ClassData data;

    /**
     * Creates a context from the source object.
     * @param source any object.
     */
    protected Context(Object source){
        this.isStatic = source instanceof Class<?>;
        this.isEnum = source instanceof Enum<?>;
        this.isArray = source instanceof Array;
        this.type = isEnum ? ((Enum<?>) source).getDeclaringClass() : isStatic ? (Class<?>)source : source.getClass();
        this.source=source;
        this.data = ClassData.getClassData(this.type);
    }

    /**
     * Creates a context from the source object.
     * @param source any object.
     */
    public static Context of(Object source){
        return new Context(source);
    }

    public Class<?> getType(){
        return type;
    }

    public void forEach(Consumer<FieldData<?>> consumer) {
        data.forEach(isStatic, consumer);
    }

    public boolean hasMappifiableTraits() {
        return data.hasTraitsInContext(isStatic);
    }

    public void whenPresent(String key, Consumer<FieldData<?>> consumer) {
        data.whenPresent(key,isStatic,consumer);
    }

    public boolean hasMappifiableTraits(boolean isStatic) {
        return data.hasTraitsInContext(isStatic);
    }

    public String getPersistentPath() {
        return data.getPersistentPath(this);
    }

    public NotationFormat getPersistentPathFormat() {
        return data.getPersistentPathFormat(this);
    }

    public boolean getPersistentPathMigrateMode() {
        return data.getPersistentPathMigrateMode(this);
    }

    public boolean hasTransformations() {
        return data.hasTransformations(isStatic);
    }

    /**
     * Applies key transformations from {@link ReplaceKeys}
     * @param map the map to transform.
     */
    public void transform(Map<Object, Object> map) {
        if(map.containsKey(Reserved.TRANSFORMER_VER) && map.get(Reserved.TRANSFORMER_VER) instanceof Number){
            if(data.getTransformerVer() == ((Number)map.get(Reserved.TRANSFORMER_VER)).intValue()){
                return;
            }
        }
        transform(map,map,data.getTransformations(isStatic),0,"");
    }

    @SuppressWarnings("unchecked")
    private void transform(Map<Object,Object> rootMap, Map<Object, Object> map, StringTree<String> tree, int depth, String path) {
        int treeDepth = tree.depth();
        for (Object o : new ArrayList<>(map.keySet())) {
            String str = Stringify.stringify(o);
            String pth = path+str;
            String replacement = tree.get(pth);
            if (replacement != null) {
                Object val = map.remove(str);
                String[] dest = replacement.split("/");
                Map<Object,Object> sub = rootMap;
                for (int i = 0; i < dest.length-1; i++) {
                    sub = (Map<Object,Object>)sub.computeIfAbsent(dest[i],k->new HashMap<>());
                }
                sub.put(dest[dest.length-1],val);
            } else if (depth+1 < treeDepth && map.get(str) instanceof Map && tree.containsNode(pth)) {
                transform(rootMap,(Map<Object,Object>) map.get(str), tree, depth + 1, pth + "/");
            }
        }
    }

    public int getTransformerVer() {
        return data.getTransformerVer();
    }

    public FieldData<?> getField(String targetField) {
        return data.getField(targetField);
    }
}
