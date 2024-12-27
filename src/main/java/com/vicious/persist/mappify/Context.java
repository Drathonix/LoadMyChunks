package com.vicious.persist.mappify;

import com.vicious.persist.mappify.reflect.ClassData;
import com.vicious.persist.mappify.reflect.FieldData;
import com.vicious.persist.shortcuts.NotationFormat;
import com.vicious.persist.util.ClassMap;

import java.util.function.Consumer;

public class Context {
    private static final ClassMap<ClassData> classData = new ClassMap<>();

    public final Class<?> type;
    public final boolean isStatic;
    public final boolean isEnum;
    public final Object source;
    public final ClassData data;

    protected Context(Object source){
        this.isStatic = source instanceof Class<?>;
        this.isEnum = source instanceof Enum<?>;
        this.type = isEnum ? ((Enum<?>) source).getDeclaringClass() : isStatic ? (Class<?>)source : source.getClass();
        this.source=source;
        this.data = getClassData(this);
    }

    public static Context of(Object source){
        return new Context(source);
    }

    public static synchronized ClassData getClassData(Context context) {
        return classData.computeIfAbsent(context.getType(), ClassData::new);
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
}
