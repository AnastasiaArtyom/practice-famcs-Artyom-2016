package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AddMessageOperation implements AbstractOperation {

    private static final String OPTION_NAME = "Add message";

    @Override
    public String getName() {
        return OPTION_NAME;
    }

    @Override
    public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
        final int MAX_MESSAGE_SIZE = 140;
        try {
            System.out.println("Enter the author of your message:");
            String author = StringGetter.getStringFromUser();
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
