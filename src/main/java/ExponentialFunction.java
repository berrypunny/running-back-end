import java.lang.Math;
public class ExponentialFunction extends Function{
    //represents a function of y = b*e^(ax)
    public ExponentialFunction(double a, double b) {
        super(a, b);
    }

    public double output(double input) {
        return b * Math.exp(a * input);
    }

    public String toString() {
        return "Exponential";
    }

}
