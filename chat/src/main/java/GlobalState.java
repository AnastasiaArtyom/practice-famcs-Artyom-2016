/**
 * Created by anastasia on 30.5.16.
 */
public class GlobalState {
    private static FileKeyStorage fileKeyStorage = new FileKeyStorage();

    public static FileKeyStorage getKeyStorage(){
        return fileKeyStorage;
    }
}
