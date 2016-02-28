package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadFileOperation implements AbstractOperation {

    private static final String OPTION_NAME = "Load chat from file";

    @Override
    public String getName() {
        return OPTION_NAME;
    }

    @Override
    public ChatHistory perform(ChatHistory oldHistory, Logger logger) {
        System.out.println("Please, input name of the file (default is base.json):");
        try {
            String fName = StringGetter.getStringFromUser();
            if (fName.equals("")) {
                fName = "base.json";
            }
            oldHistory.readFromFile(fName);
            logger.info(oldHistory.getStatistics());
        } catch (FileNotFoundException e) {
            logger.exception("FileNotFoundException" + e);
        } catch (IOException e) {
            logger.exception("IOException" + e);
        }
        return oldHistory;
    }
}
