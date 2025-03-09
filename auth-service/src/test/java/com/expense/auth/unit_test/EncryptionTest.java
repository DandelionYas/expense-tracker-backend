package com.expense.auth.unit_test;

import com.expense.utils.EncryptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EncryptionTest {

    private final EncryptionUtil encryptionUtil = new EncryptionUtil();

    @BeforeEach
    public void setUp() {
        encryptionUtil.setEncryptionCipher("AES");
        encryptionUtil.setEncryptionKey("kjd78nf53ksiodnf");
    }

    @Test
    public void testEncryption() throws Exception {
        String encrypted = encryptionUtil.encrypt("Yaser's Password");
        Assertions.assertEquals("A4CC+5SJDpTJ8o1F/QA/xgKi7Gc3r1yDUdOu5v05JjA=", encrypted);
    }

    @Test
    public void testDecryption() throws Exception {
        String decrypted = encryptionUtil.decrypt("A4CC+5SJDpTJ8o1F/QA/xgKi7Gc3r1yDUdOu5v05JjA=");
        Assertions.assertEquals("Yaser's Password", decrypted);
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        String encrypted = encryptionUtil.encrypt("Yaser's Password");
        String decrypted = encryptionUtil.decrypt(encrypted);
        Assertions.assertEquals("Yaser's Password", decrypted);
    }
}
