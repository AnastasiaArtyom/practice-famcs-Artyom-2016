package by.bsu.up.chat.logging;


import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LogsStorage {
    private Calendar calendar;
    private PrintStream ps;
    private static volatile LogsStorage instance = new LogsStorage();
    private final String DESIGN_OUTPUT = "%-30s %-10s %-140s%n";
    private final  String FILE_NAME = "logFile.txt";

    private LogsStorage() {
        calendar = new GregorianCalendar();
        try {
            ps = new PrintStream(FILE_NAME);
        } catch (FileNotFoundException e) {
            System.out.println("the path for the log was not found");
        }
    }


    public static LogsStorage getInstance() {
        return instance;
    }

    public void addException(String information) {
        ps.printf(DESIGN_OUTPUT, calendar.getTime().toString(), "Exception", information);
    }

    public void addInformation(String information) {
        ps.printf(DESIGN_OUTPUT, calendar.getTime().toString(), "Information", information);
    }

    public void addWarning(String information) {
        ps.printf(DESIGN_OUTPUT, calendar.getTime().toString(), "Warning", information);
    }


}