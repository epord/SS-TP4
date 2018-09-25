package Oscillator;

import models.Particle;
import models.Vector;
import CalculationMethods.ForceCalculator;

import java.util.Collection;
import java.util.function.BiFunction;

public class OscillatorForce implements ForceCalculator {

    private static Double k = 10000.0; // Elastic constant
    private static Double gamma = 100.0; // Damping coefficient
    public static BiFunction<Particle, Double, Vector> stepCalculator = ((p, t) -> {
        return p.getCopyWithPosition(new Vector(Math.exp(-gamma / (2.0 * p.getMass()) * t)
                * Math.cos(Math.sqrt(k/p.getMass() - gamma * gamma / (4*p.getMass()*p.getMass())) * t), 0.0))
                .getPosition();
    });

    public OscillatorForce() { }

    public OscillatorForce(Double k, Double gamma) {
        this.k = k;
        this.gamma = gamma;
    }

    @Override
    public Vector calculateForce(Particle p, Collection<Particle> particles) {
        return p.getPosition().dot(-k).minus(p.getVelocity().dot(gamma));
    }

}
