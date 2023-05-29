package fr.tt54.qrcodes.polynoms;

import java.util.ArrayList;
import java.util.List;

public class Polynome2 {


    private List<Boolean> coeffs;

    public Polynome2(List<Boolean> coeffs) {
        this.coeffs = coeffs;
    }

    public Polynome2(){
        this.coeffs = new ArrayList<>();
        this.coeffs.add(false);
    }

    public Polynome2(boolean... coeffs){
        List<Boolean> c = new ArrayList<>();
        for(boolean d : coeffs){
            c.add(d);
        }
        this.coeffs = c;
    }

    public void setCoeff(int index, boolean coeff){
        while (coeffs.size() <= index){
            coeffs.add(false);
        }
        coeffs.set(index, coeff);
    }

    public void simplify(){
        if(this.coeffs.size() > 0 && !this.coeffs.get(this.coeffs.size() - 1)) {
            this.coeffs.remove(this.coeffs.size() - 1);
            this.simplify();
        }
    }

    public int getDegree(){
        this.simplify();
        return coeffs.size() - 1;
    }

    public boolean getCoeff(int index){
        if(coeffs.size() <= index) return false;
        return coeffs.get(index);
    }

    public double evaluate(double x){
        double eval = 0;
        for(int i = 0; i < coeffs.size(); i++){
            eval += (coeffs.get(i) ? 1 : 0) * Math.pow(x, i);
        }
        return eval;
    }

    public boolean getCoeffDominant(){
        return this.getCoeff(this.getDegree());
    }

    /**
     *
     * @param polynom Le polynôme qui va diviser ce polynôme
     * @return Un tableau de polynômes dont le premier élément est le quotient et le second le reste
     */
    public Polynome2[] euclidDivision(Polynome2 polynom){
        Polynome2 quotient = new Polynome2();
        Polynome2 reste = this.clone();

        while(reste.getDegree() >= polynom.getDegree()){
            int deg = reste.getDegree() - polynom.getDegree();
            Polynome2 divide = new Polynome2();
            divide.setCoeff(deg, true);

            Polynome2 toAdd = divide.clone();

            divide = multiply(divide, polynom);
/*            divide = multiply(divide, reste.getCoeffDominant() / divide.getCoeffDominant());

            toAdd = multiply(toAdd, divide.getCoeffDominant() / polynom.getCoeffDominant());*/

            reste = substract(reste, divide);
            quotient = add(quotient, toAdd);
        }

        return new Polynome2[] {quotient, reste};
    }

    public static Polynome2 multiply(Polynome2 p1, Polynome2 polynom){
        Polynome2 ret = new Polynome2();
        for(int k = 0; k <= p1.getDegree() + polynom.getDegree(); k++){
            boolean somme = false;
            for(int i = 0; i <= k; i++){
                somme = somme != (p1.getCoeff(i) && polynom.getCoeff(k - i));
            }
            //ret.setCoeff(k, Math.abs(somme % 2));
            ret.setCoeff(k, somme);
        }
        return ret;
    }

    public Polynome2 multiply(boolean k){
        Polynome2 ret = new Polynome2();
        for(int i = 0; i <= this.getDegree(); i++){
            ret.setCoeff(i, this.getCoeff(i) && k);
        }
        return ret;
    }

    public static Polynome2 substract(Polynome2 p1, Polynome2 polynom){
        Polynome2 ret = new Polynome2();
        for(int i = 0; i <= Math.max(p1.getDegree(), polynom.getDegree()); i++){
            ret.setCoeff(i, p1.getCoeff(i) != polynom.getCoeff(i));
        }
        return ret;
    }

    public static Polynome2 add(Polynome2 p1, Polynome2 polynom){
        Polynome2 ret = new Polynome2();
        for(int i = 0; i <= Math.max(p1.getDegree(), polynom.getDegree()); i++){
            ret.setCoeff(i, p1.getCoeff(i) != polynom.getCoeff(i));
        }
        return ret;
    }

    @Override
    public Polynome2 clone() {
        return new Polynome2(new ArrayList<>(this.coeffs));
    }

    public boolean equal(Polynome2 pol) {
        if(pol.getDegree() != this.getDegree())
            return false;

        for(int i = 0; i <= pol.getDegree(); i++){
            if(this.getCoeff(i) != pol.getCoeff(i)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < this.coeffs.size(); i++){
            double coeff = this.getCoeff(i) ? 1 : 0;
            if(coeff != 0){
                str += " + " + coeff + ((i != 0) ? "X^" + i : "") + " ";
            }
        }
        return str.isEmpty() ? "0" : str;
    }

    public Polynome2 euclidMultiply(Polynome2 multipy, Polynome2 modulo){
        return multiply(this, multipy).euclidDivision(modulo)[1];
    }

    public Polynome2 euclidPuissance(int puissance, Polynome2 modulo){
        Polynome2 result = this.clone();
        for(int i = 0; i < puissance - 1; i++){
            result = result.euclidMultiply(this, modulo);
        }
        return result;
    }

    public Polynome2 euclideAdd(Polynome2 toAdd, Polynome2 modulo){
        return add(this, toAdd).euclidDivision(modulo)[1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polynome2 polynome2 = (Polynome2) o;
        return this.equal(polynome2);
    }

    @Override
    public int hashCode() {
        int somme = 0;
        for(int i = 0; i < this.coeffs.size(); i++){
            somme += this.getCoeff(i) ? Math.pow(2, i) : 0;
        }
        return somme;
    }
}
