package com.company;

import javax.json.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;


public class ChatHistory {

    private class HistoryItemId implements Comparable<HistoryItemId> {

        public HistoryItemId() {
            id = UUID.randomUUID();
        }

        public HistoryItemId(String id) throws IllegalArgumentException {
            this.id = UUID.fromString(id);
        }

        @Override
        public int compareTo(HistoryItemId other) {
            return this.id.compareTo(other.id);
        }

        @Override
        public String toString() {
            return id.toString();
        }

        private UUID id;

    }

    private class HistoryItem {

        public HistoryItem() {

        }

        public HistoryItem(String author, String message) {
            this.author = author;
            this.message = message;
            this.id = new HistoryItemId();
            this.timestamp = new Date().getTime();
        }

        // should be used only to compare id's
        public HistoryItem(HistoryItemId id) {
            this.id = id;
        }

        public HistoryItem parseFromJson(JsonObject jsonObject) {
            author = jsonObject.getString("author");
            message = jsonObject.getString("message");
            timestamp = jsonObject.getJsonNumber("timestamp").longValue();
            id = new HistoryItemId(jsonObject.getString("id"));
            return this;
        }

        public JsonObject getJson() {
            return Json.createObjectBuilder()
                    .add("author", author)
                    .add("message", message)
                    .add("id", id.toString())
                    .add("timestamp", timestamp).build();
        }

        public StringBuilder printPretty(StringBuilder streamB) {
            streamB.append("Author: ");
            streamB.append(author);
            streamB.append("\nTime: ");
            streamB.append(new SimpleDateFormat().format(new Date(timestamp)));
            streamB.append("\nId: ");
            streamB.append(id);
            streamB.append("\nMessage:\n");
            streamB.append(message);
            streamB.append("\n");
            return streamB;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public HistoryItemId getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getMessage() {
            return message;
        }

        private String author;
        private String message;
        private long timestamp;
        private HistoryItemId id;

    }

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
        while (!chat.add(new HistoryItem(author, message))) {
        }
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
                long diff = first.getTimestamp() - second.getTimestamp();
                if (diff == 0) {
                    return 0;
                } else if (diff > 0) {
                    return 1;
                } else
                    return -1;
            }
        });
        return chatList;
    }

    private abstract class Predicate {
        public abstract boolean condition(HistoryItem item);
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

    private TreeSet<HistoryItem> chat;
    private int numItems;
    private int numItemsAdded;
    private int numItemsDeleted;


}
