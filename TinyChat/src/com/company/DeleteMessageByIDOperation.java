package com.company;

import java.io.IOException;

public class DeleteMessageByIDOperation implements AbstractOperation {
    private static final String OPTION_NAME = "Delete message by ID";
    @Override
    public String getName() {
        return OPTION_NAME;
    }

    @Override
    public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
        try {
            System.out.println("Please, enter ID of the message you want to delete:");
            String id = StringGetter.getStringFromUser();
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
