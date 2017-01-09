/**
 * Created by kevin on 1/8/17.
 */
import Jama.Matrix;
public class DataHandler {
    public double[] distribution;
    public DataHandler(double[] d) {
        if (d.length == 7) {
            throw new  IllegalArgumentException("Array not of length 7");
        }
        distribution = d;
    }
    //assumptions made: restdays start at end of the week
    private Function findEffortCurve(Day[] days, int startF, int endF, int recoveryF, double[] runnerCurve, int minrate, int maxrate) {
        ExponentialFunction runner = new ExponentialFunction(runnerCurve[0], runnerCurve[1]); //runner as characterized by curve

        //find where rest days start
        int index = 0;
        while (days[index].length != 0) {
            index ++;
        }
        int[] heartrates = new int[index];
        double[] lengths = new double[index];
        double[] pace = new double[index];
        double[] racePace = new double[index];
        for (int i = 0; i < index; i++) {
            heartrates[i] = days[i].heartrate;
            lengths[i] = days[i].length;
            pace[i] = (double) days[i].time / lengths[i];
            racePace[i] = runner.output(lengths[i]);

        }
        double[] effort = normalizeEffort(heartrates, minrate, maxrate);
        double[] rConstants = recoveryConstants(7 - index, startF, endF, recoveryF, effort, lengths);

        double[] fatigue = new double[index];
        fatigue[0] = startF;
        for (int j = 1; j < index; j++) {
            fatigue[j] = fatigue[j - 1] - rConstants[0] + effort[j]*lengths[j] * rConstants[1];
        }
        double[] speedup = calculateSpeedup(racePace, pace, fatigue);
        Function[] curves = effortCurves(effort, speedup);
        return bestFit(curves, effort, speedup);

    }
    private double[] recoveryConstants(int restDays, int startF, int endF, int recoveryF, double[] effort, double[] miles) {
        int delta = endF - startF;
        double rConst = (double) (endF - recoveryF) / restDays;
        double weightedSum = 0;
        for (int i = 0; i < miles.length; i++) {
            weightedSum += miles[i] * effort[i];
        }
        double fConst = ((double) delta - 7 * rConst) / weightedSum;

        double[] constants = new double[2];
        constants[0] = rConst;
        constants[1] = fConst;
        return constants;

    }

    private double[] normalizeEffort(int[] heartrates, int minrate, int maxrate) {
        double[] effort = new double[heartrates.length];
        for(int i = 0; i < heartrates.length; i++) {
            effort[i] = (double) (heartrates[i] - minrate) / maxrate;
        }
        return effort;
    }

    private double[] calculateSpeedup(double[] race, double[] actual, double[] fatigue) {
        double[] speedup = new double[actual.length];
        for (int i = 0; i < speedup.length; i++) {
            speedup[i] = race[i] / actual[i] * Math.exp(fatigue[i]);
        }
        return speedup;

    }

    //returns the index of the function which gives the least squared error
    private Function bestFit(Function[] functions, double[] input, double[] output) {

        if (input.length != output.length) {
            throw new IllegalArgumentException("input and output arrays should be the same length");
        }

        double[] error = new double[functions.length]; //gets the squared error

        double min = Double.MAX_VALUE;
        int index = 0;

        //finds error for each function
        for (int i = 0; i < functions.length; i++) {

            double squaredErr = 0;
            for (int j = 0; j < output.length; j++) {
                squaredErr += Math.pow(output[j] - functions[i].output(input[j]), 2);
            }

            error[i] = squaredErr;


        }

        for (int k = 0; k < error.length; k++) {

            if (error[k] < min) {
                index = k;
                min = error[k];
            }
        }
        return functions[index];


    }

    private Function[] effortCurves(double[] effort, double[] speedup) {

        Function[] curves = new Function[3];

        curves[0] = linearBestFit(effort, speedup);
        curves[1] = expBestFit(effort, speedup);
        curves[2] = logBestFit(effort, speedup);

        return curves;

    }

    private Function linearBestFit(double[] effort, double[] speedup) {
        double[] constants = regression(effort, speedup);
        return new LinearFunction(constants[0], constants[1]);
    }

    private Function expBestFit(double[] effort, double[] speedup) {

        double[] logged = new double[speedup.length];

        for (int i = 0; i < speedup.length; i++) {
            logged[i] = Math.log(speedup[i]);
        }

        double[] constants = regression(effort, logged);

        return new ExponentialFunction(constants[0], Math.exp(constants[1]));
    }
    private Function logBestFit(double[] effort, double[] speedup) {

        double[] expedited = new double[effort.length];

        for (int i = 0; i < speedup.length; i++) {
            expedited[i] = Math.log(effort[i]);
        }
        double[] constants = regression(expedited, speedup);
        double a = constants[0];
        double b = constants[1];

        return new LogarithmicFunction(a, b);
    }

    private double[] regression(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("input output do not have matching lengths");
        }
        Matrix m = new Matrix(x.length, 2, 1);
        Matrix answer = new Matrix(y.length, 1);
        for (int i = 0; i < x.length; i++) {
            m.set(i, 0, x[i]);
            answer.set(i, 0, y[i]);
        }
        Matrix h = m.transpose().times(m).inverse().times(m.transpose()).times(answer); //linear regression
        double[] constants = new double[2];
        constants[0] = h.get(0, 0);
        constants[1] = h.get(1, 0);
        return constants;

    }

}
