package by.bsu.up.chat.common.models;

import java.io.Serializable;

import javax.json.Json;
import javax.json.JsonObject;

public class Message implements Serializable {

    private String id;
    private String author;
    private String time;
    private String text;
    private boolean isDeleted;
    private boolean isEdited;
    private String method;

    public Message() {}

    public Message(JsonObject message){
        this.id  = message.getString("id");
        this.author = message.getString("author");
        this.time = message.getString("time");
        this.text = message.getString("text");
        this.isDeleted = message.getBoolean("isDeleted");
        this.isEdited = message.getBoolean("isEdited");
    }

    public Message(String id, String text){
        this.id = id;
        this.text = text;
        this.author = "";
        this.time = "";
        this.isEdited = false;
        this.isDeleted = false;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimestamp() {
        return time;
    }

    public void setTimestamp(String timestamp) {
        this.time = timestamp;
    }

    public boolean getDeletedFlag(){
        return isDeleted;
    }

    public void setDeletedFlag(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    public boolean getEditedFlag(){
        return isEdited;
    }

    public void setEditedFlag(boolean isEdited){
        this.isEdited = isEdited;
    }
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", time=" + time +
                ", text='" + text + '\'' +
                ", method='" + method + '\'' +
                ", isEdited='" + isEdited + '\'' +
                ", isDeleted='" + isDeleted + '\'' +

                '}';
    }

    public JsonObject toJsonObject () {
        return  Json.createObjectBuilder()
                .add("id", id)
                .add("author", author)
                .add("time", time)
                .add("text", text)
                .add("idEdited", isEdited)
                .add("isDeleted", isDeleted)
                .build();
    }


}
