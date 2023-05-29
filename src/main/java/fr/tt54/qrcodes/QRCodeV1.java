package fr.tt54.qrcodes;

import fr.tt54.qrcodes.finite_fields.F256;
import fr.tt54.qrcodes.polynoms.Polynome256;
import fr.tt54.qrcodes.reedsolomon.ReedSolomon;

import java.nio.charset.StandardCharsets;

public class QRCodeV1 {

    public static final int white = 0xFFFFFFFF;
    public static final int black = 0xFF000000;

    public static int[] encodeString(String input, int maxLength) {
        byte[] bytes = input.getBytes(StandardCharsets.ISO_8859_1);
        int[] encoded = new int[Math.min(bytes.length, maxLength)];
        for (int i = 0; i < encoded.length; i++) {
            encoded[i] = bytes[i] & (0xFF);
        }
        return encoded;
    }

    public static int[] getCodeWithInfos(int[] encode) {
        int[] result = new int[encode.length + 1];
        result[0] = encode.length;

        for (int i = 0; i < encode.length; i++) {
            result[i] = encode[i];
        }

        return result;
    }

    public static boolean[] bytesToBinaryArray(int[] data) {
        boolean[] bytes = new boolean[data.length * 8];
        for (int i = 0; i < data.length; i++) {
            int b = data[i];
            bytes[i * 8 + 7] = (b & 0b1) != 0;
            bytes[i * 8 + 6] = (b & 0b10) != 0;
            bytes[i * 8 + 5] = (b & 0b100) != 0;
            bytes[i * 8 + 4] = (b & 0b1000) != 0;
            bytes[i * 8 + 3] = (b & 0b10000) != 0;
            bytes[i * 8 + 2] = (b & 0b100000) != 0;
            bytes[i * 8 + 1] = (b & 0b1000000) != 0;
            bytes[i * 8] = (b & 0b10000000) != 0;
        }
        return bytes;
    }

    public static int[] addErrorCorrection(int[] encodedData) {
        F256[] elements = new F256[encodedData.length];
        for (int i = 0; i < encodedData.length; i++) {
            elements[i] = ReedSolomon.getF256FromByte(encodedData[i]);
        }
        Polynome256 poly = new Polynome256(elements);
        Polynome256 encoded = ReedSolomon.encode(poly);

        int[] correction = new int[encoded.getDegree() + 1];
        for (int i = 0; i < correction.length; i++) {
            correction[i] = ReedSolomon.getByteFromF256(encoded.getCoeff(i));
        }

        return correction;
    }


    public static int[] getData(String entry) {
        return addErrorCorrection(getCodeWithInfos(encodeString(entry, 14)));
    }

    public static int[][] initializeMatrix() {
        return new int[21][21];
    }

    public static void encodeInfos(int[][] matrix, boolean[] datas) {
        int l = matrix.length;

        matrix[l - 1][l - 1] = black;
        matrix[l - 1][l - 2] = white;
        matrix[l - 2][l - 1] = white;
        matrix[l - 2][l - 2] = white;

        
    }

}
