package com.company;

import javax.json.Json;
import javax.json.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryItem {

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

    public Long getTimestamp() {
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
    private Long timestamp;
    private HistoryItemId id;

}