package persist.mappify.reflect;

import com.vicious.persist.annotations.PersistentPath;
import com.vicious.persist.annotations.Save;
import com.vicious.persist.except.InvalidAnnotationException;
import com.vicious.persist.except.InvalidSavableElementException;
import com.vicious.persist.mappify.Context;
import com.vicious.persist.mappify.reflect.FieldData;
import com.vicious.persist.mappify.reflect.PathFieldData;
import com.vicious.persist.mappify.registry.Reserved;
import com.vicious.persist.shortcuts.NotationFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClassData {
    private final Map<String, FieldData<?>> savableFields = new HashMap<>();
    private final com.vicious.persist.mappify.reflect.PathFieldData<?>[] persistentPath = new com.vicious.persist.mappify.reflect.PathFieldData[2];
    private static void forEach(Class<?> cls, Consumer<Class<?>> consumer){
        if(cls != null){
            consumer.accept(cls);
            for (Class<?> anInterface : cls.getInterfaces()) {
                consumer.accept(anInterface);
            }
            forEach(cls.getSuperclass(), consumer);
        }
    }

    public ClassData(Class<?> c){
        forEach(c,cls->{
            for (Method m1 : cls.getDeclaredMethods()) {
                Save save = m1.getAnnotation(Save.class);
                PersistentPath path = m1.getAnnotation(PersistentPath.class);
                if(save != null){
                    String name = save.value().isEmpty() ? m1.getName() : save.value();
                    if(Modifier.isAbstract(m1.getModifiers())){
                        throw new InvalidSavableElementException("Abstract method " + m1.getName() + " in " + m1.getDeclaringClass() + " @Save(\"" + name + "\"), this is illegal. Maybe you should create a wrapper method instead.");
                    }
                    if(Reserved.isReserved(name)){
                        throw new InvalidSavableElementException("Method " + m1.getName() + " in " + m1.getDeclaringClass() + " @Save(\"" + name + "\"), has a reserved name! Use a different name.");
                    }
                    if(savableFields.containsKey(name)){
                        continue;
                    }
                    Method setter = null;
                    for (Method declaredMethod : cls.getDeclaredMethods()) {
                        Save.Setter saveSetter = declaredMethod.getAnnotation(Save.Setter.class);
                        if(saveSetter != null){
                            if(Modifier.isAbstract(declaredMethod.getModifiers())){
                                throw new InvalidAnnotationException("Abstract method " + declaredMethod.getName() + " in " + declaredMethod.getDeclaringClass() + " @Save.Setter(\"" + saveSetter.value() + "\"), this is illegal. Maybe you should create a wrapper method instead.");
                            }
                            if(saveSetter.value().equals(name) && staticMatches(m1,declaredMethod)){
                                setter = declaredMethod;
                                break;
                            }
                        }
                    }
                    savableFields.put(name, new FieldData<>(m1,setter));
                }
                if(path != null){
                    int idx = Modifier.isStatic(m1.getModifiers()) ? 1 : 0;
                    if(persistentPath[idx] != null){
                        continue;
                    }
                    if(m1.getReturnType() != String.class || m1.getParameterCount() != 0){
                        throw new InvalidAnnotationException("@Persistent path applied to non-String returning parameterless method: " + m1.getName() + " in " + m1.getDeclaringClass());
                    }
                    persistentPath[idx] = new com.vicious.persist.mappify.reflect.PathFieldData<>(m1);
                }
            }
            for (Field field : cls.getDeclaredFields()) {
                Save save = field.getAnnotation(Save.class);
                PersistentPath path = field.getAnnotation(PersistentPath.class);
                if(save != null){
                    String name = save.value().isEmpty() ? field.getName() : save.value();
                    if(savableFields.containsKey(name)){
                        continue;
                    }
                    if(Reserved.isReserved(name)){
                        throw new InvalidSavableElementException("Field " + field.getName() + " in " + field.getDeclaringClass() + " @Save(\"" + name + "\"), has a reserved name! Use a different name.");
                    }
                    Method setter = null;
                    for (Method declaredMethod : cls.getDeclaredMethods()) {
                        Save.Setter saveSetter = declaredMethod.getAnnotation(Save.Setter.class);
                        if(saveSetter != null){
                            if(saveSetter.value().equals(name) && staticMatches(field,declaredMethod)){
                                setter = declaredMethod;
                                break;
                            }
                        }
                    }
                    savableFields.put(name, new FieldData<>(field,setter));
                }
                if(path != null){
                    int idx = Modifier.isStatic(field.getModifiers()) ? 1 : 0;
                    if(persistentPath[idx] != null){
                        continue;
                    }
                    if(field.getType() != String.class){
                        throw new InvalidSavableElementException("@Persistent path applied to non-String field: " + field.getName() + " in " + field.getDeclaringClass());
                    }
                    persistentPath[idx] = new com.vicious.persist.mappify.reflect.PathFieldData<>(field);
                }
            }
        });
    }

    private boolean staticMatches(Member m1, Method declaredMethod) {
        return (Modifier.isStatic(m1.getModifiers()) && Modifier.isStatic(declaredMethod.getModifiers()))
                || (!Modifier.isStatic(m1.getModifiers()) && !Modifier.isStatic(declaredMethod.getModifiers()));
    }

    public void forEach(boolean isStatic, Consumer<FieldData<?>> accessor){
        savableFields.forEach((name, field) -> {
            if(field.matchesStaticness(isStatic)) {
                accessor.accept(field);
            }
        });
    }

    public boolean hasTraitsInContext(boolean isStatic) {
        for (FieldData<?> value : savableFields.values()) {
            if(value.matchesStaticness(isStatic)) {
                return true;
            }
        }
        return false;
    }

    public void whenPresent(String key, boolean isStatic, Consumer<FieldData<?>> consumer) {
        FieldData<?> field = savableFields.get(key);
        if(field != null && field.matchesStaticness(isStatic)) {
            consumer.accept(field);

        }
    }

    public PathFieldData<?> getPersistentPathAnnotation(Context context){
        int idx = context.isStatic ? 1 : 0;
        if(persistentPath[idx] != null){
            return persistentPath[idx];
        }
        else{
            throw new IllegalArgumentException(context.getType() + " is missing an @PersistentPath annotated method or field in the " + (context.isStatic ? "static" : "non-static") + " context!");
        }
    }

    public String getPersistentPath(Context context){
        return (String)getPersistentPathAnnotation(context).get(context);
    }

    public NotationFormat getPersistentPathFormat(Context context) {
        return getPersistentPathAnnotation(context).path.value();
    }

    public boolean getPersistentPathMigrateMode(Context context) {
        return getPersistentPathAnnotation(context).path.autoMigrate();
    }
}
