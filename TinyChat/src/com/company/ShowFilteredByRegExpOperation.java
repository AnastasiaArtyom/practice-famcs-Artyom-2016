package com.company;

import java.io.IOException;

public class ShowFilteredByRegExpOperation implements AbstractOperation {
    private static final String OPTION_NAME = "Show messages filtered by regular expression";

    @Override
    public String getName() {
        return OPTION_NAME;
    }

    @Override
    public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
        try {
            System.out.println("Enter your regular expression:");
            String regExp = StringGetter.getStringFromUser();
            int numMessages = oldHistory.findByRegExpAndPrint(regExp);
            logger.info("Found " + numMessages + " messages matching \"" + regExp + "\"");
        } catch (IOException e) {
            logger.exception("IOExceptin " + e);
        }
        return oldHistory;
    }
}
