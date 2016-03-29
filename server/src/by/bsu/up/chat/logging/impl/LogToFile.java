package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class LogToFile implements Logger{
    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("M:d:yyyy HH:mm:ss");
    FileWriter log;
    String logFile;
    public LogToFile(String logFile) throws IOException{
        log = new FileWriter(logFile, true);
        this.logFile = logFile;
    }

    public void closeFile(){
        try {
            log.close();
        }catch(IOException e){

        }
    }

    @Override
    public void error(String message, Throwable e){
        try{
            String date = DATA_FORMAT.format(System.currentTimeMillis());
            StringBuilder bufferString = new StringBuilder(date);
            bufferString.append(" ");
            bufferString.append(message);
            bufferString.append("\r\n");
            log.write(bufferString.toString());
            log.write(date + " " + e.getMessage() + "\r\n");
        } catch(IOException ex){
            System.err.println("exception: " + ex.getMessage());
        }
    }


    @Override
    public void info(String message){
        try(FileWriter log = new FileWriter(logFile, true)){
            String date = DATA_FORMAT.format(System.currentTimeMillis());
            StringBuilder bufferString = new StringBuilder(date);
            bufferString.append(" ");
            bufferString.append(message);
            bufferString.append("\r\n");
            log.write(bufferString.toString());
        } catch(IOException e){
            System.err.println("exception: " + e.getMessage());
        }
    }



}
