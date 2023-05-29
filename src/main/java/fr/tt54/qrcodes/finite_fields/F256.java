package fr.tt54.qrcodes.finite_fields;

import fr.tt54.qrcodes.polynoms.Polynome2;

import java.util.HashMap;
import java.util.Map;

public class F256 {

    public static final Polynome2 IDEAL = new Polynome2(true, false, true, true, true, false, false, false, true);

    public static final Map<Polynome2, F256> elementsPoly = new HashMap<>();
    public static final Map<F256, F256> inverses = new HashMap<>();
    public static final Map<F256, Integer> alphaPowers = new HashMap<>();
    public static final Map<Integer, F256> elementFromAlphaPowers = new HashMap<>();

    public static F256[] elements = new F256[256];

    public static F256 getElement(int index) {
        return elements[index];
    }

    public static F256 getElement(Polynome2 poly) {
        return elementsPoly.get(poly.euclidDivision(IDEAL)[1]);
    }

    static {
        generateElements();
        generateInverses();
        generateAlphaPowers();
        generateRuleTables();
        /*
        int equals = 0;


        System.out.println(equals - 256);*/
    }

    private static void generateElements() {
        for (int i = 0; i < 256; i++) {
            Polynome2 pol = new Polynome2(i % 2 == 0, i % 4 < 2, i % 8 < 4, i % 16 < 8, i % 32 < 16, i % 64 < 32, i % 128 < 64, i % 256 < 128);
            F256 f256 = new F256(pol, pol.hashCode());
            elements[pol.hashCode()] = f256;
            elementsPoly.put(pol, f256);
        }
    }

    private static void generateInverses() {
        F256 unit = getElement(new Polynome2(true));
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                if (!elements[i].equal(elements[j])) {
                    if (elements[i].longMultiply(elements[j]).equal(unit)) {
                        inverses.put(elements[i], elements[j]);
                        inverses.put(elements[j], elements[i]);
                    }
                }
            }
        }
        inverses.put(unit, unit);
    }

    private static void generateAlphaPowers() {
        F256 alpha = F256.getElement(new Polynome2(false, true));
        F256 calculus = getUnit();
        for (int i = 0; i < 255; i++) {
            alphaPowers.put(calculus, i);
            elementFromAlphaPowers.put(i, calculus);
            calculus = calculus.longMultiply(alpha);
        }
    }

    private static void generateRuleTables() {
        //Addition :

    }

    public static F256 getElementFromAlphaPower(int alphaPower) {
        return elementFromAlphaPowers.get(alphaPower);
    }

    public static F256 getZero() {
        return getElement(0);
    }

    public static F256 getUnit() {
        return getElement(new Polynome2(true));
    }

    public static F256 getAlpha(){
        return getElement(new Polynome2(false, true));
    }

    private int position;
    private Polynome2 polynome;

    private F256(Polynome2 polynome, int position){
        this.position = position;
        this.polynome = polynome;
    }

    public Polynome2 getPolynome() {
        return polynome;
    }

    private F256 longMultiply(F256 f256) {
        return elementsPoly.get(this.getPolynome().euclidMultiply(f256.polynome, IDEAL));
    }

    public F256 multiply(F256 f256) {
        if (this.isZero() || f256.isZero()) {
            return F256.getZero();
        }

        int otherPower = f256.getAlphaPower();
        int newPower = (this.getAlphaPower() + otherPower) % 255;

        return getElementFromAlphaPower(newPower);


        /*return elementsPoly.get(this.getPolynome().euclidMultiply(f256.polynome, IDEAL));*/
    }

    /*public fr.tt54.reedsolomon.finite_fields.F256 add(fr.tt54.reedsolomon.finite_fields.F256 f256){
        return elementsPoly.get(this.getPolynome().euclideAdd(f256.polynome, IDEAL));
    }*/

    public F256 add(F256 f256) {
        return elementsPoly.get(Polynome2.add(this.getPolynome(), f256.polynome));
    }

    public boolean isZero() {
        return this.position == 0;
    }

    public F256 getInverse() {
        if (this.isZero()) throw new IllegalStateException("Vous ne pouvez pas inverser 0 !");
        return inverses.get(this);
    }

    public int getPosition() {
        return position;
    }

    public F256 divide(F256 f256){
        return this.multiply(f256.getInverse());
    }


    public boolean equal(F256 f256) {
        return this.getPolynome().equal(f256.getPolynome());
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = this.getPolynome().getDegree(); i >= 0; i--){
            double coeff = this.getPolynome().getCoeff(i) ? 1 : 0;
            if(coeff != 0){
                str += " + " + coeff + ((i != 0) ? "α^" + i : "") + " ";
            }
        }
        return str.isEmpty() ? "0" : str;
    }

    public F256 substract(F256 coeff) {
        return this.add(coeff);
    }

    public F256 power(int pow){
        F256 f256 = F256.getUnit();
        for(int i = 0; i < pow; i++){
            f256 = f256.multiply(this);
        }

        return f256;
    }

    public int getAlphaPower() {
        if (this.isZero()) throw new IllegalArgumentException("0 ne peut pas s'écrire comme puissance d'alpha");
        return alphaPowers.get(this);
    }

    public F256 realMultiplication(int number) {
        F256 result = F256.getZero();
        for (int i = 0; i < number; i++) {
            result = result.add(this);
        }
        return result;
    }
}
