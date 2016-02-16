package com.company;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Logger {
    private PrintStream outStream;

    public void setFile(String filename) throws FileNotFoundException{
        outStream = new PrintStream(filename);
    }

    public void warning(String text){
        if(outStream != null){
            outStream.println("WARNING: " + text);
        }
    }

    public void exception(String text){
        if(outStream != null){
            outStream.println("EXCEPTION: " + text);
        }
    }


    public void info(String text){
        if(outStream != null){
            outStream.println("INFO: " + text);
        }
    }

    public void close(){
        outStream.close();
    }

}
