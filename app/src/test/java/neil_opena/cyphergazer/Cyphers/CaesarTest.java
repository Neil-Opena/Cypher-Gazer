package neil_opena.cyphergazer.Cyphers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CaesarTest {
    private Caesar mCaesarCypher;

    @Before
    public void setUp(){
        mCaesarCypher = new Caesar();
    }

    @Test
    public void encrypt() {
        String result = mCaesarCypher.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "23" );
        assertEquals("XYZABCDEFGHIJKLMNOPQRSTUVW", result);

        result = mCaesarCypher.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "1" );
        assertEquals("BCDEFGHIJKLMNOPQRSTUVWXYZA", result);

        result = mCaesarCypher.encrypt("THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG", "23");
        assertEquals("QEBNRFZHYOLTKCLUGRJMPLSBOQEBIXWVALD", result);
    }

    @Test
    public void decrypt() {

        String result = mCaesarCypher.decrypt("BCDEFGHIJKLMNOPQRSTUVWXYZA", "1");
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", result);

        result = mCaesarCypher.decrypt("QEBNRFZHYOLTKCLUGRJMPLSBOQEBIXWVALD","23");
        assertEquals("THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG", result);
    }
}