package com.vicious.persist.io.parser;

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

        @Override
        public Class<?> getType() {
            return double.class;
        }
    },
    BOOLEAN{
        @Override
        public AssumedType append(char c) {
            String booleanChars = "TtRrUuEeFfAaLlSs";
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

    public Class<?> getType(){
        return String.class;
    }

    public abstract AssumedType append(char c);
}
