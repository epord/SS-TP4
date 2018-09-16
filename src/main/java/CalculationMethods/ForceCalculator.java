package CalculationMethods;

import Models.Particle;
import Models.Vector;

import java.util.Collection;

public interface ForceCalculator {

    Vector calculateForce(Particle p, Collection<Particle> particles);

    default Vector calculateAcceleration(Particle p, Collection<Particle> particles) {
        return calculateForce(p, particles).dot(1.0/p.getMass());
    }

}
