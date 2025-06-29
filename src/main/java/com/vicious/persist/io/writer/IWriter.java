package com.vicious.persist.io.writer;

import java.io.OutputStream;
import java.util.Map;

public interface IWriter {
    void write(Map<? extends Object, Object> map, OutputStream out);
}
