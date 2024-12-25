package persist.shortcuts;

import com.vicious.persist.io.parser.IParser;
import com.vicious.persist.io.parser.gon.GONParser;
import com.vicious.persist.io.writer.IWriter;
import com.vicious.persist.io.writer.gon.GONWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum NotationFormat {
    GON(GONWriter.DEFAULT, GONParser.DEFAULT,".txt",".gon"),
    JSON(GONWriter.UGLY, GONParser.DEFAULT,"json"),
    JSON5(GONWriter.JSON5, GONParser.DEFAULT,"json5");

    public final IWriter writer;
    public final IParser parser;
    private final Set<String> validExtensions = new HashSet<>();

    NotationFormat(IWriter writer, IParser parser, String... validExtensions) {
        this.writer = writer;
        this.parser = parser;
        this.validExtensions.addAll(Arrays.asList(validExtensions));
    }

    public boolean isValidFile(String file){
        for (String validExtension : validExtensions) {
            if(file.endsWith(validExtension)){
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void write(Map<?,?> map, OutputStream out) {
        writer.write((Map<Object,Object>)map,out);
    }

    public Map<String,Object> parse(InputStream in) {
        return parser.mappify(in);
    }
}
