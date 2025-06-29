package com.vicious.persist.io.parser.enums;

public enum ValueType {
    STRING{
        @Override
        public boolean isValueEnder(char in) {
            return in == '\"';
        }

        @Override
        public ValueType append(char currentToken) {
            return this;
        }
    },
    CHAR{
        @Override
        public boolean isValueEnder(char in) {
            return in == '\'';
        }

        @Override
        public ValueType append(char currentToken) {
            return CHAR;
        }
    },
    GENERIC_INTEGER{
        @Override
        public boolean isValueEnder(char in) {
            return "bBsSiIlL".contains(Character.toString(in));
        }
        @Override
        public ValueType append(char currentToken) {
            if(Character.isDigit(currentToken)) {
                return this;
            }
            if(currentToken == '-') {
                return this;
            }
            if(currentToken == '.') {
                return GENERIC_DECIMAL;
            }
            return STRING;
        }
    },
    GENERIC_DECIMAL{
        @Override
        public boolean isValueEnder(char in) {
            return "fFdD".contains(Character.toString(in));
        }
        @Override
        public ValueType append(char currentToken) {
            if(Character.isDigit(currentToken)) {
                return this;
            }
            if(currentToken == '-') {
                return this;
            }
            if(currentToken == '.') {
                return this;
            }
            return STRING;
        }
    },
    BOOLEAN{
        @Override
        public ValueType append(char currentToken) {
            return this;
        }
    },
    OTHER{
        @Override
        public ValueType append(char currentToken) {
            if(currentToken == '"'){
                return STRING;
            }
            if(currentToken == '\''){
                return CHAR;
            }
            if(currentToken == 'T' || currentToken == 't' || currentToken == 'F' || currentToken == 'f') {
                return BOOLEAN;
            }
            if(Character.isDigit(currentToken) || currentToken == '-') {
                return GENERIC_INTEGER;
            }
            if(currentToken == '.'){
                return GENERIC_DECIMAL;
            }
            return STRING;
        }
    };

    public abstract ValueType append(char currentToken);

    public boolean isValueEnder(char in){
        return false;
    }
}
