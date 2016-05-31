import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Hasher {

    public static String encryptPassword(String password){
        try {
            String sha1 = "";
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for (byte b : crypt.digest()) {
                formatter.format("%02x", b);
            }
            sha1 = formatter.toString();
            formatter.close();
            return sha1;
        }
        catch(NoSuchAlgorithmException _){}
        catch(UnsupportedEncodingException _){}
        return null;
    }
}
