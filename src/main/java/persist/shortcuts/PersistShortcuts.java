package persist.shortcuts;

import com.vicious.persist.io.writer.wrapped.WrappedObjectMap;
import com.vicious.persist.mappify.Context;
import com.vicious.persist.mappify.Mappifier;
import com.vicious.persist.shortcuts.Migrator;
import com.vicious.persist.shortcuts.NotationFormat;
import com.vicious.persist.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Map;

public class PersistShortcuts {
    public static void saveAsFile(Object obj){
        Context context = Context.of(obj);
        saveAsFile(context.getPersistentPathFormat(),obj, context.getPersistentPath());
    }

    public static void saveAsFile(com.vicious.persist.shortcuts.NotationFormat format, WrappedObjectMap map, String fileName) {
        try {
            FileUtil.resolve(fileName);
            FileOutputStream fos = new FileOutputStream(fileName);
            format.write(map,fos);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveAsFile(com.vicious.persist.shortcuts.NotationFormat format, Object obj, String fileName) {
        saveAsFile(format,Mappifier.DEFAULT.mappify(obj),fileName);
    }

    public static void readFromFile(Object obj){
        Context context = Context.of(obj);
        readFromFile(context.getPersistentPathFormat(),obj, context.getPersistentPath(),false,context.getPersistentPathMigrateMode());
    }

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

    public static void init(Object obj){
        readFromFile(obj);
        saveAsFile(obj);
    }
}
