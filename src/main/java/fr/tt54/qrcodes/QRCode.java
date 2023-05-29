package fr.tt54.qrcodes;

import fr.tt54.qrcodes.finite_fields.F256;
import fr.tt54.qrcodes.polynoms.Polynome256;
import fr.tt54.qrcodes.reedsolomon.ReedSolomon;

import java.nio.charset.StandardCharsets;

public class QRCode {


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


    public static int[] addInformations(int[] inputBytes) {
        int[] result = new int[inputBytes.length + 2];

        result[0] = 0b01000000 | (inputBytes.length & 0xF0);
        System.out.println(inputBytes.length & 0x0F);
        System.out.println();
        result[1] = ((inputBytes.length & 0x0F) << 0b100) | ((inputBytes[0] & 0xF0) >> 0b100);

        for (int i = 0; i < inputBytes.length - 1; i++) {
            result[i + 2] = ((inputBytes[i] & 0x0F) << 0b100) | ((inputBytes[i + 1] & 0xF0) >> 0b100);
        }

        result[result.length - 1] = ((inputBytes[inputBytes.length - 1] & 0x0F) << 0b100) | (0b00000000);

        return result;
    }

    public static int[] fillSequence(int[] encodedData, int finalLength) {
        int[] data = new int[finalLength];

        boolean is236 = true;
        for (int i = 0; i < finalLength; i++) {
            if (i < encodedData.length) {
                data[i] = encodedData[i];
            } else {
                data[i] = is236 ? 236 : 17;
                is236 = !is236;
            }
        }

        return data;
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


    public static boolean[] byteModeEncoding(String input) {
        int[] data = encodeString(input, 13);
        int[] dataWithInfos = fillSequence(addInformations(data), 15);
        int[] encoded = addErrorCorrection(dataWithInfos);

        return bytesToBinaryArray(encoded);
    }


    public static int[][] initializeMatrix() {
        return new int[21][21];
    }


    public static void addFinderPatterns(int[][] matrix) {
        for (int i = 0; i < 7; i++) {
            matrix[i][0] = black;
            matrix[i][6] = black;

            matrix[matrix.length - 1 - i][0] = black;
            matrix[matrix.length - 1 - i][6] = black;

            matrix[i][matrix.length - 1] = black;
            matrix[i][matrix.length - 7] = black;
        }


    }

}
