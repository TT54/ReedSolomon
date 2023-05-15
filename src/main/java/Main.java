public class Main {

    public static void main(String[] args) {
        Polynom pol = new Polynom(2, 0, -3, 2);

        //pol.euclidDivision(new Polynom(3, 3));

        /*System.out.println(pol.euclidDivision(new Polynom(5, 1))[0].toString());
        System.out.println(pol.euclidDivision(new Polynom(5, 1))[1].toString());*/


/*
        Polynom p = new Polynom(0, 1, 0, 0, 0, 0, 0, 0);
        System.out.println(Polynom.multiply(p, p));

        System.out.println(new Polynom(0, 0, 0, 0, 0, 0, 0, 0, 1).euclidDivision(new Polynom(1, 1, 0, 0, 0, 0, 0, 1))[1].toString());
*/


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
        System.out.println(polynome256);

        //System.out.println(pol.multiply(new Polynom(0, 1)));
    }

}
