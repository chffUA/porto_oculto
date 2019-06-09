package pt.oitoo.portooculto.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class EncryptionTest {


    @Test
    public void encodeStringBase64() {
        String input = "test String to be encrypted";

        String test = Encryption.encodeStringBase64(input);

        assertEquals(test.length(), 37);


    }

    @Test
    public void decodeStringBase64() {
        String encodedTestString = "dGVzdCBTdHJpbmcgdG8gYmUgZW5jcnlwdGVk";

        String decoded = Encryption.decodeStringBase64(encodedTestString);

        assertEquals("test String to be encrypted",decoded);



    }
}