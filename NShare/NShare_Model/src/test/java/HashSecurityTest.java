import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import security.HashSecurity;

import java.security.NoSuchAlgorithmException;

/**
 * Created by banhidi on 5/25/2017.
 */
public class HashSecurityTest {

    @Test
    public void applyHashFunctionTest1() {
        HashSecurity hashSecurity = new HashSecurity();
        String input = "universitate";
        String output = "8375c0cf2a0d8d171c7c6c82e83638a503f13608aa224a6c0320de7f87273ebf";
        try {
            Assert.assertEquals(output, hashSecurity.applyHashFunction(input));
        } catch(NoSuchAlgorithmException ex) {
            Assert.fail();
        }
    }

}
