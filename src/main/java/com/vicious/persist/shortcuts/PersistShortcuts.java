package com.vicious.persist.shortcuts;

import com.vicious.persist.except.NoSuchSavableElementException;
import com.vicious.persist.io.writer.wrapped.WrappedObject;
import com.vicious.persist.io.writer.wrapped.WrappedObjectMap;
import com.vicious.persist.mappify.Context;
import com.vicious.persist.mappify.Mappifier;
import com.vicious.persist.mappify.reflect.ClassData;
import com.vicious.persist.mappify.reflect.FieldData;
import com.vicious.persist.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Map;

/**
 * A utility class that provides basic file read and write methods for persistent data.
 * @author Jack Andersen
 */
public class PersistShortcuts {
    /**
     * Saves an object to a path indicated by the appropriate String Field marked with {@link com.vicious.persist.annotations.PersistentPath}
     * @param obj the object to save. Class instances will be considered "static" objects.
     */
    public static void saveAsFile(Object obj){
        Context context = Context.of(obj);
        saveAsFile(context.getPersistentPathFormat(),obj, context.getPersistentPath());
    }

    /**
     * Saves a map to the path provided using the requested notation format.
     * @param format the notation format to use
     * @param map the map storing objects and possibly comments as well
     * @param fileName the relative file path to write to.
     */
    public static void saveAsFile(NotationFormat format, WrappedObjectMap map, String fileName) {
        try {
            FileUtil.resolve(fileName);
            FileOutputStream fos = new FileOutputStream(fileName);
            format.write(map,fos);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves an object to the path provided using the requested notation format.
     * @param format the notation format to use
     * @param obj the object to mappify. Class instances will be considered "static" objects.
     * @param fileName the relative file path to write to.
     */
    public static void saveAsFile(NotationFormat format, Object obj, String fileName) {
        saveAsFile(format,Mappifier.DEFAULT.mappify(obj),fileName);
    }

    /**
     * Reads an object from a path indicated by the appropriate String Field marked with {@link com.vicious.persist.annotations.PersistentPath}
     * @param obj the object to apply changes to. Class instances will be considered "static" objects.
     */
    public static void readFromFile(Object obj){
        Context context = Context.of(obj);
        readFromFile(context.getPersistentPathFormat(),obj, context.getPersistentPath(),false,context.getPersistentPathMigrateMode());
    }

    /**
     * Reads an object from the file specified.
     * @param format the file's expected notation format
     * @param obj the object to apply changes to. Class instances will be considered "static" objects.
     * @param fileName the file's relative path
     * @param throwOnNoSuchFile if true, a {@link java.nio.file.NoSuchFileException} will be thrown when the file is not present.
     * @param migrate if true, old files will be migrated to the new file if the new file is not present.
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    public static void readFromFile(NotationFormat format, Object obj, String fileName, boolean throwOnNoSuchFile, boolean migrate) {
        try {
            File file = new File(fileName);
            if(file.exists() || (migrate && Migrator.migrate(format, fileName))){
                FileInputStream fis = new FileInputStream(fileName);
                Map<String,Object> map = format.parse(fis);
                fis.close();
                Mappifier.DEFAULT.unmappify(obj,(Map)map);
            }
            else if(throwOnNoSuchFile){
                throw new NoSuchFileException(fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A shortcut method to initialize an object from the path indicated by the appropriate String Field marked with {@link com.vicious.persist.annotations.PersistentPath}
     * First reads the file if it exists.
     * Secondly saves the object to file, overwriting the old one if it is present.
     * @param obj the initialization target.
     */
    public static void init(Object obj){
        readFromFile(obj);
        saveAsFile(obj);
    }

    /**
     * A shortcut method to set a value while using Persist's built-in value validator.
     * @param target the object that contains the value.
     * @param value the new value to set.
     * @param targetField the name of the field to modify.
     * @author Jack Andersen
     * @since 1.3.0
     */
    public static void set(@NotNull Object target, @Nullable Object value, @NotNull String targetField) {
        Context ctx = Context.of(target);
        FieldData<?> data = ctx.getField(targetField);
        if(data != null){
            data.set(ctx,value);
        }
        else{
            throw new NoSuchSavableElementException(targetField + " is not present in the target object.");
        }
    }
}
