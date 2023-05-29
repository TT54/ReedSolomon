package fr.tt54.qrcodes;

import fr.tt54.qrcodes.reedsolomon.ReedSolomon;

public class Main {

    public static void main(String[] args) {
        /*fr.tt54.reedsolomon.polynoms.Polynom pol = new fr.tt54.reedsolomon.polynoms.Polynom(2, 0, -3, 2);

        //pol.euclidDivision(new fr.tt54.reedsolomon.polynoms.Polynom(3, 3));

        *//*System.out.println(pol.euclidDivision(new fr.tt54.reedsolomon.polynoms.Polynom(5, 1))[0].toString());
        System.out.println(pol.euclidDivision(new fr.tt54.reedsolomon.polynoms.Polynom(5, 1))[1].toString());*//*


         *//*
        fr.tt54.reedsolomon.polynoms.Polynom p = new fr.tt54.reedsolomon.polynoms.Polynom(0, 1, 0, 0, 0, 0, 0, 0);
        System.out.println(fr.tt54.reedsolomon.polynoms.Polynom.multiply(p, p));

        System.out.println(new fr.tt54.reedsolomon.polynoms.Polynom(0, 0, 0, 0, 0, 0, 0, 0, 1).euclidDivision(new fr.tt54.reedsolomon.polynoms.Polynom(1, 1, 0, 0, 0, 0, 0, 1))[1].toString());
*//*


        fr.tt54.reedsolomon.polynoms.Polynome2 p = new fr.tt54.reedsolomon.polynoms.Polynome2(false, true, true);
        fr.tt54.reedsolomon.polynoms.Polynome2 p2 = new fr.tt54.reedsolomon.polynoms.Polynome2(true, true, false, true);
        System.out.println(fr.tt54.reedsolomon.polynoms.Polynome2.multiply(p, p).euclidDivision(p2)[1].toString());


        System.out.println(p.euclidMultiply(p, p2));

        fr.tt54.reedsolomon.polynoms.Polynome2 p3 = new fr.tt54.reedsolomon.polynoms.Polynome2(true, true);
        System.out.println(p3.euclidMultiply(p, p2));

        System.out.println("----");

        fr.tt54.reedsolomon.polynoms.Polynome2 ideal = new fr.tt54.reedsolomon.polynoms.Polynome2(true, false, true, true, true, false, false, false, true);
        fr.tt54.reedsolomon.polynoms.Polynome2 mult = new fr.tt54.reedsolomon.polynoms.Polynome2(false, true);
        System.out.println(mult.euclidMultiply(mult, ideal));
        System.out.println(mult.euclidPuissance(15, ideal));
        System.out.println(mult.euclidPuissance(17, ideal));
        System.out.println(mult.euclidPuissance(51, ideal));
        System.out.println(mult.euclidPuissance(85, ideal));
        System.out.println(mult.euclidPuissance(255, ideal));

        fr.tt54.reedsolomon.finite_fields.F256 f = fr.tt54.reedsolomon.finite_fields.F256.getElement(ideal);
        System.out.println("---");
        System.out.println(f.getPolynome());
        fr.tt54.reedsolomon.finite_fields.F256 f2 = fr.tt54.reedsolomon.finite_fields.F256.getElement(new fr.tt54.reedsolomon.polynoms.Polynome2(false, true, false, true));
        System.out.println(f2.getPolynome());
        System.out.println(f2.getInverse());
        System.out.println(f2.getInverse().multiply(f2));

        fr.tt54.reedsolomon.polynoms.Polynome256 polynome256 = new fr.tt54.reedsolomon.polynoms.Polynome256(f2.multiply(f2), f2);
        System.out.println(polynome256);*/


        /*fr.tt54.reedsolomon.polynoms.Polynome256 p1 = new fr.tt54.reedsolomon.polynoms.Polynome256(fr.tt54.reedsolomon.finite_fields.F256.getAlpha(), fr.tt54.reedsolomon.finite_fields.F256.getAlpha(), fr.tt54.reedsolomon.finite_fields.F256.getElement(15), fr.tt54.reedsolomon.finite_fields.F256.getElement(12));
        fr.tt54.reedsolomon.polynoms.Polynome256 p2 = new fr.tt54.reedsolomon.polynoms.Polynome256(fr.tt54.reedsolomon.finite_fields.F256.getAlpha(), fr.tt54.reedsolomon.finite_fields.F256.getUnit(), fr.tt54.reedsolomon.finite_fields.F256.getZero(), fr.tt54.reedsolomon.finite_fields.F256.getUnit());

        fr.tt54.reedsolomon.polynoms.Polynome256[] result = fr.tt54.reedsolomon.reedsolomon.Decoder.useEuclide(p1, p2);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);

        System.out.println(fr.tt54.reedsolomon.polynoms.Polynome256.add(fr.tt54.reedsolomon.polynoms.Polynome256.multiply(p1, result[0]), fr.tt54.reedsolomon.polynoms.Polynome256.multiply(p2, result[1])));*/

        //System.out.println(fr.tt54.reedsolomon.reedsolomon.Decoder.testEuclid(10000, 10));


        //System.out.println(pol.multiply(new fr.tt54.reedsolomon.polynoms.Polynom(0, 1)));

        /*fr.tt54.reedsolomon.reedsolomon.ReedSolomon.test();*/

        launchTest(1000);
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
