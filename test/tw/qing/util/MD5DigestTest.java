package tw.qing.util;

import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

public class MD5DigestTest extends TestCase {

    public void testGetDigest() {
        // there are example md5hex from python
        //
        // >>> hashlib.md5("abc").hexdigest()
        // '900150983cd24fb0d6963f7d28e17f72'
        // >>> hashlib.md5("def").hexdigest()
        // '4ed9407630eb1000c0f6b63842defa7d'
        // >>> hashlib.md5("ghi").hexdigest()
        // '826bbc5d0522f5f20a1da4b60fa8c871'

        try {
            assertEquals("900150983cd24fb0d6963f7d28e17f72",
                    MD5Digest.getDigest("abc", ""));
        } catch (NoSuchAlgorithmException ignored) {
        }

        try {
            assertEquals("4ed9407630eb1000c0f6b63842defa7d",
                    MD5Digest.getDigest("def", ""));
        } catch (NoSuchAlgorithmException ignored) {
        }

        try {
            assertEquals("826bbc5d0522f5f20a1da4b60fa8c871",
                    MD5Digest.getDigest("ghi", ""));
        } catch (NoSuchAlgorithmException ignored) {
        }


        // >>> hashlib.md5("ghi"+"salt").hexdigest()
        // 'a1d0e4e8db37d236bf262b833f472ccd'
        try {
            assertEquals("a1d0e4e8db37d236bf262b833f472ccd",
                    MD5Digest.getDigest("ghi", "salt"));
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    public void testToHexString() {
        for (byte i = 0; i < 9; i++) {
            assertEquals("0" + i, MD5Digest.toHexString(new byte[] { i }));
        }

        assertEquals("0a", MD5Digest.toHexString(new byte[] { 10 }));
        assertEquals("0b", MD5Digest.toHexString(new byte[] { 11 }));
        assertEquals("0c", MD5Digest.toHexString(new byte[] { 12 }));
        assertEquals("0d", MD5Digest.toHexString(new byte[] { 13 }));
        assertEquals("0e", MD5Digest.toHexString(new byte[] { 14 }));
        assertEquals("0f", MD5Digest.toHexString(new byte[] { 15 }));
        assertEquals("10", MD5Digest.toHexString(new byte[] { 16 }));

        assertEquals("0a0100ff", MD5Digest.toHexString(
                new byte[] { 0x0a, 0x01, 0, (byte) 0xff }));
    }

}
