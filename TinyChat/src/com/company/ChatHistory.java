package com.company;

import javax.json.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.*;
import java.io.File;

public class ChatHistory {

    public ChatHistory() {
        numItems = 0;
        numItemsAdded = 0;
        numItemsDeleted = 0;
        chat = new TreeSet<>(new Comparator<HistoryItem>() {
            @Override
            public int compare(HistoryItem first, HistoryItem second) {
                return first.getId().compareTo(second.getId());
            }
        });
    }

    public void readFromFile(String filename) throws FileNotFoundException {
        String content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
        JsonReader reader = Json.createReader(new StringReader(content));
        JsonArray items = reader.readArray();
        reader.close();
        for (JsonValue item : items) {
            chat.add(new HistoryItem().parseFromJson((JsonObject) item));
            ++numItems;
        }
    }

    public void writeToFile(String filename) throws FileNotFoundException {
        PrintStream outStream = new PrintStream(filename);
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (HistoryItem item : chat) {
            builder = builder.add(item.getJson());
        }
        outStream.println(builder.build());
        outStream.close();
    }

    public void consolePrint() {
        for (HistoryItem item : sortByTimestamp(getAllItemsAsArrayList())) {
            System.out.println(item.printPretty(new StringBuilder()).toString());
        }
    }

    public int findByAuthorAndPrint(final String author) {
        int counter = 0;
        for (HistoryItem item : sortByTimestamp(getFilteredBy(new Predicate() {
            @Override
            public boolean condition(HistoryItem item) {
                return item.getAuthor().equals(author);
            }
        }))) {
            System.out.println(item.printPretty(new StringBuilder()).toString());
            ++counter;
        }
        return counter;
    }

    public int findByRegExpAndPrint(final String regExp) {
        int counter = 0;
        for (HistoryItem item : sortByTimestamp(getFilteredBy(new Predicate() {
            @Override
            public boolean condition(HistoryItem item) {
                return item.getMessage().matches(regExp);
            }
        }))) {
            System.out.println(item.printPretty(new StringBuilder()).toString());
            ++counter;
        }
        return counter;
    }

    public int findByTimePeriodAndPrint(final long begin, final long end) {
        int counter = 0;
        for (HistoryItem item : sortByTimestamp(getFilteredBy(new Predicate() {
            @Override
            public boolean condition(HistoryItem item) {
                return ((item.getTimestamp() >= begin) && (item.getTimestamp() <= end));
            }
        }))) {
            System.out.println(item.printPretty(new StringBuilder()).toString());
            ++counter;
        }
        return counter;
    }

    public void addMessage(String author, String message) {
        chat.add(new HistoryItem(author, message));
        ++numItems;
        ++numItemsAdded;
    }

    public int findByKeywordAndPrint(final String keyword) {
        int counter = 0;
        for (HistoryItem item : sortByTimestamp(getFilteredBy(new Predicate() {
            @Override
            public boolean condition(HistoryItem item) {
                return doesStringContainWord(item.getMessage(), keyword);
            }
        }))) {
            System.out.println(item.printPretty(new StringBuilder()).toString());
            ++counter;
        }
        return counter;
    }

    // return true if message exists and successfully deleted
    public boolean deleteById(String id) {
        boolean result = chat.remove(new HistoryItem(new HistoryItemId(id)));
        if (result) {
            --numItems;
            ++numItemsDeleted;
        }
        return result;
    }

    public String getStatistics() {
        StringBuilder builder = new StringBuilder();
        builder.append("Total number of messages: ");
        builder.append(numItems);
        builder.append(", added from keyboard: ");
        builder.append(numItemsAdded);
        builder.append(", deleted: ");
        builder.append(numItemsDeleted);
        builder.append(".");
        return builder.toString();
    }

    private ArrayList<HistoryItem> getAllItemsAsArrayList() {
        ArrayList<HistoryItem> chatList = new ArrayList<>(numItems);
        for (HistoryItem item : chat) {
            chatList.add(item);
        }
        return chatList;
    }

    private static ArrayList<HistoryItem> sortByTimestamp(ArrayList<HistoryItem> chatList) {
        Collections.sort(chatList, new Comparator<HistoryItem>() {
            @Override
            public int compare(HistoryItem first, HistoryItem second) {
                return first.getTimestamp().compareTo(second.getTimestamp());
            }
        });
        return chatList;
    }


    private ArrayList<HistoryItem> getFilteredBy(Predicate predicate) {
        ArrayList<HistoryItem> chatList = new ArrayList<>();
        for (HistoryItem item : chat) {
            if (predicate.condition(item)) {
                chatList.add(item);
            }
        }
        return chatList;
    }


    private static boolean doesStringContainWord(String string, String word) {
        //int wordLength = word.length();
        for (String sequence : string.split("[^\\p{IsAlphabetic}-\\\\']")) {
            if (sequence.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    private Set<HistoryItem> chat;
    private int numItems;
    private int numItemsAdded;
    private int numItemsDeleted;


}
