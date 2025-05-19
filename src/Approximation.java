import com.google.common.base.Function;

import java.util.ArrayList;

public class Approximation {
    private final double a;
    private final double b;
    private final int n;
    private final Function<Double, Double> mathFunction;

    public Approximation(double a, double b, int n, Function<Double, Double> mathFunction) {
        this.a = a;
        this.b = b;
        this.n = n;
        this.mathFunction = mathFunction;
    }

    public void smallestSquares(double []x, int m, double vx) {
        Matrix result = new Matrix(n + 1);
        double [] y = new double[x.length];
        fillYs(y,x);
        result = fillMatrix(result, x, n, m);
        result = fillLastColumn(result, x, y, m);
        //result.wypisz();
        result.oblicz();
        double sum = 0;
        for (int i = 0; i < result.wyniki.length; i++) {
            sum += result.wyniki[i] * Math.pow(vx, i);
        }
        System.out.println("Approximation smallest squares result: " + sum);
    }
    private void fillYs(double [] y,double [] x){
        for(int i = 0; i < y.length; i++){
            y[i] = mathFunction.apply(x[i]);
        }
    }
    private Matrix fillMatrix(Matrix matrix, double [] x, int n, int m) {
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                double result;
                result = sumUpS(i, j, m,x);
                matrix.setWartosc(matrix, i, j, result);
            }
        }
        return matrix;
    }
    private Matrix fillLastColumn(Matrix matrix, double [] x, double [] y, int m) {
        int lastColumn = matrix.kolumny - 1;
        for (int i = 0; i < matrix.wiersze; i++) {
            double result;
            result = sumUpT(i ,m, x, y);
            matrix.setWartosc(matrix, i, lastColumn, result);
        }
        return matrix;
    }
    private double sumUpS(int k, int j, int m, double[] x) {
        double sum = 0;
        for(int i = 0; i < m; i++){
            sum += Math.pow(x[i],k+j);
        }
        return sum;
    }

    private double sumUpT(int k, int m, double[] x, double[] y) {
        double sum = 0;
        for(int i = 0; i < m; i++){
            sum += Math.pow(x[i],k) * y[i];
            //System.out.println(x[i] + "^" + k + " * " + y[i]);
        }
        return sum;
    }

    public void wielomiansOrthogonal(double vx){
        Integral integral;
        ArrayList<Function<Double,Double>> wielomians = new ArrayList<>();
        wielomians.add(x -> 1.0);
        wielomians.add(x -> x);
        for (int i = 1; i < n; i++) {
            final int in = i;
            Function<Double, Double> pn = wielomians.get(in);
            Function<Double, Double> pn_minus1 = wielomians.get(in - 1);
            Function<Double, Double> pn_plus1 = x -> (1.0 / (in + 1)) * ((2 * in + 1) * x * pn.apply(x) - in * pn_minus1.apply(x));
            wielomians.add(pn_plus1);
        }
        ArrayList<Double> lambdas = new ArrayList<>();
        for (int i = 0; i < wielomians.size(); i++) {
            int index = i;
            Function<Double, Double> powr = x -> Math.pow(wielomians.get(index).apply(x), 2);
            integral = new Integral(a, b, powr);
            lambdas.add(integral.trapezoidMethod(100));
        }
        ArrayList<Double> cN = new ArrayList<>();
        for (int i = 0; i < wielomians.size(); i++) {
            int index = i;
            Function<Double, Double> mul = x -> wielomians.get(index).apply(x) * mathFunction.apply(x);
            cN.add(Math.pow(lambdas.get(i),-1) * new Integral(a, b, mul).trapezoidMethod(100));
        }
        double result = 0;
        for (int i = 0; i < cN.size(); i++) {
            result += cN.get(i) * wielomians.get(i).apply(vx);
        }
        System.out.println("Approximation orthogonal result: " + result);
    }
    public void leastSquaresApproximation(double x){
        Matrix result = new Matrix(n + 1);
        double res = 0;
        result = fill3x3Matrix(result);
        result = fillLastColumnMatrix(result);
        result.oblicz();
        for (int i = 0; i < result.wyniki.length; i++) {
            res += result.wyniki[i] * Math.pow(x, i);
        }
        System.out.println("Approximation least sqaures result: " + res);
    }
    private Matrix fill3x3Matrix(Matrix matrix){
        Integral integral;
        for (int i = 0; i < matrix.kolumny - 1; i++) {
            for (int j = 0; j < matrix.wiersze; j++) {
                int pow1 = j;
                int pow2 = i;
                Function<Double, Double> function = x -> Math.pow(x, pow1) * Math.pow(x, pow2);
                integral = new Integral(a,b,function);
                matrix.setWartosc(matrix,i,j,integral.trapezoidMethod( 100));
            }
        }
        return matrix;
    }
    private Matrix fillLastColumnMatrix(Matrix matrix){
        Integral integral;
        int lastColumn = matrix.kolumny - 1;
        for (int i = 0; i < matrix.wiersze; i++) {
            int pow = i;
            Function<Double, Double> function = x -> Math.pow(x, pow) * mathFunction.apply(x);
            integral = new Integral(a,b,function);
            matrix.setWartosc(matrix,i,lastColumn,integral.trapezoidMethod(100));
        }
        return matrix;
    }
    public static void main(String[] args) {
        Approximation approximation1 = new Approximation(-1,1,3,x -> Math.sqrt(3 * Math.pow(x, 3) - x + 5));
        approximation1.leastSquaresApproximation(0.25);
        approximation1.wielomiansOrthogonal(0.25);
        approximation1.smallestSquares(new double[]{-1, -0.5, 0, 0.5, 1}, 5, 0.25);
    }
}


