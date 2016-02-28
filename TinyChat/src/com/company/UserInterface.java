package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UserInterface {

    private ChatHistory chatHistory;
    private Logger logger;

    private final AbstractOperation kOptions[] =
            {new LoadFileOperation(), new SaveToFileOperation(), new AddMessageOperation(),
                    new DeleteMessageByIDOperation(), new ShowAllInTimeOrderOperation(),
                    new ShowFilteredByAuthorOperation(), new ShowFilteredByKeywordOperation(),
                    new ShowFilteredByRegExpOperation(), new ShowFilteredByTimePeriodOperation()
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
                int optionNum = Integer.parseInt(StringGetter.getStringFromUser());
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
