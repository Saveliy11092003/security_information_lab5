package nsu.trushkov;

import java.util.Scanner;

public class StreamCipher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String data = scanner.nextLine();
        System.out.println("Data : " + data);
        String encryptData = encrypt(data);
        System.out.println("Encrypt Data : " + encryptData);
        String decryptData = decrypt(encryptData);
        System.out.println("Decrypt Data : " + decryptData);
    }

    public static String encrypt(String data) {
        int a = 5;
        int b = 12;
        int c = 23;
        int z = 4;

        byte[] bytes = data.getBytes();
        byte[] encrypt = new byte[bytes.length];

        for (int i = 0; i < data.length(); i++) {
            byte key = (byte) (z & 0xFF);
            encrypt[i] = (byte) (key ^ bytes[i]);
            z = (a * z + b) % c;
        }

        return new String(encrypt);
    }

    public static String decrypt(String data) {
        int a = 5;
        int b = 12;
        int c = 23;
        int z = 4;

        byte[] bytes = data.getBytes();
        byte[] decrypt = new byte[bytes.length];

        for (int i = 0; i < data.length(); i++) {
            byte key = (byte) (z & 0xFF);
            decrypt[i] = (byte) (key ^ bytes[i]);
            z = (a * z + b) % c;
        }

        return new String(decrypt);
    }

}

