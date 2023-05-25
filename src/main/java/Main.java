public class Main {

    public static void main(String[] args) {
        /*Polynom pol = new Polynom(2, 0, -3, 2);

        //pol.euclidDivision(new Polynom(3, 3));

        *//*System.out.println(pol.euclidDivision(new Polynom(5, 1))[0].toString());
        System.out.println(pol.euclidDivision(new Polynom(5, 1))[1].toString());*//*


*//*
        Polynom p = new Polynom(0, 1, 0, 0, 0, 0, 0, 0);
        System.out.println(Polynom.multiply(p, p));

        System.out.println(new Polynom(0, 0, 0, 0, 0, 0, 0, 0, 1).euclidDivision(new Polynom(1, 1, 0, 0, 0, 0, 0, 1))[1].toString());
*//*


        Polynome2 p = new Polynome2(false, true, true);
        Polynome2 p2 = new Polynome2(true, true, false, true);
        System.out.println(Polynome2.multiply(p, p).euclidDivision(p2)[1].toString());


        System.out.println(p.euclidMultiply(p, p2));

        Polynome2 p3 = new Polynome2(true, true);
        System.out.println(p3.euclidMultiply(p, p2));

        System.out.println("----");

        Polynome2 ideal = new Polynome2(true, false, true, true, true, false, false, false, true);
        Polynome2 mult = new Polynome2(false, true);
        System.out.println(mult.euclidMultiply(mult, ideal));
        System.out.println(mult.euclidPuissance(15, ideal));
        System.out.println(mult.euclidPuissance(17, ideal));
        System.out.println(mult.euclidPuissance(51, ideal));
        System.out.println(mult.euclidPuissance(85, ideal));
        System.out.println(mult.euclidPuissance(255, ideal));

        F256 f = F256.getElement(ideal);
        System.out.println("---");
        System.out.println(f.getPolynome());
        F256 f2 = F256.getElement(new Polynome2(false, true, false, true));
        System.out.println(f2.getPolynome());
        System.out.println(f2.getInverse());
        System.out.println(f2.getInverse().multiply(f2));

        Polynome256 polynome256 = new Polynome256(f2.multiply(f2), f2);
        System.out.println(polynome256);*/


        /*Polynome256 p1 = new Polynome256(F256.getAlpha(), F256.getAlpha(), F256.getElement(15), F256.getElement(12));
        Polynome256 p2 = new Polynome256(F256.getAlpha(), F256.getUnit(), F256.getZero(), F256.getUnit());

        Polynome256[] result = Decoder.useEuclide(p1, p2);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);

        System.out.println(Polynome256.add(Polynome256.multiply(p1, result[0]), Polynome256.multiply(p2, result[1])));*/

        //System.out.println(Decoder.testEuclid(10000, 10));


        //System.out.println(pol.multiply(new Polynom(0, 1)));

        /*ReedSolomon.test();*/

        boolean success = true;
        final int testsAmount = 100;

        for (int i = 0; i < testsAmount; i++) {
            success = success && ReedSolomon.testWithRandom();
        }

        System.out.println(success);
    }

}
