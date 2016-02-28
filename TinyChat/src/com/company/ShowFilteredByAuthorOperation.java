package com.company;

import java.io.IOException;

public class ShowFilteredByAuthorOperation implements AbstractOperation {
    private static final String OPTION_NAME = "Show messages filtered by author";

    @Override
    public String getName() {
        return OPTION_NAME;
    }

    @Override
    public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
        try {
            System.out.println("Enter the author:");
            String author = StringGetter.getStringFromUser();
            int numMessages = oldHistory.findByAuthorAndPrint(author);
            logger.info("Found " + numMessages + " messages by " + author);
        } catch (IOException e) {
            logger.exception("IOException" + e);
        }
        return oldHistory;
    }
}
