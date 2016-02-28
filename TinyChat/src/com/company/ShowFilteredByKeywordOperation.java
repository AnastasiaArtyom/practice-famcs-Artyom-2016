package com.company;

import java.io.IOException;

public class ShowFilteredByKeywordOperation implements AbstractOperation {
    private static final String OPTION_NAME =
            "Show messages filtered by keyword (case insensetive, should be fully present in message)";

    @Override
    public String getName() {
        return OPTION_NAME;
    }

    @Override
    public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
        try {
            System.out.println("Enter the keyword:");
            String keyword = StringGetter.getStringFromUser();
            int numMessages = oldHistory.findByKeywordAndPrint(keyword);
            logger.info("Found " + numMessages + " messages containing \"" + keyword + "\"");
        } catch (IOException e) {
            logger.exception("IOException " + e);
        }
        return oldHistory;
    }
}
