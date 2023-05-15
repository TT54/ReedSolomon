import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polynome256 {

    private List<F256> coeffs;

    public Polynome256(List<F256> coeffs) {
        this.coeffs = coeffs;
    }

    public Polynome256(){
        this.coeffs = new ArrayList<>();
        this.coeffs.add(F256.getZero());
    }

    public Polynome256(F256... coeffs){
        this.coeffs = new ArrayList<>(Arrays.asList(coeffs));
    }

    public void setCoeff(int index, F256 coeff){
        while (coeffs.size() <= index){
            coeffs.add(F256.getZero());
        }
        coeffs.set(index, coeff);
    }

    public void simplify(){
        if(this.coeffs.size() > 0 && this.coeffs.get(this.coeffs.size() - 1).isZero()) {
            this.coeffs.remove(this.coeffs.size() - 1);
            this.simplify();
        }
    }

    public int getDegree(){
        this.simplify();
        return coeffs.size() - 1;
    }

    public F256 getCoeff(int index){
        if(coeffs.size() <= index) return F256.getZero();
        return coeffs.get(index);
    }

/*    public double evaluate(double x){
        double eval = 0;
        for(int i = 0; i < coeffs.size(); i++){
            eval += (coeffs.get(i) ? 1 : 0) * Math.pow(x, i);
        }
        return eval;
    }*/

    public F256 getCoeffDominant(){
        return this.getCoeff(this.getDegree());
    }

    /**
     *
     * @param polynom Le polynôme qui va diviser ce polynôme
     * @return Un tableau de polynômes dont le premier élément est le quotient et le second le reste
     */
    public Polynome256[] euclidDivision(Polynome256 polynom){
        Polynome256 quotient = new Polynome256();
        Polynome256 reste = this.clone();

        while(reste.getDegree() >= polynom.getDegree()){
            int deg = reste.getDegree() - polynom.getDegree();
            Polynome256 divide = new Polynome256();
            divide.setCoeff(deg, F256.getUnit());

            Polynome256 toAdd = divide.clone();

            divide = multiply(divide, polynom);
            divide = multiply(divide, reste.getCoeffDominant().divide(divide.getCoeffDominant()));

            toAdd = multiply(toAdd, divide.getCoeffDominant().divide(polynom.getCoeffDominant()));

            reste = substract(reste, divide);
            quotient = add(quotient, toAdd);
        }

        return new Polynome256[] {quotient, reste};
    }

    public static Polynome256 multiply(Polynome256 polynom, F256 k){
        Polynome256 ret = new Polynome256();
        for(int i = 0; i <= polynom.getDegree(); i++){
            ret.setCoeff(i, polynom.getCoeff(i).multiply(k));
        }
        return ret;
    }

    public static Polynome256 multiply(Polynome256 p1, Polynome256 polynom){
        Polynome256 ret = new Polynome256();
        for(int k = 0; k <= p1.getDegree() + polynom.getDegree(); k++){
            F256 somme = F256.getZero();
            for(int i = 0; i <= k; i++){
                somme = somme.add(p1.getCoeff(i).multiply(polynom.getCoeff(k - i)));
            }
            //ret.setCoeff(k, Math.abs(somme % 2));
            ret.setCoeff(k, somme);
        }
        return ret;
    }

    public static Polynome256 substract(Polynome256 p1, Polynome256 polynom){
        Polynome256 ret = new Polynome256();
        for(int i = 0; i <= Math.max(p1.getDegree(), polynom.getDegree()); i++){
            ret.setCoeff(i, p1.getCoeff(i).substract(polynom.getCoeff(i)));
        }
        return ret;
    }

    public static Polynome256 add(Polynome256 p1, Polynome256 polynom){
        Polynome256 ret = new Polynome256();
        for(int i = 0; i <= Math.max(p1.getDegree(), polynom.getDegree()); i++){
            ret.setCoeff(i, p1.getCoeff(i).add(polynom.getCoeff(i)));
        }
        return ret;
    }

    @Override
    public Polynome256 clone() {
        return new Polynome256(new ArrayList<>(this.coeffs));
    }

    public boolean equal(Polynome256 pol) {
        if(pol.getDegree() != this.getDegree())
            return false;

        for(int i = 0; i <= pol.getDegree(); i++){
            if(!this.getCoeff(i).equal(pol.getCoeff(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < this.coeffs.size(); i++){
            F256 coeff = this.getCoeff(i);
            if(!coeff.isZero()){
                str += " + (" + coeff.toString() + ")" + ((i != 0) ? "X^" + i : "") + " ";
            }
        }
        return str.isEmpty() ? "0" : str;
    }
    
}
