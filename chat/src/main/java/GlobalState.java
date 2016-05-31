public class GlobalState {
    private static FileKeyStorage fileKeyStorage = new FileKeyStorage();
    private static MessageStorage messageStorage = new InMemoryMessageStorage();

    public static FileKeyStorage getKeyStorage(){
        return fileKeyStorage;
    }

    public static MessageStorage getMessageStorage(){
        return messageStorage;
    }
}
