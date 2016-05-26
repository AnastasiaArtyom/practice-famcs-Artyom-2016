package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;

import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private static final Logger logger = Log.create(FileMessageStorage.class);

    private static final String messageStorageFile = "messageStorage.txt";

    private List<Message> messages = new ArrayList<>();

    public FileMessageStorage() {
        loadMessages();
    }

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        System.out.println("from " + from + " to " + to);
        return messages.subList(from, to);
    }

    public void saveMessages() {
        try {
            JsonWriter jsonWriter = Json.createWriter(new FileWriter(messageStorageFile));
            JsonArrayBuilder builder = Json.createArrayBuilder();
            for (Message msg : messages) {
                builder.add(msg.toJsonObject());
            }
            JsonArray messageArray = builder.build();
            jsonWriter.writeArray(messageArray);
            jsonWriter.close();
        } catch (IOException e) {
            logger.error("Message storage is unavailable", e);
        }
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
        saveMessages();
    }

    @Override
    public boolean updateMessage(Message message) {
        for (Message msg : messages) {
            if (msg.getId().equals(message.getId())) {
                msg.setText(message.getText());
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (Message msg : messages) {
            if (msg.getId().equals(messageId)) {
                messages.remove(msg);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return messages.size();
    }


    private void loadMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader(messageStorageFile))) {
            String messageData = reader.readLine();
            reader.close();
            if (messageData != null) {
                JsonReader jsonReader = Json.createReader(new StringReader(messageData));
                JsonArray messagesArray = jsonReader.readArray();
                jsonReader.close();
                for (int index = 0; index < messagesArray.size(); ++index) {
                    JsonObject obj = messagesArray.getJsonObject(index);
                    messages.add(new Message(obj));
                }
            }
        } catch (IOException e) {
            logger.error("Message storage is unavailable", e);
        }

    }


}

