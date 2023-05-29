package fr.tt54.qrcodes.reedsolomon;

import fr.tt54.qrcodes.finite_fields.F256;
import fr.tt54.qrcodes.polynoms.Polynome256;

public class Encoder {

    public static final int t = 8;

    private static Polynome256 generator;

    static {
        generator = new Polynome256(F256.getAlpha(), F256.getUnit());
        for (int i = 2; i <= 2 * t; i++) {
            Polynome256 poly = new Polynome256(F256.getAlpha().power(i), F256.getUnit());
            generator = Polynome256.multiply(generator, poly);
        }
    }


    public static Polynome256 getGenerator(){
        return generator;
    }

    public static Polynome256 getTestInformationPolynome(){
        Polynome256 poly = new Polynome256(F256.getAlpha());
        for(int i = 0; i < 239; i++){
            poly.setCoeff(i, F256.getAlpha());
        }
        return poly;
    }

    public static Polynome256 getX2t(){
        Polynome256 poly = new Polynome256(F256.getZero());
        poly.setCoeff(2 * t, F256.getUnit());
        return poly;
    }

    public static Polynome256 getTransmission(Polynome256 toTransmit){
        Polynome256 x2t = getX2t();
        Polynome256 I = Polynome256.multiply(toTransmit, x2t);
        Polynome256 B = I.euclidDivision(getGenerator())[1];
//        fr.tt54.reedsolomon.polynoms.Polynome256 Q = I.euclidDivision(getGenerator())[0];
        return Polynome256.add(I, B);
    }

}
