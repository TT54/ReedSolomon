public class Main {

    public static void main(String[] args) {
        Polynom pol = new Polynom(2, 0, -3, 2);

        //pol.euclidDivision(new Polynom(3, 3));

        System.out.println(pol.euclidDivision(new Polynom(5, 1))[0].toString());
        System.out.println(pol.euclidDivision(new Polynom(5, 1))[1].toString());

        //System.out.println(pol.multiply(new Polynom(0, 1)));
    }

}
