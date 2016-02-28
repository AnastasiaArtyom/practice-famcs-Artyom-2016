package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SaveToFileOperation implements AbstractOperation {
    private static final String OPTION_NAME = "Save chat to file";

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
            oldHistory.writeToFile(fName);
        } catch (FileNotFoundException e) {
            logger.exception("We cannot find " + e);
        } catch (IOException e) {
            logger.exception("IOExceptin" + e);
        }
        return oldHistory;
    }

}
