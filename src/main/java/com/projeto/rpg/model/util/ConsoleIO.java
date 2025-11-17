package com.projeto.rpg.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.projeto.rpg.interfaces.*;

public class ConsoleIO implements IO {

    private final BufferedReader leitor = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String lerLinha() throws IOException {
        return leitor.readLine();
    }

    @Override
    public void println(String msg) {
        System.out.println(msg);
    }

    @Override
    public void print(String msg) {
        System.out.print(msg);
    }

    @Override
    public void printf(String formato, Object... args) {
        System.out.printf(formato, args);
    }
}
