package fr.tt54.qrcodes;

import fr.tt54.qrcodes.reedsolomon.ReedSolomon;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //launchTest(10);

        int[] message = new int[]{1, 2, 3, 4, 5, 6, 7, 8};

        int[] toSend = ReedSolomon.encodeBytes(message);

        System.out.println(Arrays.toString(toSend));

        toSend[0] = 0;
        toSend[1] = 0;
        toSend[2] = 102;
        toSend[3] = 37;
        toSend[4] = 1;
        toSend[5] = 54;

        System.out.println(Arrays.toString(toSend));

        System.out.println(Arrays.toString(ReedSolomon.decodeBytes(toSend)));
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
