package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.Scanner;


public class UserInterface {

    private ChatHistory chatHistory;
    private Logger logger;

    static String getStringFromUser() throws IOException {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    private abstract class AbstractOperation {
        public abstract String getName();

        public abstract ChatHistory perform(ChatHistory oldHistory, Logger logger);
    }

    private class LoadFile extends AbstractOperation {
        @Override
        public String getName() {
            return "Load chat from file";
        }

        @Override
        public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
            System.out.println("Please, input name of the file (default is base.json):");
            try {
                String fName = getStringFromUser();
                if (fName.equals("")) {
                    fName = "base.json";
                }
                oldHistory.readFromFile(fName);
                logger.info(oldHistory.getStatistics());
            } catch (FileNotFoundException e) {
                //System.err.println("We cannot find this file");
                logger.exception("FileNotFoundException" + e);
            } catch (IOException e) {
                //System.err.println("Something were wrong");
                logger.exception("IOException" + e);
            }
            return oldHistory;
        }
    }

    private class SaveToFile extends AbstractOperation {
        @Override
        public String getName() {
            return "Save chat to file";
        }

        @Override
        public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
            System.out.println("Please, input name of the file (default is base.json):");
            try {
                String fName = getStringFromUser();
                if (fName.equals("")) {
                    fName = "base.json";
                }
                oldHistory.writeToFile(fName);
            } catch (FileNotFoundException e) {
                logger.exception("We cannot find " + e);
            } catch (IOException e) {
                logger.exception("IOExceptin" + e);
            }
            return oldHistory;
        }

    }

    private class AddMessage extends AbstractOperation {
        @Override
        public String getName() {
            return "Add message";
        }

        @Override
        public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
            final int MAX_MESSAGE_SIZE = 140;
            try {
                System.out.println("Enter the author of your message:");
                String author = getStringFromUser();
                System.out.println("Enter your message. End it with two blank lines:");
                String message = getMessageFromUser();
                if (message.length() >= MAX_MESSAGE_SIZE) {
                    logger.warning("Entered a large message (" + message.length() + " characters)");
                }
                oldHistory.addMessage(author, message);
                logger.info(oldHistory.getStatistics());
            } catch (IOException e) {
                logger.exception("IOExceptin" + e);
            }
            return oldHistory;
        }

        private String getMessageFromUser() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String previousLine = "a";
            String currentLine = reader.readLine();
            StringBuilder entireMessage = new StringBuilder();
            while (!(previousLine.equals("") && currentLine.equals(""))) {
                entireMessage.append(currentLine);
                entireMessage.append("\n");
                previousLine = currentLine;
                currentLine = reader.readLine();
            }
            return entireMessage.substring(0, entireMessage.length() - 2);
        }

    }

    private class DeleteMessageByID extends AbstractOperation {
        @Override
        public String getName() {
            return "Delete message by ID";
        }

        @Override
        public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
            try {
                System.out.println("Please, enter ID of the message you want to delete:");
                String id = getStringFromUser();
                logger.info("Trying to delete message with ID=\"" + id + "\"");
                if (oldHistory.deleteById(id)) {
                    System.out.println("Message found and deleted.");
                    logger.info("Message with id " + id + "found and deleted.");
                    logger.info(oldHistory.getStatistics());
                } else {
                    System.out.println("Message with such ID does not exist");
                    logger.warning("Message with id " + id + "does not exist");
                }
            } catch (IOException e) {
                logger.exception("IOExcetin" + e);
            }
            return oldHistory;
        }
    }

    private class ShowAllInTimeOrder extends AbstractOperation {
        @Override
        public String getName() {
            return "Show all messages in chronological order";
        }

        @Override
        public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
            oldHistory.consolePrint();
            return oldHistory;
        }
    }

    private class ShowFilteredByAuthor extends AbstractOperation {
        @Override
        public String getName() {
            return "Show messages filtered by author";
        }

        @Override
        public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
            try {
                System.out.println("Enter the author:");
                String author = getStringFromUser();
                int numMessages = oldHistory.findByAuthorAndPrint(author);
                logger.info("Found " + numMessages + " messages by " + author);
            } catch (IOException e) {
                logger.exception("IOException" + e);
            }
            return oldHistory;
        }
    }

    private class ShowFilteredByKeyword extends AbstractOperation {
        @Override
        public String getName() {
            return "Show messages filtered by keyword (case insensetive, should be fully present in message)";
        }

        @Override
        public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
            try {
                System.out.println("Enter the keyword:");
                String keyword = getStringFromUser();
                int numMessages = oldHistory.findByKeywordAndPrint(keyword);
                logger.info("Found " + numMessages + " messages containing \"" + keyword + "\"");
            } catch (IOException e) {
                logger.exception("IOException " + e);
            }
            return oldHistory;
        }
    }

    private class ShowFilteredByRegExp extends AbstractOperation {
        @Override
        public String getName() {
            return "Show messages filtered by regular expression";
        }

        @Override
        public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
            try {
                System.out.println("Enter your regular expression:");
                String regExp = getStringFromUser();
                int numMessages = oldHistory.findByRegExpAndPrint(regExp);
                logger.info("Found " + numMessages + " messages matching \"" + regExp + "\"");
            } catch (IOException e) {
                logger.exception("IOExceptin " + e);
            }
            return oldHistory;
        }
    }

    private class ShowFilteredByTimePeriod extends AbstractOperation {
        @Override
        public String getName() {
            return "Show messages filtered by time period";
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
            int year = Integer.parseInt(getStringFromUser());
            System.out.println("Enter the month number:");
            int month = Integer.parseInt(getStringFromUser());
            System.out.println("Enter the day of month:");
            int day = Integer.parseInt(getStringFromUser());
            System.out.print("Enter the time in HH:MM format (default is ");
            if (isDayEnd) {
                System.out.print("23:59");
            } else {
                System.out.print("00:00");
            }
            System.out.println("):");
            String time = getStringFromUser();
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

    private final AbstractOperation kOptions[] =
            {new LoadFile(), new SaveToFile(), new AddMessage(),
                    new DeleteMessageByID(), new ShowAllInTimeOrder(),
                    new ShowFilteredByAuthor(), new ShowFilteredByKeyword(),
                    new ShowFilteredByRegExp(), new ShowFilteredByTimePeriod()
            };

    public UserInterface() {
        chatHistory = new ChatHistory();
        logger = new Logger();
        try {
            logger.setFile("logfile.txt");
            logger.info("Log started");
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: logfile.txt cannot be created");
        }
    }

    public void run() {
        showGeneralOptions();
        while (true) {
            showPrompt();
            try {
                int optionNum = Integer.parseInt(getStringFromUser());
                if (optionNum == 0) {
                    logger.info("Exiting now");
                    logger.close();
                    return;
                } else {
                    if (optionNum > 0 && optionNum <= kOptions.length) {
                        AbstractOperation operation = kOptions[optionNum - 1];
                        logger.info("User asked: " + operation.getName());
                        chatHistory = operation.perform(chatHistory, logger);
                        showGeneralOptions();
                    } else {
                        System.out.println("Option with such number does not exist. Please enter another number.");
                    }
                }
            } catch (IOException e) {
                logger.exception("IOException " + e);

            } catch (IllegalArgumentException e) {
                System.out.println("Got exception: " + e + ". Please, enter the valid data (ID etc)");
                logger.warning("Caught exception: " + e);
            }
        }
    }

    private void showGeneralOptions() {
        System.out.println("Available options:");
        for (int optionNum = 0; optionNum < kOptions.length; ++optionNum) {
            System.out.print(optionNum + 1);
            System.out.print(". ");
            System.out.println(kOptions[optionNum].getName());
        }
        System.out.println("0. Exit");
    }

    private void showPrompt() {
        System.out.println("Please, enter the option number.");
    }

}
