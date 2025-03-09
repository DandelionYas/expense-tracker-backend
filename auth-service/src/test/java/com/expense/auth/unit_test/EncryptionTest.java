package com.expense.auth.unit_test;

import com.expense.utils.EncryptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EncryptionTest {

    private final EncryptionUtils encryptionUtils = new EncryptionUtils();

    @BeforeEach
    public void setUp() {
        encryptionUtils.setEncryptionCipher("AES");
        encryptionUtils.setEncryptionKey("kjd78nf53ksiodnf");
    }

    @Test
    public void testEncryption() throws Exception {
        String encrypted = encryptionUtils.encrypt("Yaser's Password");
        Assertions.assertEquals("A4CC+5SJDpTJ8o1F/QA/xgKi7Gc3r1yDUdOu5v05JjA=", encrypted);
    }

    @Test
    public void testDecryption() throws Exception {
        String decrypted = encryptionUtils.decrypt("A4CC+5SJDpTJ8o1F/QA/xgKi7Gc3r1yDUdOu5v05JjA=");
        Assertions.assertEquals("Yaser's Password", decrypted);
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        String encrypted = encryptionUtils.encrypt("Yaser's Password");
        String decrypted = encryptionUtils.decrypt(encrypted);
        Assertions.assertEquals("Yaser's Password", decrypted);
    }
}
