package com.vicious.persist.shortcuts;

import com.vicious.persist.io.parser.IParser;
import com.vicious.persist.io.parser.gon.GONParser;
import com.vicious.persist.io.writer.IWriter;
import com.vicious.persist.io.writer.gon.GONWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

//TODO: change this to a registry-type system.
/**
 * An enum representing an arbitrary notation format with a predetermined writer and parser configuration.
 * @author Jack Andersen
 */
public enum NotationFormat {
    /**
     * A format I made for fun. It probably already exists in a different name.
     * GON stands for Generic Object Notation and represents data extremely similar to JSON.
     * Conveniently the GON parser can distinguish both formats.
     * I had added GON before JSON and JSON5 so unfortunately there are some projects of mine that use .txt and .gon interchangeably.
     */
    GON(GONWriter.DEFAULT, GONParser.DEFAULT,".txt",".gon"),
    /**
     * JSON standard.
     * does not support comments.
     */
    JSON(GONWriter.UGLY, GONParser.DEFAULT,"json"),
    /**
     * JSON5 standard.
     * supports comments.
     */
    JSON5(GONWriter.JSON5, GONParser.DEFAULT,"json5");

    public final IWriter writer;
    public final IParser parser;
    private final Set<String> validExtensions = new HashSet<>();

    NotationFormat(IWriter writer, IParser parser, String... validExtensions) {
        this.writer = writer;
        this.parser = parser;
        this.validExtensions.addAll(Arrays.asList(validExtensions));
    }

    /**
     * Checks if a file's extension is supported by the format.
     * @param file the file's name.
     * @return whether the file extension is supported.
     */
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
