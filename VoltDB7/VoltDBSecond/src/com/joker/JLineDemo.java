package com.joker;


import jline.console.ConsoleReader;

import java.io.IOException;

/**
 * Created by hunter on 2018/2/7
 */
public class JLineDemo {
    public static void main(String[] args) throws IOException {
        //System.setProperty("jline.internal.Log.debug", "true");
        System.setProperty("jline.WindowsTerminal.directConsole", "false");
        //ConsoleReader reader = new ConsoleReader(new FileInputStream(FileDescriptor.in), System.out);
        ConsoleReader reader = new ConsoleReader();
        reader.setBellEnabled(false);
        getInteractiveQueries(reader);
        reader.close();
    }

    public static void getInteractiveQueries(ConsoleReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine("Enter password> ", '*');
            System.out.println("Got password: " + line);
        } while(line != null && line.length() > 0);
    }
}
