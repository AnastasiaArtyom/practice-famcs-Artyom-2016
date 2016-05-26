package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.logging.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;


public class LogFile implements Logger {
    PrintStream ps;
    public LogFile(String fileName) throws FileNotFoundException {
        ps = new PrintStream(new File(fileName));
    }
    @Override
    public void info(String message) {
        ps.println( message);
    }

    @Override
    public void error(String message, Throwable e) {
        ps.println( "Error " + e.getMessage()+ " " + message);
    }
}