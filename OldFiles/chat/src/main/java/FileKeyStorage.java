import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FileKeyStorage {
    private static final String FILE_NAME = System.getProperty("user.home") + "/users.db";
    private Map<String, String> users;

    public FileKeyStorage(){
        try {
            users = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            while (reader.ready()) {
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line);
                String password = tokenizer.nextToken();
                String user = tokenizer.nextToken();
                users.putIfAbsent(password, user);
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public String getUserNameForPassword(String password){
        return users.get(password);
    }

    public boolean addUser(String name, String password) throws IOException{
        if(users.putIfAbsent(password, name) != null){
            return false;
        }
        File myFile = new File(FILE_NAME);
        myFile.createNewFile();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(myFile, true)));
        out.println(password + " " + name);
        out.close();
        return true;
    }
}
