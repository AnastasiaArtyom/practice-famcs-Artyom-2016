import java.io.IOException;

/**
 * Created by anastasia on 30.5.16.
 */
public class TestDBGenerator {
    public static void main(String args[]){
        try {
            GlobalState.getKeyStorage().addUser("vasia", Hasher.encryptPassword("pupkin"));
            GlobalState.getKeyStorage().addUser("root", Hasher.encryptPassword("1111"));
            GlobalState.getKeyStorage().addUser("guest", Hasher.encryptPassword("guest"));
        }
        catch(IOException _) {}
    }
}
