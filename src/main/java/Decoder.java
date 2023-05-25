import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Decoder {


    public static Polynome256 getSyndrome(Polynome256 received){
        F256 alpha = F256.getAlpha();
        Polynome256 S = new Polynome256(F256.getZero());
        for(int i = 1; i <= 2 * Encoder.t; i++){
            S.setCoeff(i - 1, received.evaluate(alpha.power(i)));
        }
        return S;
    }

    public static Polynome256 decode(Polynome256 received){
        Polynome256 syndrome = getSyndrome(received);

        if (syndrome.equal(new Polynome256(F256.getZero()))) {
            return received;
        }

        Polynome256[] euclide = useEuclide(syndrome, Encoder.getX2t());
        if (euclide[2].equal(new Polynome256(F256.getZero()))) {
            return null;
        }

        Polynome256 L = euclide[0];
        Polynome256 w = euclide[2];

        /*L = Polynome256.multiply(L, L.getCoeffDominant().getInverse());
        w = Polynome256.multiply(w, w.getCoeffDominant().getInverse());*/

        List<Integer> errorPositions = new ArrayList<>();
        for (int i = 1; i < 256; i++) {
            F256 eval = L.evaluate(F256.getElement(i));
            if (eval.isZero()) {
                errorPositions.add((256 - F256.getElement(i).getAlphaPower() - 1) % 255);
/*
                System.out.println(256 - F256.getElement(i).getAlphaPower() - 1);
*/
            }
        }

        Polynome256 corrector = new Polynome256(F256.getZero());

        for(int position : errorPositions){
            F256 alphaI = F256.getAlpha().power(position);
            corrector.setCoeff(position, w.evaluate(alphaI.getInverse()).divide(L.derivate().evaluate(alphaI.getInverse())));
        }
/*

        System.out.println("correcteur : " + corrector);
        System.out.println(received);
*/



        return Polynome256.add(received, corrector);
    }


    /**
     * Permet d'effectuer l'algorithme d'Euclide pour obtenir la relation de Bézout : a * poly1 + b * poly2 = c avec a,b,c des polynomes dans F256
     * @param poly1
     * @param poly2
     * @return [a, b, c]
     */
    public static Polynome256[] useEuclide(Polynome256 poly1, Polynome256 poly2){
        Polynome256 r0 = poly2, r1 = poly1;
        Polynome256 u0 = new Polynome256(F256.getUnit()), u1 = new Polynome256(F256.getZero());
        Polynome256 v0 = new Polynome256(F256.getZero()), v1 = new Polynome256(F256.getUnit());


        while (r1.getDegree() >= Encoder.t) {
            Polynome256[] div = r0.euclidDivision(r1);

            if (div[1].equal(new Polynome256(F256.getZero()))) {
                break;
            }

            r0 = r1;
            r1 = div[1];

            Polynome256 copy = u0;
            u0 = u1;
            u1 = Polynome256.substract(copy, Polynome256.multiply(div[0], u1));

            copy = v0;
            v0 = v1;
            v1 = Polynome256.substract(copy, Polynome256.multiply(div[0], v1));
        }

        return new Polynome256[] {v1, u1, r1};
    }




    public static boolean testEuclid(int tests, int maxPolynomeSize){
        Random random = new Random();

        for(int i = 0; i < tests; i++){
            Polynome256 p1 = new Polynome256(F256.getZero());
            Polynome256 p2 = new Polynome256(F256.getZero());

            int p1Degree = random.nextInt(maxPolynomeSize) + 1;
            for(int j = 0; j < p1Degree; j++){
                p1.setCoeff(j, F256.getElement(random.nextInt(255)));
            }

            int p2Degree = random.nextInt(maxPolynomeSize) + 1;
            for(int j = 0; j < p2Degree; j++){
                p2.setCoeff(j, F256.getElement(random.nextInt(255)));
            }

            if(p1.equal(new Polynome256(F256.getZero())) || p2.equal(new Polynome256(F256.getZero()))){
                continue;
            }

            Polynome256[] result = Decoder.useEuclide(p1, p2);

            Polynome256 calcul = Polynome256.add(Polynome256.multiply(p1, result[0]), Polynome256.multiply(p2, result[1]));
            if(!calcul.equal(result[2])){
                System.out.println(calcul);
                System.out.println("=/=");
                System.out.println(result[2]);
                return false;
            }
        }
        return true;
    }

}
