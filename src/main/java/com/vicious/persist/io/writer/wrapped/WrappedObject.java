package com.vicious.persist.io.writer.wrapped;

public class WrappedObject {
    public final Object object;
    public final String comment;

    public WrappedObject(Object object) {
        this.object = object;
        comment = "";
    }

    public WrappedObject(Object object, String comment) {
        this.object = object;
        if(comment == null){
            comment = "";
        }
        this.comment = comment;
    }

    public static WrappedObject of(Object object) {
        return new WrappedObject(object);
    }

    public static WrappedObject of(Object object, String comment) {
        return new WrappedObject(object, comment);
    }

    public static Object unwrap(Object o){
        if(o instanceof WrappedObject){
            return ((WrappedObject) o).object;
        }
        else{
            return o;
        }
    }

    public static String unwrapComment(Object o){
        if(o instanceof WrappedObject){
            return ((WrappedObject) o).comment;
        }
        else{
            return "";
        }
    }

    public static WrappedObject nullified() {
        return of(null);
    }

    @Override
    public String toString() {
        String s = object instanceof String ? "\"" : (object instanceof Character ? "'" : "");
        return "(" + s + object.toString() + s + ")" + (!comment.isEmpty() ? "[" + comment + "]" : "");
    }
}
