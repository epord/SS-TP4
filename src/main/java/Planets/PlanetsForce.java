package Planets;

import CalculationMethods.ForceCalculator;
import Models.Particle;
import Models.Vector;

import java.util.Collection;

public class PlanetsForce implements ForceCalculator {

//    private static final Double G = 1.492708192E-18; // Universal Gravitation Constant [N.ua^2/10^24kg]
//    private static final Double G = 2.980318282E-3; // Universal Gravitation Constant [N.ua^2/10^24kg]
    private static final Double G = 6.673E-11; // Universal Gravitation Constant [N.m^2/kg]

    @Override
    public Vector calculateForce(Particle p, Collection<Particle> particles) {
        Vector force = new Vector(0.0, 0.0);
        for (Particle particle: particles) {
            if (!p.equals(particle)) {
                Double forceIntensity = G * particle.getMass() * p.getMass() / Math.pow(p.getPosition().distance(particle.getPosition()), 2);
                Vector forceDirection = new Vector(particle.getPosition().getX() - p.getPosition().getX(),
                        particle.getPosition().getY() - p.getPosition().getY()).normalize().dot(forceIntensity);
                force = force.plus(forceDirection);
            }
        }
        return force;

    }
}
