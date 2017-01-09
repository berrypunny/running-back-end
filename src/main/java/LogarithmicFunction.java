import java.lang.Math;
public class LogarithmicFunction extends Function{
    //represents a function of form y = a * ln(input) + b
    public LogarithmicFunction(double a, double b) {
        super(a, b);
    }
    public double output(double input) {
        return Math.log(input) * a + b;
    }

    @Override
    public String toString() {
        return "log";

    }
}