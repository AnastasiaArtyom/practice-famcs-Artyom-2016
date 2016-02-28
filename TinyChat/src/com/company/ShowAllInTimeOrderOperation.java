package com.company;

public class ShowAllInTimeOrderOperation implements AbstractOperation {
    private static final String OPTION_NAME = "Show all messages in chronological order";

    @Override
    public String getName() {
        return OPTION_NAME;
    }

    @Override
    public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
        oldHistory.consolePrint();
        return oldHistory;
    }
}
