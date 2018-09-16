package Oscillator;

import Models.Particle;
import Models.Vector;
import CalculationMethods.DerivativeUtils;

import java.util.Arrays;
import java.util.List;

public class GearOscillatorUtils implements DerivativeUtils {

    private Double k = 10000.0; // Elastic constant
    private Double gamma = 300.0; // Damping coefficient

    public GearOscillatorUtils() { }

    public GearOscillatorUtils(Double k, Double gamma) {
        this.k = k;
        this.gamma = gamma;
    }

    public List<Vector> calculateDerivatives(Particle p) {
        Vector r = p.getPosition();
        Vector r1 = p.getVelocity();
        Vector r2 = r.dot(-k).minus(r1.dot(-gamma)).dot(1 / p.getMass());
        Vector r3 = r1.dot(-k).minus(r2.dot(-gamma)).dot(1 / p.getMass());
        Vector r4 = r2.dot(-k).minus(r3.dot(-gamma)).dot(1 / p.getMass());
        Vector r5 = r3.dot(-k).minus(r4.dot(-gamma)).dot(1 / p.getMass());

        return Arrays.asList(r, r1, r2, r3 ,r4, r5);
    }

    public List<Double> getGearPredictorCoefficients() {
        return Arrays.asList(3.0/16.0, 251.0/360.0, 1.0, 11.0/18.0, 1.0/6.0, 1.0/60.0);
    }

}
