package fr.tt54.qrcodes;

import fr.tt54.qrcodes.reedsolomon.ReedSolomon;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //launchTest(10);

        int[] message = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int[] toSend = ReedSolomon.encodeBytes(message);

        System.out.println(Arrays.toString(toSend));

/*
        toSend[16] = 0;
        toSend[20] = 0;
        toSend[21] = 102;
        toSend[22] = 37;
        toSend[17] = 1;
        toSend[18] = 54;
        toSend[2] = 201;
        toSend[3] = 45;
*/

        System.out.println(Arrays.toString(toSend));

        System.out.println(Arrays.toString(ReedSolomon.decodeBytes(toSend)));

        int[][] matrix = QRCodeV1.initializeMatrix();
        QRCodeV1.writeBaseMatrix(matrix);
        QRCodeV1.encodeInfos(matrix, QRCodeV1.bytesToBinaryArray(toSend));


        QRCodeV1.generateImage(matrix);


        try {
            boolean[] data = QRCodeV1.readQRCode(QRCodeV1.getMatrixFromImage("GrayScale.png"));

            int[] correct = ReedSolomon.decodeBytes(QRCodeV1.binaryToBytesArray(data));

            int[] realData = Arrays.copyOfRange(correct, 16, correct.length);
            System.out.println(Arrays.toString(realData));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void launchTest(int testsAmount) {
        boolean success = true;
        for (int i = 0; i < testsAmount; i++) {
            if (!success) break;
            success = ReedSolomon.testWithRandom();
        }

        System.out.println(success);
    }
}
