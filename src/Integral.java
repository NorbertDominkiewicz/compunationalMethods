import java.util.ArrayList;
import java.util.function.Function;
public class Integral {
    private double a;
    private double b;
    private Function<Double, Double> mathFunction;
    public Integral(double a, double b, Function<Double, Double> mathFunction) {
        this.a = a;
        this.b = b;
        this.mathFunction = mathFunction;
    }
    public double trapezoidMethod(int n){
        double h;
        double result;
        h = (b - a) / n;
        result = h * (mathFunction.apply(a)/2 + (mathFunction.apply(b)/2));
        for(double i = 1; i < n; i++){
            result = result + h*(mathFunction.apply(a + (i / n) * (b - a)));
        }
        return result;
    }
    public double simpsonMethod(int n){
        double h;
        double result = 0;
        ArrayList<Double> xi = new ArrayList<>();
        ArrayList<Double> ti = new ArrayList<>();
        for(double i = 0; i <= n; i++) {
            xi.add(a + (i / n) * (b - a));
        }
        h = (xi.getLast() - xi.get(xi.size()-2))/2;
        for(double i = 0; i < n; i++) {
            ti.add((xi.get((int)i+1) + xi.get((int)i))/2);
        }
        result += mathFunction.apply(xi.getFirst());
        for(int i = 0; i < n; i++) {
            result += 4 * mathFunction.apply(ti.get(i));
        }
        for(int i = 1; i < n; i++) {
            result += 2 * mathFunction.apply(xi.get(i));
        }
        result += mathFunction.apply(xi.get(n));
        result *= h/3;
        //System.out.println("Simpson: " + result);
        return result;
    }
    public void quadratureMethod(int n, boolean writeOutput){
        double result = 0;
        double multiplier = (b - a) / 2;
        Scrapper scrapper = new Scrapper(n,writeOutput);
        Element element = scrapper.getElement(n);
        for(int i = 0; i < n; i++) {
            result += element.w.get(i) * mathFunction.apply(multiplier * element.t.get(i) + ((b + a)/2));
        }
        result *= multiplier;
        System.out.println("Quadrature: " + result);
    }
//    public static void main(String[] args) {
//        Integral integral = new Integral(0.5, 1.8,x -> Math.cos(Math.pow(x, 3) + 0.7) / (1.1 + Math.cos(0.3 * x + 0.1)));
//        integral.trapezoidMethod(12);
//        integral.simpsonMethod(12);
//        integral.quadratureMethod(12,false);
//    }
//    public static void main(String[] args) {
//        Integral integral = new Integral(-1,1,x -> 0.5 * ( 3 * Math.pow(x,2) - 1) * Math.sqrt(3 * Math.pow(x, 3) -x + 5));
//        System.out.println(integral.trapezoidMethod(100));
//    }
}
