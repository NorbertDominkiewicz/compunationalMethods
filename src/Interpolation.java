public class Interpolation {
    double[]x;
    double[]y;
    double[]px;
    double[]py;

    private double[] a;
    private double[] alpha;

    Interpolation(double[] x, double[] y) {
        if (x.length != y.length){
            System.exit(-1);
        }
        this.x = x;
        this.y = y;

        this.alpha = new double[x.length - 1];
        this.a = new double[4];
    }

    Interpolation(double[] x, double[] y, double[] px, double[] py) {
        if ((x.length != y.length) && (px.length != py.length)){
            System.exit(-1);
        }
        this.x = x;
        this.y = y;
        this.px = px;
        this.py = py;

        this.alpha = new double[x.length - 1];
        this.a = new double[4];

        calculate();
    }

    private double countFirstDegree(int i, double w){
        double licznik = 1;
        double mianownik = 1;
        for(int j = 1; j < x.length; j++){
            if (i == j) continue;
            licznik *= (w - x[j]);
            mianownik *= (x[i] - x[j]);
        }
        return y[i]*(licznik/mianownik);
    }

    private double countSecondDegree(int i, double w){
        double licznik = 1;
        double mianownik = 1;
        for(int j = 0; j < x.length; j++){
            if (i == j) continue;
            licznik *= (w - x[j]);
            mianownik *= (x[i] - x[j]);
        }
        return y[i]*(licznik/mianownik);
    }

    private double countNDegree(int i,double w){
        double licznik = 1;
        double mianownik = 1;
        for(int j = 0; j < x.length; j++){
            if (i == j) continue;
            licznik *= (w - x[j]);
            mianownik *= (x[i] - x[j]);
        }
        return y[i]*(licznik/mianownik);
    }
    public void methodLagrange(double w){
        double wynik = 0;
        for (int i = 0; i < x.length; i++){
            if(i == 0) {
                wynik += countFirstDegree(i, w);
            } else if(i == 1){
                wynik += countSecondDegree(i, w);
            } else {
                wynik += countNDegree(i, w);
            }
        }
        System.out.println("Lagrange: " + wynik);
    }

    public void methodDifferentialNewton(double w){
        double [][] values = new double[x.length][];
        double [] finals = new double[x.length];
        int indexArray = x.length - 1;
        for(int i = 0; i < values.length; i++){
            if(i == 0){
                values[i] = new double[1];
            }
            else{
                values[i] = new double[indexArray];
                indexArray--;
            }
        }
        for(int i = 0; i < x.length; i++){
            if(i == 0){
                finals[0] = y[0];
            }
            if(i == 1){
                for(int j = 0; j < x.length - 1; j++){
                    values[i][j] = (y[j+1] - y[j]) / (x[j+1] - x[j]);
                    if(j ==0){
                        finals[i] = (y[j+1] - y[j]) / (x[j+1] - x[j]);
                    }
                }
            }
            if(i > 1){
                for(int j = 0; j < x.length - i; j++){
                    values[i][j] = (values[i-1][j+1] - values[i-1][j]) / (x[i+j] - x[j]);
                    if(j ==0){
                        finals[i] = (values[i-1][j+1] - values[i-1][j]) / (x[i+j] - x[j]);
                    }
                }
            }
        }
        double wynik = 0;
        for (int i = 0; i < finals.length; i++){
            wynik+= multiply(i, w, finals[i]);
        }
        System.out.println("Differential Newton: " + wynik);
    }

    public void methodProgressiveNewton(double w){
        double h = x[2] - x[1];
        double [][] values = new double[x.length][];
        double [] finals = new double[x.length];
        int indexArray = x.length - 1;

        for(int i = 0; i < values.length; i++){
            if(i == 0){
                values[i] = new double[1];
            }
            else{
                values[i] = new double[indexArray];
                indexArray--;
            }
        }
        for(int i = 0; i < x.length; i++){
            if(i == 0){
                finals[0] = y[0];
            }
            if(i == 1){
                for(int j = 0; j < x.length - 1; j++){
                    values[i][j] = (y[j+1] - y[j]);
                    if(j ==0){
                        finals[i] = (y[j+1] - y[j]);
                    }
                }
            }
            if(i > 1){
                for(int j = 0; j < x.length - i; j++){
                    values[i][j] = (values[i-1][j+1] - values[i-1][j]);
                    if(j ==0){
                        finals[i] = (values[i-1][j+1] - values[i-1][j]);
                    }
                }
            }
        }
        double wynik = 0;
        for (int i = 0; i < finals.length; i++){
            if(i == 0){
                wynik += multiply(i, w, finals[i]);
            } else{
                wynik+= divide(multiply(i, w, finals[i]),i,h);
            }
        }
        System.out.println("Progressive Newton: " + wynik);
    }

    private double divide(double wynik, int k, double h){
        return wynik / (silnia(k) * Math.pow(h,k));
    }

    private double silnia(int k) {
        if (k < 2) {
            return 1;
        }
        return k * silnia(k - 1);
    }

    private double multiply(int k, double w, double howMany){
        double wynik = 1;
        for(int i = 0; i < k; i++){
            wynik *= (w - x[i]);
        }
        return howMany * wynik;
    }

    private void calculate() {
        int n = x.length;
        int toCalculate = 4 + (n - 1);

        Matrix matrix = new Matrix(toCalculate);

        fillMatrixWithWielomians(matrix);
        fillMatrixColumnsBetween(matrix);

        matrix.oblicz();

        for (int i = 0; i < 4; i++) {
            a[i] = matrix.wyniki[i];
        }
        for (int i = 0; i < n - 1; i++) {
            alpha[i] = matrix.wyniki[4 + i];
        }
    }

    private void fillMatrixWithWielomians(Matrix matrix) {
        int rowInd = 0;

        double[] wielomns = wielomian(x[0]);
        for (int i = 0; i < 4; i++) {
            matrix.tab[rowInd][i] = wielomns[i];
        }
        matrix.tab[rowInd][matrix.kolumny - 1] = y[0];
        rowInd++;

        for (int i = 1; i < x.length; i++) {
            wielomns = wielomian(x[i]);

            for (int col = 0; col < 4; col++) {
                matrix.tab[rowInd][col] = wielomns[col];
            }

            for (int s = 1; s <= i; s++) {
                matrix.tab[rowInd][4 + (s-1)] = Math.pow(x[i] - x[s], 3);
            }

            matrix.tab[rowInd][matrix.kolumny - 1] = y[i];
            rowInd++;
        }
    }

    private void fillMatrixColumnsBetween(Matrix matrix) {
        int n = x.length;
        int rowInd = n;

        double[] pochs = wielomianPoch(px[0]);
        for (int i = 0; i < 4; i++) {
            matrix.tab[rowInd][i] = pochs[i];
        }
        matrix.tab[rowInd][matrix.kolumny - 1] = py[0];
        rowInd++;

        pochs = wielomianPoch(px[1]);
        for (int i = 0; i < 4; i++) {
            matrix.tab[rowInd][i] = pochs[i];
        }

        for (int s = 1; s < n; s++) {
            matrix.tab[rowInd][4 + (s-1)] = 3 * Math.pow(px[1] - x[s], 2);
        }

        matrix.tab[rowInd][matrix.kolumny - 1] = py[1];
    }

    public void stickyMethod(double w) {
        double wynik = 0;
        double[] wielomians = wielomian(w);

        for (int i = 0; i < 4; i++) {
            wynik += a[i] * wielomians[i];
        }

        for (int s = 1; s < x.length; s++) {
            if (w > x[s]) {
                wynik += alpha[s-1] * Math.pow(w - x[s], 3);
            }
        }
        System.out.println("Sticky Method: " + wynik);
    }

    private double[] wielomian(double x) {
        double[] values = new double[4];
        for (int i = 0; i < 4; i++) {
            values[i] = Math.pow(x, i);
        }
        return values;
    }

    private double[] wielomianPoch(double x) {
        double[] values = new double[4];
        values[0] = 0;
        for (int i = 1; i < 4; i++) {
            values[i] = i * Math.pow(x, i - 1);
        }
        return values;
    }

    public static void main(String[] args) {
//        Interpolation ob3 = new Interpolation(new double[] { -4, -2, 0, 2, 4 }, new double[] { -96, 6, -4, -30, -360 });
//        ob3.methodLagrange(3);
//        Interpolation ob4 = new Interpolation(new double[] { -4, -2, 0, 2, 4}, new double[] { -96, 6, -4, -30, -360 });
//        ob4.methodDifferentialNewton(3);
        //Interpolation ob5 = new Interpolation(new double[] { -4, -2, 0, 2, 4}, new double[] { -96, 6, -4, -30, -360 });
        //ob5.methodProgressiveNewton(3);
        Interpolation ob7 = new Interpolation(new double[] { -4, -2, 0, 2, 4}, new double[] { 405, 13, -3, -21, 517 }, new double[] {-4, -442}, new double[] {4, 534});
        ob7.stickyMethod(3);
//        Interpolation ob6 = new Interpolation(new double[] { -4, -2, 0, 2, 4}, new double[] { -96, 6, -4, -30, -360 }, new double[] { -4, 4 }, new double[] { 143, -337 });
//        ob6.stickyMethod(3);
    }
}
