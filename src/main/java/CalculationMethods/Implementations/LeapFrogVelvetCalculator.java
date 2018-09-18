package CalculationMethods.Implementations;

import CalculationMethods.ForceCalculator;
import CalculationMethods.StepCalculator;
import models.Particle;
import models.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class LeapFrogVelvetCalculator implements StepCalculator {

    private ForceCalculator forceCalculator;
    private Double deltaT;

    public LeapFrogVelvetCalculator(ForceCalculator forceCalculator, Double deltaT) {
        this.forceCalculator = forceCalculator;
        this.deltaT = deltaT;
    }

    @Override
    public List<Particle> updateParticles(List<Particle> particles) {
        List<Particle> updatedParticles = particles.stream().map(p -> {
            Vector a = forceCalculator.calculateAcceleration(p, particles);
            Vector halfVelocity = p.getVelocity().plus(a.dot(deltaT / 2.0)); // v(0.5) = v(0) + a(0) * 0.5
            Vector pos = p.getPosition().plus(halfVelocity.dot(deltaT)); // r(1) = p(0) + v(0.5) * 1

            return p.getCopyWithVelocity(halfVelocity)
                .getCopyWithPosition(pos)
                .getCopyWithAcceleration(a);
        }).collect(Collectors.toList());

        return updatedParticles.stream().map(particle -> {
            Vector a = forceCalculator.calculateAcceleration(particle, updatedParticles);
            Vector velocity = particle.getVelocity().plus(a.dot(deltaT / 2.0));

            return particle.getCopyWithVelocity(velocity).getCopyWithAcceleration(a);
        }).collect(Collectors.toList());

    }

}
