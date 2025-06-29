package com.vicious.persist.io.writer.gon;

import com.vicious.persist.io.writer.Separation;
import com.vicious.persist.mappify.registry.Stringify;
import com.vicious.persist.except.WriterException;
import com.vicious.persist.io.writer.wrapped.WrappedObject;
import com.vicious.persist.io.writer.IWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class GONWriter implements IWriter {
    public static final GONWriter DEFAULT = new GONWriter();
    public static final GONWriter PRETTY_JSON = new GONWriter()
            .quoteStrings(true)
            .quoteChars(true)
            .quoteNames(true)
            .charQuote('"')
            .nameValueSeparator(':')
            .includeMapBrackets(true)
            .writeComments(false)
            .equalsEntryPadding(0)
            .alwaysWriteCommaBetweenEntries(true);
    public static final GONWriter UGLY = new GONWriter(PRETTY_JSON)
            .useListValueSeparator(Separation.COMMA)
            .useMapEntrySeparator(Separation.COMMA)
            .putEndingBracketsOnNewline(false);

    public static final GONWriter JSON5 = new GONWriter(PRETTY_JSON)
            .writeComments(true)
            .useMultilineComments(false);

    private Separation listValueSeparator = Separation.NEWLINE;
    private Separation mapEntrySeparator = Separation.NEWLINE;
    private boolean alwaysWriteCommaBetweenEntries = false;
    private boolean putEndingBracketsOnNewline = true;
    private int tabWidth = 1;
    private int equalsExitPadding = 1;
    private int equalsEntryPadding = 1;
    private int commaPadding = 1;
    private boolean stringsAutomaticallyQuoted = true;
    private boolean quoteNames = true;
    private boolean charsAutomaticallyQuoted = true;
    private int commentLineWrap = 72;
    private boolean writeComments = true;
    private boolean useMultilineComments = true;
    private char charQuote = '\'';
    private char nameValueSeparator = '=';
    private boolean includeMapBrackets = false;

    public GONWriter() {}

    public GONWriter(GONWriter original) {
        this.listValueSeparator=original.listValueSeparator;
        this.mapEntrySeparator=original.mapEntrySeparator;
        this.tabWidth=original.tabWidth;
        this.equalsExitPadding=original.equalsExitPadding;
        this.commaPadding=original.commaPadding;
        this.stringsAutomaticallyQuoted=original.stringsAutomaticallyQuoted;
        this.quoteNames=original.quoteNames;
        this.charsAutomaticallyQuoted=original.charsAutomaticallyQuoted;
        this.commentLineWrap=original.commentLineWrap;
        this.charQuote=original.charQuote;
        this.nameValueSeparator=original.nameValueSeparator;
        this.includeMapBrackets=original.includeMapBrackets;
        this.writeComments=original.writeComments;
        this.useMultilineComments=original.useMultilineComments;
        this.putEndingBracketsOnNewline =original.putEndingBracketsOnNewline;
        this.equalsEntryPadding=original.equalsEntryPadding;
        this.alwaysWriteCommaBetweenEntries=original.alwaysWriteCommaBetweenEntries;
    }

    public GONWriter alwaysWriteCommaBetweenEntries(boolean b){
        this.alwaysWriteCommaBetweenEntries=b;
        return this;
    }

    public GONWriter quoteNames(boolean quoteNames){
        this.quoteNames=quoteNames;
        return this;
    }
    public GONWriter includeMapBrackets(boolean b){
        this.includeMapBrackets=b;
        return this;
    }

    private GONWriter putEndingBracketsOnNewline(boolean b) {
        this.putEndingBracketsOnNewline=b;
        return this;
    }

    public GONWriter useMultilineComments(boolean b){
        this.useMultilineComments=b;
        return this;
    }

    public GONWriter equalsEntryPadding(int a){
        this.equalsEntryPadding=a;
        return this;
    }

    public GONWriter writeComments(boolean b){
        this.writeComments=b;
        return this;
    }

    public GONWriter quoteStrings(boolean setting) {
        this.stringsAutomaticallyQuoted=setting;
        return this;
    }

    private GONWriter charQuote(char c) {
        this.charQuote=c;
        return this;
    }
    private GONWriter nameValueSeparator(char c) {
        this.nameValueSeparator=c;
        return this;
    }

    public GONWriter quoteChars(boolean setting) {
        this.charsAutomaticallyQuoted=setting;
        return this;
    }

    public GONWriter useListValueSeparator(@NotNull Separation separation) {
        listValueSeparator=separation;
        return this;
    }

    public GONWriter useMapEntrySeparator(@NotNull Separation separation) {
        mapEntrySeparator=separation;
        return this;
    }

    public GONWriter useCommentLineWrap(int length) {
        commentLineWrap=length;
        return this;
    }

    public GONWriter useTabWidth(int width) {
        tabWidth=width;
        return this;
    }

    public GONWriter equalsExitPadding(int padding) {
        this.equalsExitPadding = padding;
        return this;
    }

    public GONWriter commaPadding(int padding) {
        this.commaPadding=padding;
        return this;
    }

    @Override
    public void write(@NotNull Map<? extends Object, Object> map, @NotNull OutputStream out) {
        if(includeMapBrackets) {
            try {
                writeValue(map, out, 0);
            } catch (IOException e) {
                throw new WriterException("Failed to write",e);
            }
        }
        else {
            write(map, out, 0);
        }
    }

    public void write(Map<? extends Object, Object> map, OutputStream out, int depth) {
        Iterator<?> keys = map.keySet().iterator();
        while(keys.hasNext()){
            Object k = keys.next();
            Object v = map.get(k);
            try {
                Object value = WrappedObject.unwrap(v);
                if(writeComments) {
                    String comment = WrappedObject.unwrapComment(v);
                    if (!comment.isEmpty()) {
                        writeComment(out, comment, depth);
                    }
                }
                if(mapEntrySeparator == Separation.COMMA && (value instanceof Collection || value instanceof Map)){
                    out.write('\n');
                }
                if(mapEntrySeparator==Separation.NEWLINE || value instanceof Map || value instanceof Collection) {
                    tabs(out,depth*tabWidth);
                }
                writeName(k, out);
                writeChar(out,' ',equalsEntryPadding);
                out.write(nameValueSeparator);
                writeChar(out,' ',equalsExitPadding);
                writeValue(value,out,depth);
                if(keys.hasNext() && (mapEntrySeparator == Separation.COMMA || alwaysWriteCommaBetweenEntries)){
                    out.write(',');
                    writeChar(out,' ',commaPadding);
                }
                if(mapEntrySeparator==Separation.NEWLINE || value instanceof Map || value instanceof Collection) {
                    out.write('\n');
                }
            } catch (IOException e) {
                throw new WriterException("Failed to write.",e);
            }
        };
    }

    protected void writeName(Object value, OutputStream out) throws IOException {
        boolean insertQuotes = quoteNames;
        String str = Stringify.stringify(value);
        if(insertQuotes){
            insertQuotes = !str.endsWith("\"") && !str.startsWith("\"");
        }
        if(insertQuotes){
            out.write('\"');
        }
        out.write(Stringify.stringify(value).getBytes(StandardCharsets.UTF_8));
        if(insertQuotes){
            out.write('\"');
        }
    }

    @SuppressWarnings("unchecked")
    protected void writeValue(Object value, OutputStream out, int depth) throws IOException {
        if(value instanceof Map){
            out.write('{');
            if(mapEntrySeparator==Separation.NEWLINE && !((Map<?,?>) value).isEmpty()) {
                out.write('\n');
            }
            write((Map<Object, Object>) value,out,depth+1);
            if(mapEntrySeparator==Separation.NEWLINE && !((Map<?,?>) value).isEmpty()){
                tabs(out,tabWidth*depth);
            }
            out.write('}');
            return;
        }
        if(value instanceof Collection){
            out.write('[');
            if(listValueSeparator==Separation.NEWLINE && !((Collection<?>) value).isEmpty()) {
                out.write('\n');
            }
            writeCollection((Collection<Object>)value,out,depth+1);
            if(listValueSeparator==Separation.NEWLINE && !((Collection<?>) value).isEmpty()){
                tabs(out,tabWidth*depth);
            }
            out.write(']');
            return;
        }
        if(value == null){
            out.write("null".getBytes(StandardCharsets.UTF_8));
            return;
        }
        String stringifiedObject = Stringify.stringify(value);
        if(stringsAutomaticallyQuoted && !shouldQuote(value) && !stringifiedObject.startsWith("\"") && !stringifiedObject.endsWith("\"")){
            stringifiedObject = "\""+stringifiedObject+"\"";
        }
        if(charsAutomaticallyQuoted && value instanceof Character){
            stringifiedObject = charQuote+stringifiedObject+charQuote;
        }
        out.write(stringifiedObject.getBytes(StandardCharsets.UTF_8));
    }
    private boolean shouldQuote(Object object){
        return object instanceof Number || object instanceof Boolean || object instanceof Character;
    }

    private void tabs(OutputStream out, int n) throws IOException {
        writeChar(out,'\t',n);
    }

    private void writeCollection(Collection<Object> collection, OutputStream out, int depth) {
        Iterator<?> values = collection.iterator();
        while(values.hasNext()){
            Object v = values.next();
            try {

                Object value = WrappedObject.unwrap(v);
                String comment = WrappedObject.unwrapComment(v);
                if(!comment.isEmpty()){
                    writeComment(out,comment,depth);
                }
                if(listValueSeparator == Separation.COMMA && (value instanceof Collection || value instanceof Map)){
                    out.write('\n');
                }
                if(listValueSeparator==Separation.NEWLINE || value instanceof Map || value instanceof Collection) {
                    writeChar(out,'\t',depth*tabWidth);
                }
                writeValue(value,out,depth);
                if(values.hasNext() && (listValueSeparator == Separation.COMMA || alwaysWriteCommaBetweenEntries)){
                    out.write(',');
                    writeChar(out,' ',commaPadding);
                }
                if(listValueSeparator==Separation.NEWLINE) {
                    out.write('\n');
                }
            } catch (IOException e) {
                throw new WriterException("Failed to write.",e);
            }
        };
    }

    protected void writeComment(OutputStream out, String comment, int depth) throws IOException {
        int lines = comment.length()/commentLineWrap+1;
        int start = 0;
        int end;
        tabs(out,depth*tabWidth);
        boolean isMultiLine = useMultilineComments && lines > 1;
        if(isMultiLine){
            out.write("/* ".getBytes(StandardCharsets.UTF_8));
        }
        for (int i = 0; i < lines; i++) {
            end = Math.min(comment.length(),(i+1)*commentLineWrap);
            if(i < lines-1) {
                int j = end >= comment.length() ? comment.length() - 1 : end;
                while (j >= 0) {
                    if (Character.isWhitespace(comment.charAt(j))) {
                        end = j;
                        break;
                    }
                    j--;
                }
            }
            String line = comment.substring(start, end);
            if(i == lines - 1 && isMultiLine) {
                line += "*/";
            }
            start = end;
            if(i > 0) {
                tabs(out, depth * tabWidth);
            }
            if(!isMultiLine){
                out.write("// ".getBytes(StandardCharsets.UTF_8));
            }
            else if(i > 0){
                out.write(" * ".getBytes(StandardCharsets.UTF_8));
            }
            out.write(line.trim().getBytes(StandardCharsets.UTF_8));
            out.write('\n');
        }
    }

    protected void writeChar(OutputStream out, char c, int times) throws IOException {
        for (int i = 0; i < times; i++) {
            out.write(c);
        }
    }
}
