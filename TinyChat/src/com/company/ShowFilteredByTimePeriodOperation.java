package com.company;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class ShowFilteredByTimePeriodOperation implements AbstractOperation {
    private static final String OPTION_NAME =
            "Show messages filtered by time period";

    @Override
    public String getName() {
        return OPTION_NAME;
    }

    @Override
    public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
        try {
            final int DIFFERENCE = 59999;
            System.out.println("Please, enter BEGIN of the time period:");
            long begin = askAndGetDateFromUser(false);
            System.out.println("Please, enter END of the time period:");
            long end = askAndGetDateFromUser(true) + DIFFERENCE;
            int numMessages = oldHistory.findByTimePeriodAndPrint(begin, end);
            logger.info("Found " + numMessages + " messages between " +
                    begin + " and " + end + " timestamps");
        } catch (IOException e) {
            logger.exception("IOexception " + e);

        }
        return oldHistory;
    }

    private long askAndGetDateFromUser(boolean isDayEnd) throws IOException {
        System.out.println("Enter the year:");
        int year = Integer.parseInt(StringGetter.getStringFromUser());
        System.out.println("Enter the month number:");
        int month = Integer.parseInt(StringGetter.getStringFromUser());
        System.out.println("Enter the day of month:");
        int day = Integer.parseInt(StringGetter.getStringFromUser());
        System.out.print("Enter the time in HH:MM format (default is ");
        if (isDayEnd) {
            System.out.print("23:59");
        } else {
            System.out.print("00:00");
        }
        System.out.println("):");
        String time = StringGetter.getStringFromUser();
        int hour, minute;
        if (time.equals("")) {
            if (isDayEnd) {
                hour = 23;
                minute = 59;
            } else {
                hour = 0;
                minute = 0;
            }
        } else {
            Scanner sc = new Scanner(time);
            sc.useDelimiter(":");
            hour = Integer.parseInt(sc.next());
            minute = Integer.parseInt(sc.next());
        }
        return new GregorianCalendar(year, month - 1, day, hour, minute).getTimeInMillis();
    }
}