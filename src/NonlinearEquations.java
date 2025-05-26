import com.google.common.base.Function;

class Result{
    double x;
    double fx;
    int n;
    Result(double x, double fx, int n){
        this.x = x;
        this.fx = fx;
        this.n = n;
    }
    @Override
    public String toString(){
        return "x: " + x + ", wartosc f(x): " + fx + ", iteracje: " + n;
    }
}

public class NonlinearEquations {
    private double a;
    private double b;
    private final Function<Double, Double> mathFuncution;
    private final Function<Double, Double> derFuncution1;
    private final Function<Double, Double> derFuncution2;
    public NonlinearEquations(double a, double b, Function<Double, Double> mathFuncution, Function<Double, Double> derFuncution1, Function<Double, Double> derFuncution2) {
        this.a = a;
        this.b = b;
        this.mathFuncution = mathFuncution;
        this.derFuncution1 = derFuncution1;
        this.derFuncution2 = derFuncution2;
    }
    private boolean checkFunction() {
        return mathFuncution.apply(a) * mathFuncution.apply(b) < 0 ? false : true;
    }

    public void bisectionMethod(double e){
        if (checkFunction()) {
            System.out.println("Warunek konieczny nie jest spełniony");
        } else {
            int n = 0;
            while (true) {
                n++;
                double xSr = (a + b) / 2.0;
                if (mathFuncution.apply(xSr) == 0) {
                    System.out.println("Koniec działania metody");
                    System.out.println(new Result(xSr, mathFuncution.apply(xSr),n));
                    break;
                }
                if (mathFuncution.apply(a) * mathFuncution.apply(xSr) < 0) {
                    b = xSr;
                } else {
                    a = xSr;
                }
                if (Math.abs(mathFuncution.apply(xSr)) < e) {
                    System.out.println("Koniec działania metody");
                    System.out.println("Wynik Bisekcji : ");
                    System.out.println(new Result(xSr, mathFuncution.apply(xSr),n));
                    break;
                }
            }
        }
    }

    public void newtonMethod(double e) {
        double xn1 = 0;
        double xn = 0;
        if (checkFunction()) {
            System.out.println("Warunek konieczny nie jest spełniony");
        } else {
            if(derFuncution2.apply(a) * mathFuncution.apply(a) > 0) {
                xn = a;
            } else {
                xn = b;
            }
            int n = 0;
            while (true) {
                n++;
                xn1 = xn - mathFuncution.apply(xn) / derFuncution1.apply(xn);
                if(Math.abs(mathFuncution.apply(xn1)) < e || Math.abs(xn1 - xn) < e) {
                    System.out.println("Koniec działania metody");
                    System.out.println("Wynik Newtona: ");
                    System.out.println(new Result(xn1, mathFuncution.apply(xn1),n));
                    break;
                }
                xn = xn1;
            }
        }
    }
    public void secantMethod(double e){
        double xn;
        double xn1;

        if (checkFunction()) {
            System.out.println("Warunek konieczny nie jest spełniony");
        } else {
            if(mathFuncution.apply(a) * derFuncution2.apply(a) > 0) {
                xn = b;
                int n = 0;
                while(true){
                    n++;
                    xn1 = xn - mathFuncution.apply(xn) / (mathFuncution.apply(xn) - mathFuncution.apply(a)) * (xn - a);
                    if(Math.abs(mathFuncution.apply(xn1)) < e || Math.abs(xn1 - xn) < e) {
                        System.out.println("Koniec działania metody");
                        System.out.println("Wynik Siecznych : ");
                        System.out.println(new Result(xn1, mathFuncution.apply(xn1),n));
                        break;
                    }
                    xn = xn1;
                }

            } else if(mathFuncution.apply(b) * derFuncution2.apply(b) > 0) {
                xn = a;
                int n = 0;
                while(true){
                    n++;
                    xn1 = xn - mathFuncution.apply(xn) / (mathFuncution.apply(b) - mathFuncution.apply(xn)) * (b - xn);
                    if(Math.abs(mathFuncution.apply(xn1)) < e || Math.abs(xn1 - xn) < e) {
                        System.out.println("Koniec działania metody");
                        System.out.println("Wynik Siecznych: ");
                        System.out.println(new Result(xn1, mathFuncution.apply(xn1),n));
                        break;
                    }
                    xn = xn1;
                }
            }
        }
    }

    public static void main(String[] args) {
        NonlinearEquations nq = new NonlinearEquations(-4, -1, x -> 12 * Math.pow(x, 2) - 6 * x - 32, x -> 24 * x - 6, x -> 24.0);
        nq.bisectionMethod(0.0001);
        nq.secantMethod(0.02);
        nq.newtonMethod(0.0001);
    }
}
