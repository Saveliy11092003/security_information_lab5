package nsu.trushkov;

import java.util.Arrays;


public class Gost28147 {

    private static byte[][] sBlock = new byte[0][];

    public Gost28147(byte[][] sBlock) {
        this.sBlock = sBlock;
    }

    private void step(int startIndex, byte[] in, int key) {
        int n2 = in[in.length - startIndex - 1] << 24 | (in[in.length - startIndex - 2] & 0xFF) << 16
                | (in[in.length - startIndex - 3] & 0xFF) << 8 | (in[in.length - startIndex - 4] & 0xFF);
        int n1 = in[in.length - startIndex - 5] << 24 | (in[in.length - startIndex - 6] & 0xFF) << 16
                | (in[in.length - startIndex - 7] & 0xFF) << 8 | (in[in.length - startIndex - 8] & 0xFF);
        int s = ((n1 + key) & 0xFFFFFFFF);

        s = replaceBits(s);
        s = shiftLeft(s);
        s = s ^ n2;
        byte[] res = join(n1, s);
        for (int j = 7; j >= 0; j--) {
            in[in.length - 1 - (startIndex + j)] = res[j];
        }
    }

    public byte[] encrypt(byte[] in, byte[] keys) {
        byte[] out = Arrays.copyOf(in, in.length);

        for (int i = 0; i <= in.length - 8; i += 8) {
            for (int q = 0; q < 3; q++) {
                for (int w = 0; w < 8; w++) {
                    int key = getKey(w, keys);
                    step(i, out, key);
                }
            }
            for (int w = 7; w >= 0; w--) {
                int key = getKey(w, keys);
                step(i, out, key);
                if (w == 0) {
                    byte[] temp = new byte[8];
                    System.arraycopy(out, out.length - i - 8, temp, 4, 4);
                    System.arraycopy(out, out.length - i - 4, temp, 0, 4);
                    System.arraycopy(temp, 0, out, out.length - i - 8, 8);
                }
            }
        }
        return out;
    }

    public byte[] decrypt(byte[] in, byte[] keys) {
        byte[] out = Arrays.copyOf(in, in.length);
        int w;

        for (int i = 0; i <= in.length - 8; i += 8) {
            for (w = 0; w < 8; w++) {
                int key = getKey(w, keys);
                step(i, out, key);
            }
            for (int q = 0; q < 3; q++) {
                for (w = 7; w >= 0; w--) {
                    int key = getKey(w, keys);
                    step(i, out, key);
                }
                if (w == -1 && q == 2) {
                    byte[] temp = new byte[8];
                    System.arraycopy(out, out.length - i - 8, temp, 4, 4);
                    System.arraycopy(out, out.length - i - 4, temp, 0, 4);
                    System.arraycopy(temp, 0, out, out.length - i - 8, 8);
                }
            }
        }

        return out;
    }


    private int shiftLeft(int a) {
        int shift = 11;
        a = (a >>> (32 - shift)) | a << shift;
        return a;
    }

    private byte[] join(int n1, int n2) {
        byte[] b = new byte[8];
        for (int j = 0; j < 4; j++) {
            b[j] = (byte) ((n1 >> 24 - (j * 8)) & 0xFF);
        }
        for (int j = 4; j < 8; j++) {
            b[j] = (byte) ((n2 >> 24 - (j * 8)) & 0xFF);
        }
        return b;
    }


    private int getKey(int w, byte[] keys) {
        return keys[w * 4 + 3] << 24 | (keys[w * 4 + 2] & 0xFF) << 16 | (keys[w * 4 + 1] & 0xFF) << 8
                | (keys[w * 4] & 0xFF);
    }

    private int replaceBits(int n) {
        int s = 0;
        for (int i = 0, j = 0; i <= 28; i += 4, j++) {
            s += (sBlock[j][(byte) ((n >> i) & 0xF)]) << (i);
        }
        return s;
    }

}