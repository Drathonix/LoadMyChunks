package persist.shortcuts;

import com.vicious.persist.shortcuts.NotationFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Migrator {
    public static boolean migrate(com.vicious.persist.shortcuts.NotationFormat format, String fileName) {
        try {
            //TODO: this is a temp work-around, its not clean.
            String pathFileName=Paths.get(fileName).getFileName().toString();
            String filenameWithoutExtension = pathFileName.substring(0, pathFileName.lastIndexOf('.'));
            File directory = new File(fileName).getParentFile();
            File[] options = directory.listFiles();
            if(options != null) {
                for (File option : options) {
                    if(option.getName().startsWith(filenameWithoutExtension)) {
                        for (com.vicious.persist.shortcuts.NotationFormat value : NotationFormat.values()) {
                            if (value.isValidFile(option.getName())) {
                                Map<String, Object> map = value.parser.mappify(Files.newInputStream(option.toPath()));
                                option.delete();
                                format.writer.write(map, Files.newOutputStream(new File(fileName).toPath()));
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
