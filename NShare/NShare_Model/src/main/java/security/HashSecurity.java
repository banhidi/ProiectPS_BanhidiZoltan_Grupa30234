package security;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by banhidi on 5/25/2017.
 */
public class HashSecurity {

    public String applyHashFunction(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(text.getBytes());
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }

}
