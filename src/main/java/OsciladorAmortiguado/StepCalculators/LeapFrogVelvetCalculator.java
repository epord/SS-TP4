package OsciladorAmortiguado.StepCalculators;

import OsciladorAmortiguado.ForceCalculators.ForceCalculator;
import OsciladorAmortiguado.Particle;
import OsciladorAmortiguado.StepCalculators.StepCalculator;
import OsciladorAmortiguado.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeapFrogVelvetCalculator extends StepCalculator {

    public LeapFrogVelvetCalculator(ForceCalculator forceCalculator, Double deltaT) {
        super(forceCalculator, deltaT);
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
