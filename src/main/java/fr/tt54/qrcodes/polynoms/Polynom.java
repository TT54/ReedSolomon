package fr.tt54.qrcodes.polynoms;

import java.util.ArrayList;
import java.util.List;

public class Polynom {


    private List<Double> coeffs;

    public Polynom(List<Double> coeffs) {
        this.coeffs = coeffs;
    }

    public Polynom(){
        this.coeffs = new ArrayList<>();
        this.coeffs.add(0d);
    }

    public Polynom(double... coeffs){
        List<Double> c = new ArrayList<>();
        for(double d : coeffs){
            c.add(d);
        }
        this.coeffs = c;
    }

    public void setCoeff(int index, double coeff){
        while (coeffs.size() <= index){
            coeffs.add(0d);
        }
        coeffs.set(index, coeff);
    }

    public void simplify(){
        if(this.coeffs.size() > 0 && this.coeffs.get(this.coeffs.size() - 1) == 0) {
            this.coeffs.remove(this.coeffs.size() - 1);
            this.simplify();
        }
    }

    public int getDegree(){
        this.simplify();
        return coeffs.size() - 1;
    }

    public double getCoeff(int index){
        if(coeffs.size() <= index) return 0;
        return coeffs.get(index);
    }

    public double evaluate(double x){
        double eval = 0;
        for(int i = 0; i < coeffs.size(); i++){
            eval += coeffs.get(i) * Math.pow(x, i);
        }
        return eval;
    }

    public double getCoeffDominant(){
        return this.getCoeff(this.getDegree());
    }

    /**
     *
     * @param polynom Le polynôme qui va diviser ce polynôme
     * @return Un tableau de polynômes dont le premier élément est le quotient et le second le reste
     */
    public Polynom[] euclidDivision(Polynom polynom){
        Polynom quotient = new Polynom();
        Polynom reste = this.clone();

        while(reste.getDegree() >= polynom.getDegree()){
            int deg = reste.getDegree() - polynom.getDegree();
            Polynom divide = new Polynom();
            divide.setCoeff(deg, 1);

            Polynom toAdd = divide.clone();

            divide = multiply(divide, polynom);
            divide = multiply(divide, reste.getCoeffDominant() / divide.getCoeffDominant());

            toAdd = multiply(toAdd, divide.getCoeffDominant() / polynom.getCoeffDominant());

            reste = substract(reste, divide);
            quotient = add(quotient, toAdd);
        }

        return new Polynom[] {quotient, reste};
    }

    public static Polynom multiply(Polynom p1, Polynom polynom){
        Polynom ret = new Polynom();
        for(int k = 0; k <= p1.getDegree() + polynom.getDegree(); k++){
            double somme = 0;
            for(int i = 0; i <= k; i++){
                somme += p1.getCoeff(i) * polynom.getCoeff(k - i);
            }
            //ret.setCoeff(k, Math.abs(somme % 2));
            ret.setCoeff(k, somme);
        }
        return ret;
    }

    public static Polynom multiply(Polynom polynom, double k){
        Polynom ret = new Polynom();
        for(int i = 0; i <= polynom.getDegree(); i++){
            ret.setCoeff(i, polynom.getCoeff(i) * k);
        }
        return ret;
    }

    public static Polynom substract(Polynom p1, Polynom polynom){
        Polynom ret = new Polynom();
        for(int i = 0; i <= Math.max(p1.getDegree(), polynom.getDegree()); i++){
            ret.setCoeff(i, p1.getCoeff(i) - polynom.getCoeff(i));
        }
        return ret;
    }

    public static Polynom add(Polynom p1, Polynom polynom){
        Polynom ret = new Polynom();
        for(int i = 0; i <= Math.max(p1.getDegree(), polynom.getDegree()); i++){
            ret.setCoeff(i, p1.getCoeff(i) + polynom.getCoeff(i));
        }
        return ret;
    }

    @Override
    public Polynom clone() {
        return new Polynom(new ArrayList<>(this.coeffs));
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < this.coeffs.size(); i++){
            double coeff = this.getCoeff(i);
            if(coeff != 0){
                str += " + " + coeff + ((i != 0) ? "X^" + i : "") + " ";
            }
        }
        return str.isEmpty() ? "0" : str;
    }
}
