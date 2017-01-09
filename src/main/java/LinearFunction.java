public class LinearFunction extends Function {
    //represents a function of type y = ax + b
    public LinearFunction(double a, double b) {
        super(a, b);
    }
    public double output(double input) {
        return a * input + b;
    }

    public String toString() {
        return "Linear";
    }
}