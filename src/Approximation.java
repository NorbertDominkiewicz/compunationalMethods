import com.google.common.base.Function;

public class Approximation {
    private double a;
    private double b;
    private int n;
    private Function<Double, Double> mathFunction;

    public Approximation(double a, double b, int n, Function<Double, Double> mathFunction) {
        this.a = a;
        this.b = b;
        this.n = n;
        this.mathFunction = mathFunction;
    }

    public void leastSquaresApproximation(int x){
        Matrix result = new Matrix(n + 1);
        double res = 0;
        result = fill3x3Matrix(result);
        result = fillLastColumnMatrix(result);
        result.oblicz();
        for (int i = 0; i < result.wyniki.length; i++) {
            res += result.wyniki[i] * Math.pow(x, i);
        }
        System.out.println("Approximation result: " + res);
    }
    private Matrix fill3x3Matrix(Matrix matrix){
        Integral integral;
        for (int i = 0; i < matrix.kolumny - 1; i++) {
            for (int j = 0; j < matrix.wiersze; j++) {
                int pow1 = j;
                int pow2 = i;
                Function<Double, Double> function = x -> Math.pow(x, pow1) * Math.pow(x, pow2);
                integral = new Integral(a,b,function);
                matrix.setWartosc(matrix,i,j,integral.trapezoidMethod( 10));
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
            matrix.setWartosc(matrix,i,lastColumn,integral.trapezoidMethod(8));
        }
        return matrix;
    }

    public static void main(String[] args) {
        Approximation approximation1 = new Approximation(1,3,2,x -> Math.sqrt(x));
        approximation1.leastSquaresApproximation(1);
        approximation1.leastSquaresApproximation(2);
    }
}


