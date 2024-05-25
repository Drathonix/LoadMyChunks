package com.vicious.loadmychunks.util;

public enum BoolEnum {
    ON{
        @Override
        public boolean asBoolean() {
            return true;
        }
    },
    OFF;

    public boolean asBoolean(){
        return false;
    }
}
