package fr.tt54.qrcodes;

import fr.tt54.qrcodes.finite_fields.F256;
import fr.tt54.qrcodes.polynoms.Polynome256;
import fr.tt54.qrcodes.reedsolomon.ReedSolomon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    public static int[] binaryToBytesArray(boolean[] data){
        int[] bytes = new int[data.length / 8];
        for(int i = 0; i < data.length / 8; i++){
            for(int j = 0; j < 8; j++){
                bytes[i] += data[i * 8 + j] ? Math.pow(2, 7 - j) : 0;
            }
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
        int[][] matrix = new int[21][21];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                matrix[i][j] = 0xFFFFFFFF;
            }
        }
        return matrix;
    }

    public static void writeBaseMatrix(int[][] matrix){
        int l = matrix.length;

        for(int i = 0; i < 7; i++){
            matrix[0][i] = black;
            matrix[l - 7][i] = black;
            matrix[0][l - 1 - i] = black;

            matrix[6][i] = black;
            matrix[l - 1][i] = black;
            matrix[6][l - 1 - i] = black;
        }

        for(int i = 0; i < 5; i++){
            matrix[1 + i][0] = black;
            matrix[l - 1 - 1 - i][0] = black;
            matrix[1 + i][l - 1] = black;

            matrix[1 + i][6] = black;
            matrix[l - 1 - 1 - i][6] = black;
            matrix[1 + i][l - 7] = black;
        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                matrix[2 + i][2 + j] = black;
                matrix[l - 1 - 2 - i][2 + j] = black;
                matrix[2 + i][l - 1 - 2 - j] = black;
            }

            matrix[l - 1 - 8 - 2 * i][4] = black;
            matrix[4][8 + 2 * i] = black;
        }
    }

    public static void encodeInfos(int[][] matrix, boolean[] datas) {
        int l = matrix.length;

        for(int i = 0; i < datas.length / 8; i++){
            for(int j = 0; j < 8; j++){
                boolean data = datas[i * 8 + j];

                if(i < 12){
                    int row = (i % 3) * 4 + (j/2);
                    int column = (i / 3) * 2 + (j % 2);

                    matrix[l - 1 - row][l - 1 - column] = data ? white : black;
                } else if(i < 22){
                    int row = ((i - 12) % 5) * 4 + (j/2);
                    if((i - 12) % 5 == 4)
                        row += 1;

                    int column = ((i - 12) / 5) * 2 + (j % 2) + 8;

                    matrix[l - 1 - row][l - 1 - column] = data ? white : black;
                } else if(i < 24){
                    int row = 8 + j / 2;
                    int column = 12 + (i - 22) * 2 + (j%2);

                    matrix[l - 1 - row][l - 1 - column] = data ? white : black;
                } else if(i < 26){
                    int row = 8 + j / 2;
                    int column = 17 + (i - 24) * 2 + (j%2);

                    matrix[l - 1 - row][l - 1 - column] = data ? white : black;
                }
            }
        }
    }



    public static void generateImage(int[][] matrix){
        try {
            BufferedImage image = new BufferedImage(matrix.length, matrix.length, BufferedImage.TYPE_INT_RGB);
            for(int i=0; i<matrix.length; i++) {
                for(int j=0; j< matrix.length; j++) {
                    int a = matrix[i][j];
                    Color newColor = new Color(a);
                    //Color newColor = a == 0xFFFFFFFF ? Color.WHITE : Color.BLACK;
                    //newColor = a != -10 ? Color.WHITE : Color.BLACK;
                    image.setRGB(j,i,newColor.getRGB());
                }
            }
            File output = new File("GrayScale.png");
            ImageIO.write(image, "png", output);
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean[] readQRCode(int[][] matrix){
        int l = matrix.length;
        boolean[] datas = new boolean[26 * 8];

        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 8; j++){
                if(i < 12){
                    int row = (i % 3) * 4 + (j/2);
                    int column = (i / 3) * 2 + (j % 2);

                    datas[i * 8 + j] = matrix[l - 1 - row][l - 1 - column] == white;
                } else if(i < 22){
                    int row = ((i - 12) % 5) * 4 + (j/2);
                    if((i - 12) % 5 == 4)
                        row += 1;

                    int column = ((i - 12) / 5) * 2 + (j % 2) + 8;

                    datas[i * 8 + j] = matrix[l - 1 - row][l - 1 - column] == white;
                } else if(i < 24){
                    int row = 8 + j / 2;
                    int column = 12 + (i - 22) * 2 + (j%2);

                    datas[i * 8 + j] = matrix[l - 1 - row][l - 1 - column] == white;
                } else if(i < 26){
                    int row = 8 + j / 2;
                    int column = 17 + (i - 24) * 2 + (j%2);

                    datas[i * 8 + j] = matrix[l - 1 - row][l - 1 - column] == white;
                }
            }
        }

        return datas;
    }

    public static int[][] getMatrixFromImage(String imageName) throws IOException {
        BufferedImage img=ImageIO.read(new File(imageName));
        int pix[][]= new int[img.getHeight()][img.getWidth()];

        for(int i = 0; i < pix.length; i++){
            for(int j = 0; j < pix[i].length; j++){
                pix[i][j] = img.getRGB(j, i);
            }
        }

        return pix;
    }

}