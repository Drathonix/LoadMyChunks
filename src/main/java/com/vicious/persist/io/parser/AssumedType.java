package com.vicious.persist.io.parser;

/**
 * Represents a list of possible types that a parsed value can be.
 * @author Jack Andersen
 */
public enum AssumedType {
    UNKNOWN{
        @Override
        public AssumedType append(char c) {
            if(c == '"'){
                return STRING;
            }
            if(c == '\''){
                return CHAR;
            }
            if(Character.isDigit(c) || c == '-'){
                return INTEGER;
            }
            if(c == '.'){
                return DECIMAL;
            }
            if(BOOLEAN.append(c) == AssumedType.BOOLEAN){
                return BOOLEAN;
            }
            return STRING;
        }
    },
    INTEGER{
        @Override
        public AssumedType append(char c) {
            if(Character.isDigit(c)){
                return this;
            }
            if(c == '.'){
                return DECIMAL;
            }
            else return STRING;
        }

        @Override
        public Class<?> getType() {
            return long.class;
        }
    },
    DECIMAL{
        @Override
        public AssumedType append(char c) {
            if(Character.isDigit(c) || c == '.' || c == 'E'){
                return this;
            }
            else return STRING;
        }

        /**
         * Returns the double type class.
         * @return double.class
         */
        @Override
        public Class<?> getType() {
            return double.class;
        }
    },
    BOOLEAN{
        final String booleanChars = "TtRrUuEeFfAaLlSs";
        @Override
        public AssumedType append(char c) {
            if(booleanChars.indexOf(c) >= 0){
                return this;
            }
            else{
                return STRING;
            }
        }

        @Override
        public Class<?> getType() {
            return boolean.class;
        }
    },
    CHAR {
        public AssumedType append(char c) {
            return CHAR;
        }

        @Override
        public Class<?> getType() {
            return char.class;
        }
    },
    STRING{
        @Override
        public AssumedType append(char c) {
            return STRING;
        }
    };

    /**
     * Gives the assumed class of the parsed value.
     * @return the expected type.
     */
    public Class<?> getType(){
        return String.class;
    }

    /**
     * Appends a char and recalculates the assumed type if necessary.
     * @param c the char
     * @return the new AssumedType.
     */
    public abstract AssumedType append(char c);
}
