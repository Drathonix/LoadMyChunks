package com.vicious.persist.mappify.reflect;

import com.vicious.persist.annotations.AltName;
import com.vicious.persist.annotations.PersistentPath;
import com.vicious.persist.annotations.ReplaceKeys;
import com.vicious.persist.annotations.Save;
import com.vicious.persist.except.InvalidAnnotationException;
import com.vicious.persist.except.InvalidSavableElementException;
import com.vicious.persist.mappify.Context;
import com.vicious.persist.mappify.registry.Reserved;
import com.vicious.persist.shortcuts.NotationFormat;
import com.vicious.persist.util.StringTree;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Jack Andersen
 * @since 1.0
 */
public class ClassData {
    /**
     * A map of all Fields marked with {@link Save} by savable name.
     */
    private final Map<String, FieldData<?>> savableFields = new HashMap<>();
    /**
     * The Fields marked with {@link PersistentPath} by context (non-static or static)
     * There can only be a maximum of two.
     */
    private final PathFieldData<?>[] persistentPath = new PathFieldData[2];
    /**
     * A tree of key transformations that may be applied before object unmapping.
     */
    @SuppressWarnings("unchecked")
    private final StringTree<String>[] keyTransformations = new StringTree[2];

    /**
     * The class transformation version.
     */
    private final int transformerVer;

    /**
     * Goes through a class' hierarchy and executes some arbitrary code.
     * @param cls the child class
     * @param consumer the code to execute on that class' hierarchy
     */
    private static void forEach(Class<?> cls, Consumer<Class<?>> consumer){
        if(cls != null){
            consumer.accept(cls);
            for (Class<?> anInterface : cls.getInterfaces()) {
                consumer.accept(anInterface);
            }
            forEach(cls.getSuperclass(), consumer);
        }
    }

    /**
     * Initializes the savableFields and persistentPath reference maps.
     * @throws InvalidAnnotationException if an illegal annotation is detected.
     * @throws InvalidSavableElementException if an illegal {@link Save} annotation is present.
     * @param c the class to build from.
     */
    public ClassData(Class<?> c){
        AtomicInteger tSum = new AtomicInteger(0);
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
                    AltName altName = m1.getAnnotation(AltName.class);
                    FieldData<?> data = new FieldData<>(m1,setter);
                    if(altName != null){
                        for (String s : altName.value()) {
                            if(!savableFields.containsKey(s) && !Reserved.isReserved(s)) {
                                savableFields.put(s, data);
                            }
                        }
                    }
                    savableFields.put(name, data);
                }
                if(path != null){
                    int idx = Modifier.isStatic(m1.getModifiers()) ? 1 : 0;
                    if(persistentPath[idx] != null){
                        continue;
                    }
                    if(m1.getReturnType() != String.class || m1.getParameterCount() != 0){
                        throw new InvalidAnnotationException("@Persistent path applied to non-String returning parameterless method: " + m1.getName() + " in " + m1.getDeclaringClass());
                    }
                    persistentPath[idx] = new PathFieldData<>(m1);
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
                    AltName altName = field.getAnnotation(AltName.class);
                    FieldData<?> data = new FieldData<>(field,setter);
                    if(altName != null){
                        for (String s : altName.value()) {
                            if(!savableFields.containsKey(s) && !Reserved.isReserved(s)) {
                                savableFields.put(s, data);
                            }
                        }
                    }
                    savableFields.put(name, data);
                }
                if(path != null){
                    int idx = Modifier.isStatic(field.getModifiers()) ? 1 : 0;
                    if(persistentPath[idx] != null){
                        continue;
                    }
                    if(field.getType() != String.class){
                        throw new InvalidSavableElementException("@Persistent path applied to non-String field: " + field.getName() + " in " + field.getDeclaringClass());
                    }
                    persistentPath[idx] = new PathFieldData<>(field);
                }
            }
            ReplaceKeys transformKeys = cls.getAnnotation(ReplaceKeys.class);
            if(transformKeys != null){
                if(transformKeys.transformerVersion() <= 0){
                    throw new InvalidAnnotationException("@TransformKeys in " + cls + " is invalid. TransformerVer must be greater than 0");
                }
                tSum.getAndAdd(transformKeys.transformerVersion());
                ReplaceKeys.Pair[] transformations = transformKeys.nonStaticReplacements();
                if(transformations.length > 0) {
                    keyTransformations[0] = new StringTree<>();
                    for (ReplaceKeys.Pair pair : transformations) {
                        if (pair.replacement().isEmpty()) {
                            throw new InvalidAnnotationException("@TransformKeys non static key transformation pair in " + cls + " is invalid. Replacement value cannot be empty!");
                        }
                        for (String s : pair.target()) {
                            if (keyTransformations[0].containsKey(s)) {
                                throw new InvalidAnnotationException("@TransformKeys non static key transformation pair in " + cls + " is invalid. Target is already being replaced by another annotation! Check if a super class/interface has @TransformKeys!");
                            }
                            keyTransformations[0].put(s, pair.replacement());
                        }
                    }
                }
                transformations = transformKeys.staticReplacements();
                if(transformations.length > 0) {
                    keyTransformations[1]=new StringTree<>();
                    for (ReplaceKeys.Pair pair : transformations) {
                        if (pair.replacement().isEmpty()) {
                            throw new InvalidAnnotationException("@TransformKeys static key transformation pair in " + cls + " is invalid. Replacement value cannot be empty!");
                        }
                        for (String s : pair.target()) {
                            if (keyTransformations[1].containsKey(s)) {
                                throw new InvalidAnnotationException("@TransformKeys static key transformation pair in " + cls + " is invalid. Target is already being replaced by another annotation! Check if a super class/interface has @TransformKeys!");
                            }

                            keyTransformations[1].put(s, pair.replacement());
                        }
                    }
                }
            }
        });
        transformerVer=tSum.get();
    }

    /**
     * Checks if two methods have the same static-ness.
     * @return if the methods have the same static modifier.
     */
    private boolean staticMatches(Member m1, Method declaredMethod) {
        return (Modifier.isStatic(m1.getModifiers()) && Modifier.isStatic(declaredMethod.getModifiers()))
                || (!Modifier.isStatic(m1.getModifiers()) && !Modifier.isStatic(declaredMethod.getModifiers()));
    }

    /**
     * Iterates through all the FieldData instances of the static context provided.
     * @param isStatic the static context level to filter by.
     * @param accessor some arbitrary code to run on the {@link FieldData} instance.
     */
    public void forEach(boolean isStatic, Consumer<FieldData<?>> accessor){
        savableFields.forEach((name, field) -> {
            if(field.matchesStaticness(isStatic)) {
                accessor.accept(field);
            }
        });
    }

    /**
     * Checks if an object has savable fields in the static context provided.
     * @param isStatic the static context.
     * @return if savable fields exist for the static context.
     */
    public boolean hasTraitsInContext(boolean isStatic) {
        for (FieldData<?> value : savableFields.values()) {
            if(value.matchesStaticness(isStatic)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Executes arbitrary code on a {@link FieldData} instance if there is one present.
     * @param key the FieldData's name
     * @param isStatic the expected static context.
     * @param consumer some arbitrary code.
     */
    public void whenPresent(String key, boolean isStatic, Consumer<FieldData<?>> consumer) {
        FieldData<?> field = savableFields.get(key);
        if(field != null && field.matchesStaticness(isStatic)) {
            consumer.accept(field);
        }
    }

    /**
     * Returns the persistent {@link PathFieldData} instance for the context.
     * @param context the static context.
     * @return the PathFieldData instance for the context.
     * @throws IllegalArgumentException if no PathFieldData is present.
     */
    public PathFieldData<?> getPersistentPathAnnotation(Context context){
        int idx = context.isStatic ? 1 : 0;
        if(persistentPath[idx] != null){
            return persistentPath[idx];
        }
        else{
            throw new IllegalArgumentException(context.getType() + " is missing an @PersistentPath annotated method or field in the " + (context.isStatic ? "static" : "non-static") + " context!");
        }
    }

    /**
     * Returns the persistent String path for the context.
     * @param context the static context.
     * @return the relative path for the context.
     * @throws IllegalArgumentException if no PathFieldData is present.
     */
    public String getPersistentPath(Context context){
        return (String)getPersistentPathAnnotation(context).get(context);
    }

    /**
     * Returns the persistent file format for the context.
     * @param context the static context.
     * @return the {@link NotationFormat} for the context.
     * @throws IllegalArgumentException if no PathFieldData is present.
     */
    public NotationFormat getPersistentPathFormat(Context context) {
        return getPersistentPathAnnotation(context).path.value();
    }

    /**
     * Returns the persistent migration mode for the context.
     * @param context the static context.
     * @return the {@link com.vicious.persist.shortcuts.Migrator} mode for the context.
     * @throws IllegalArgumentException if no PathFieldData is present.
     */
    public boolean getPersistentPathMigrateMode(Context context) {
        return getPersistentPathAnnotation(context).path.autoMigrate();
    }

    /**
     * Checks if there are key transformations in the static context.
     * @param isStatic the static context.
     * @return whether there are key transformations for that context.
     */
    public boolean hasTransformations(boolean isStatic) {
        return keyTransformations[isStatic ? 1 : 0] != null;
    }

    /**
     * Checks gets the key transformations for the context.
     * @param isStatic the static context.
     * @return the key transformations.
     */
    public @Nullable StringTree<String> getTransformations(boolean isStatic) {
        return keyTransformations[isStatic ? 1 : 0];
    }

    public int getTransformerVer() {
        return transformerVer;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        if (hasTraitsInContext(true)) {
            out.append("Static Context {\n");
            try{
                out.append("\tPath ").append(getPersistentPath(Context.of(Class.class))).append("\n");
                out.append("\tFormat ").append(getPersistentPathFormat(Context.of(Class.class))).append("\n");
                out.append("\tCan Migrate: ").append(getPersistentPathMigrateMode(Context.of(Class.class))).append("\n");
            } catch (Throwable ignored){}
            out.append("\tSavable Elements: {\n");
            savableFields.forEach((name, field) -> {
                if(field.matchesStaticness(true)) {
                    out.append("\t\t").append(name).append(": ").append(field.getType()).append("\n");
                }
            });
            out.append("\t}\n");
            out.append("\tKey Transformations: ").append(keyTransformations[1] == null ? "null" : keyTransformations[1].toString()).append("\n");
            out.append("}\n");
        }

        if (hasTraitsInContext(false)) {
            out.append("Non-Static Context {\n");
            try{
                out.append("\tPath ").append(getPersistentPath(Context.of(1))).append("\n");
                out.append("\tFormat ").append(getPersistentPathFormat(Context.of(1))).append("\n");
                out.append("\tCan Migrate: ").append(getPersistentPathMigrateMode(Context.of(1))).append("\n");
            } catch (Throwable ignored){}
            out.append("\tSavable Elements: {\n");
            savableFields.forEach((name, field) -> {
                if(field.matchesStaticness(false)) {
                    out.append("\t\t").append(name).append(": ").append(field.getType()).append("\n");
                }
            });
            out.append("\t}\n");
            out.append("\tKey Transformations: ").append(keyTransformations[0] == null ? "null" : keyTransformations[0].toString()).append("\n");
            out.append("}\n");
        }
        return out.toString();
    }
}
