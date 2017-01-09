/**
 * Created by kevin on 1/8/17.
 */

public abstract class Function {
    double a;
    double b;
    public Function(double h, double k) {
        a = h;
        b = k;
    }
    abstract double output(double input);
}

