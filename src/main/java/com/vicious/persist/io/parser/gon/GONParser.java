package com.vicious.persist.io.parser.gon;

import com.vicious.persist.except.ParserException;
import com.vicious.persist.mappify.registry.Stringify;
import com.vicious.persist.io.parser.AssumedType;
import com.vicious.persist.io.parser.IParser;
import com.vicious.persist.io.parser.ParserBase;

public class GONParser extends ParserBase implements IParser {
    public static final GONParser DEFAULT = new GONParser();

    public boolean castValues = true;

    public GONParser() {}

    public GONParser autoCastValues(boolean castValues){
        this.castValues=castValues;
        return this;
    }

    @Override
    protected Object convertFromString(String value, AssumedType type) {
        if(type == AssumedType.CHAR && value.length() > 3){
            type = AssumedType.STRING;
        }
        if (type == AssumedType.STRING) {
            if(value.equalsIgnoreCase("null")){
                return null;
            }
            if(value.length() < 2){
                return value;
            }
            char f = value.charAt(0);
            char e = value.charAt(value.length() - 1);
            if ("'\"".contains("" + f) || "'\"".contains("" + e)) {
                return convertFromString(value.substring(1, value.length() - 1),type);
            }
        }
        if(!castValues) {
            if(type == AssumedType.CHAR){
                if(value.isEmpty()){
                    throw new ParserException("Cannot parse a char due to an empty string.");
                }
                return String.valueOf(value.length() > 1 ? value.charAt(1) : value.charAt(0));
            }
            return super.convertFromString(value, type);
        }
        else{
            if(type == AssumedType.CHAR){
                if(value.isEmpty()){
                    throw new ParserException("Cannot parse a char due to an empty string.");
                }
                return value.length() > 1 ? value.charAt(1) : value.charAt(0);
            }
            return Stringify.objectify(type.getType(),value);
        }
    }

    @Override
    protected ParserBase copy() {
        return new GONParser()
                .autoCastValues(this.castValues);
    }
}
