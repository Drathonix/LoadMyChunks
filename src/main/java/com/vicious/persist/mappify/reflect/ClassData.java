package com.vicious.persist.mappify.reflect;

import com.vicious.persist.Persist;
import com.vicious.persist.annotations.*;
import com.vicious.persist.except.InvalidAnnotationException;
import com.vicious.persist.except.InvalidSavableElementException;
import com.vicious.persist.mappify.Context;
import com.vicious.persist.mappify.registry.Initializers;
import com.vicious.persist.mappify.registry.Reserved;
import com.vicious.persist.shortcuts.NotationFormat;
import com.vicious.persist.util.ClassMap;
import com.vicious.persist.util.ReflectionHelper;
import com.vicious.persist.util.StringTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Stores necessary information for Persist's Mappifier.
 * @author Jack Andersen
 * @since 1.0
 */
public class ClassData {
    private static final ClassMap<ClassData> classData = new ClassMap<>();
    /**
     * Map of all savable Fields by name or alt name. This is not a BiMap
     * A map of all Fields marked with {@link com.vicious.persist.annotations.Save} by savable name.
     */
    @NotNull
    private final Map<String, FieldData<?>> nameToField = new LinkedHashMap<>();

    /**
     * Set of all unique Fields present in the class.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private final LinkedHashSet<FieldData<?>>[] savableFields = new LinkedHashSet[2];

    /**
     * The Fields marked with {@link com.vicious.persist.annotations.PersistentPath} by context (non-static or static)
     * There can only be a maximum of two.
     */
    @NotNull
    private final PathFieldData<?>[] persistentPath = new PathFieldData[2];
    /**
     * A tree of key transformations that may be applied before object unmapping.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    private final StringTree<String>[] keyTransformations = new StringTree[2];

    /**
     * The class transformation version.
     */
    private final int transformerVer;

    /**
     * Set of all Fields that must be unmapped.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private final LinkedHashSet<FieldData<?>>[] requiredFields = new LinkedHashSet[2];

    /**
     * A special initializer for classes with @Save constructors. If this is present the constructor will be called when initializing the object.
     * @since 1.4.2
     */
    @Nullable
    private final Initializers.CustomConstructor<?> initializer;

    /**
     * Initializes the entire class data object.
     * @throws InvalidAnnotationException if an illegal annotation is detected.
     * @throws InvalidSavableElementException if an illegal {@link com.vicious.persist.annotations.Save} annotation is present.
     * @param c the class to build from.
     */
    @SuppressWarnings("unchecked")
    public ClassData(Class<?> c){
        for (int i = 0; i < 2; i++) {
            savableFields[i] = new LinkedHashSet<>();
            requiredFields[i] = new LinkedHashSet<>();
        }
        AtomicInteger tSum = new AtomicInteger(0);
        boolean hasInitializer = Initializers.canGenerateInitializerFor(c);
        AtomicBoolean hasPriorityOverrides = new AtomicBoolean(false);
        ReflectionHelper.forEach(c, cls->{
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
                    if(nameToField.containsKey(name)){
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
                    FieldData<?> data = new FieldData<>(m1,setter,hasInitializer);
                    if(altName != null){
                        for (String s : altName.value()) {
                            if(!nameToField.containsKey(s) && !Reserved.isReserved(s)) {
                                nameToField.put(s, data);
                            }
                        }
                    }
                    nameToField.put(name, data);
                    savableFields[getStaticIDX(data.isStatic())].add(data);
                    if(data.getPriority() > Integer.MIN_VALUE){
                        hasPriorityOverrides.set(true);
                    }
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
                    if(nameToField.containsKey(name)){
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
                    FieldData<?> data = new FieldData<>(field,setter,hasInitializer);
                    if(altName != null){
                        for (String s : altName.value()) {
                            if(!nameToField.containsKey(s) && !Reserved.isReserved(s)) {
                                nameToField.put(s, data);
                            }
                        }
                    }
                    nameToField.put(name, data);
                    savableFields[getStaticIDX(data.isStatic())].add(data);
                    if(data.getPriority() > Integer.MIN_VALUE){
                        hasPriorityOverrides.set(true);
                    }
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
        for (int i = 0; i < savableFields.length; i++) {
            for (FieldData<?> value : savableFields[i]) {
                if(value.isRequired()){
                    requiredFields[i].add(value);
                }
            }
        }
        initializer = Initializers.tryGenerateCustomReconstructorFor(c,this);
        transformerVer=tSum.get();
        Ordering ord = c.getAnnotation(Ordering.class);
        if(ord != null || hasPriorityOverrides.get()){
            LinkedHashSet<FieldData<?>>[] reorderedFields = new LinkedHashSet[2];
            for (int i = 0; i < 2; i++) {
                reorderedFields[i] = new LinkedHashSet<>();
            }
            if(ord != null){
                for (String s : ord.value()) {
                    FieldData<?> fieldData = nameToField.get(s);
                    if(fieldData == null){
                        Persist.logger.warning("@Ordering in class " + c + " has field of name'" + s + "' listed but no @Save field of that name exists in the class hierarchy.");
                        continue;
                    }
                    reorderedFields[getStaticIDX(fieldData.isStatic())].add(fieldData);
                }
            }
            PriorityQueue<FieldData<?>> queue = new PriorityQueue<>(Comparator.comparing(FieldData::getPriority));
            for (int i = 0; i < 2; i++) {
                for (LinkedHashSet<FieldData<?>> set : savableFields) {
                    queue.addAll(set);
                }
            }
            ArrayList<FieldData<?>> lst = new ArrayList<>(queue);
            for (int i = lst.size() - 1; i >= 0; i--) {
                FieldData<?> fieldData = lst.get(i);
                reorderedFields[getStaticIDX(fieldData.isStatic())].add(fieldData);
            }
            System.arraycopy(reorderedFields, 0, savableFields, 0, 2);
        }
    }

    /**
     * Gets the class data for an arbitrary object.
     * @param object the object, can be a class object or an instance.
     * @return the object's class data.
     */
    public static @NotNull ClassData getClassData(@NotNull Object object){
        if(object instanceof Class<?>){
            return getClassData((Class<?>)object);
        }
        else{
            return getClassData(object.getClass());
        }
    }

    /**
     * Gets the class data for a specific class.
     * @param type the class to use.
     * @return A ClassData object.
     */
    public static synchronized @NotNull ClassData getClassData(Class<?> type) {
        return classData.computeIfAbsent(type, ClassData::new);
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
     * @param accessor some arbitrary code to run on the {@link com.vicious.persist.mappify.reflect.FieldData} instance.
     */
    public void forEach(boolean isStatic, Consumer<FieldData<?>> accessor){
        savableFields[getStaticIDX(isStatic)].forEach(accessor);
    }

    /**
     * Checks if an object has savable fields in the static context provided.
     * @param isStatic the static context.
     * @return if savable fields exist for the static context.
     */
    public boolean hasTraitsInContext(boolean isStatic) {
        return !savableFields[getStaticIDX(isStatic)].isEmpty();
    }

    /**
     * Executes arbitrary code on a {@link com.vicious.persist.mappify.reflect.FieldData} instance if there is one present.
     * @param key the FieldData's name
     * @param isStatic the expected static context.
     * @param consumer some arbitrary code.
     */
    public void whenPresent(String key, boolean isStatic, Consumer<FieldData<?>> consumer) {
        FieldData<?> field = nameToField.get(key);
        if(field != null && field.matchesStaticness(isStatic)) {
            consumer.accept(field);
        }
    }

    /**
     * Returns the persistent {@link com.vicious.persist.mappify.reflect.PathFieldData} instance for the context.
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

    public int getStaticIDX(boolean isStatic){
        return isStatic ? 1 : 0;
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

    /**
     * Gets the transformer version for the class type.
     * @return an int representing the transformer version.
     */
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
            nameToField.forEach((name, field) -> {
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
            nameToField.forEach((name, field) -> {
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

    /**
     * Gets a Persist Field instance.
     * @param targetField the name of the field to retrieve.
     * @return the target field or null.
     */
    public @Nullable FieldData<?> getField(String targetField) {
        return nameToField.get(targetField);
    }

    /**
     * Gets all the keys for the field provided
     * @param field the field to search for.
     * @return a Set containing 0 or more keys for the field.
     */
    public @NotNull Set<String> getKeysOfField(@NotNull FieldData<?> field){
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, FieldData<?>> entry : nameToField.entrySet()) {
            if(entry.getValue() == field){
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
    /**
     * Gets a copy of the class' required fields.
     * @return all required fields
     */
    public Set<FieldData<?>> copyRequired(Context context) {
        return new HashSet<>(requiredFields[getStaticIDX(context.isStatic)]);
    }

    /**
     * Gets the custom constructor for this type if present.
     * @return a custom constructor or null.
     * @since 1.4.2
     */
    public @Nullable Initializers.CustomConstructor<?> getInitializer() {
        return initializer;
    }

    /**
     * Checks that there is a custom initializer generated for this type.
     * @return true if there is an initializer available
     * @since 1.4.2
     */
    public boolean hasInitializer() {
        return initializer != null;
    }
}
