package nsu.trushkov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFunction {

    public static void main(String[] args) throws IOException {
        System.out.println(getMd5Hash("src/main/resources/text.txt"));
    }

    public static String getMd5Hash(String filename) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = new byte[1024];
            try (var is = Files.newInputStream(Paths.get(filename))) {
                int count;
                while ((count = is.read(b)) > 0) {
                    md.update(b, 0, count);
                }
            }
            byte[] digest = md.digest();
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
