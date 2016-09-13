package com.xdrop.passlock.io;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class TextInputOutput {

    private Console console = System.console();
    private PrintWriter pw;
    private Scanner scanner;
    private OutputStream out;

    public TextInputOutput(InputStream in, OutputStream out) {

        this.pw = new PrintWriter(new OutputStreamWriter(out,
                StandardCharsets.UTF_8));
        this.scanner = new Scanner(in);

        this.out = out;
    }

    public TextInputOutput() {

        this.pw = new PrintWriter(System.out);
        this.scanner = new Scanner(System.in);
        this.out = System.out;

    }

    public void write(String l) {

        pw.write(l);
        pw.flush();

    }

    public void writeln(String l) {

        pw.write(l + "\n");
        pw.flush();

    }

    public void writeSecureLn(byte[] entry){

        try {
            IOUtils.write(entry, out);
        } catch (IOException ignored) {}

    }

    public char[] getSecure() {

        return console.readPassword();

    }

    public String getLine() {

        return scanner.nextLine();

    }

}
