package com.projeto.rpg.interfaces;

import java.io.IOException;


public interface IO {
    void println(String msg);
    void print(String msg);
    String lerLinha() throws IOException;
    void printf(String formato, Object... args);
}

