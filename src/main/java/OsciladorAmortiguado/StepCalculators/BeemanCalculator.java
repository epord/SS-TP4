package OsciladorAmortiguado.StepCalculators;

import OsciladorAmortiguado.ForceCalculators.ForceCalculator;
import OsciladorAmortiguado.Particle;
import OsciladorAmortiguado.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class BeemanCalculator extends StepCalculator {

    List<Particle> previousState;

    public BeemanCalculator(ForceCalculator forceCalculator, Double deltaT, List<Particle> particles) {
        super(forceCalculator, deltaT);

        // Calculate previous initial state with Euler
        previousState = particles.stream().map(p -> {
            Vector a = forceCalculator.calculateAcceleration(p, particles);
            Vector prevPos = p.getPosition()
                    .plus(p.getVelocity().dot(-deltaT / 2.0))
                    .plus(a.dot(deltaT * deltaT / 2));
            Vector prevVel = p.getVelocity().minus(a.dot(deltaT));

            Particle tempParticle = p.getCopyWithVelocity(prevVel).getCopyWithPosition(prevPos);
            Vector prevAcc = forceCalculator.calculateAcceleration(tempParticle, particles);
            return tempParticle.getCopyWithAcceleration(prevAcc);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Particle> updateParticles(List<Particle> particles) {

        List<Particle> predictedParticles = particles.stream().map(p -> {
            Particle previousParticleState = previousState.get(previousState.indexOf(p));
            Vector currAcc = forceCalculator.calculateAcceleration(p, particles);
            Vector nextPos = p.getPosition()
                    .plus(p.getVelocity().dot(deltaT))
                    .plus(currAcc.dot(deltaT * deltaT * (2.0 / 3.0)))
                    .minus(previousParticleState.getVelocity().dot(1.0 / 6.0 * deltaT * deltaT));
            Vector predictedVel = p.getVelocity()
                    .plus(currAcc.dot(3.0 / 2.0 * deltaT))
                    .minus(previousParticleState.getAcceleration().dot(1.0 / 2.0 * deltaT));

            return p.getCopyWithAcceleration(currAcc).getCopyWithVelocity(predictedVel).getCopyWithPosition(nextPos);
        }).collect(Collectors.toList());

        List<Particle> nextParticles = predictedParticles.stream().map(p -> {
            Particle previousParticleState = previousState.get(previousState.indexOf(p));
            Particle predictedParticleState = predictedParticles.get(predictedParticles.indexOf(p));

            Vector prevAcc = previousParticleState.getAcceleration();
            Vector currAcc = p.getAcceleration();
            Vector predictedAcc = forceCalculator.calculateAcceleration(predictedParticleState, predictedParticles); // Acceleration using predicted velocity

            Vector correctedVelocity = p.getVelocity()
                    .plus(predictedAcc.dot(1.0 / 3.0 * deltaT))
                    .plus(currAcc.dot(5.0 / 6.0 * deltaT))
                    .minus(prevAcc.dot(1.0 / 6.0 * deltaT));

            return p.getCopyWithVelocity(correctedVelocity).getCopyWithAcceleration(predictedAcc);
        }).collect(Collectors.toList());

        previousState = particles;

        return nextParticles;

    }
}
