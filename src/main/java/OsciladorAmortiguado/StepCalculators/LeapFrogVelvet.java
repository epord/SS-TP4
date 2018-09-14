package OsciladorAmortiguado.StepCalculators;

import OsciladorAmortiguado.ForceCalculators.ForceCalculator;
import OsciladorAmortiguado.Particle;
import OsciladorAmortiguado.StepCalculators.StepCalculator;
import OsciladorAmortiguado.Vector;

import java.util.List;

public class LeapFrogVelvet extends StepCalculator {

    public LeapFrogVelvet(ForceCalculator forceCalculator) {
        super(forceCalculator);
    }

    @Override
    public void updateParticles(List<Particle> particles, Double deltaT) {
        for (Particle p: particles) {
            Vector a = forceCalculator.calculateAcceleration(p, particles);
            Vector halfVelocity = p.getVelocity().plus(a.dot(deltaT / 2.0));
            Vector pos = p.getPosition().plus(halfVelocity.dot(deltaT));

            p.setVelocity(halfVelocity);
            p.setPosition(pos);
        }

        // Calculate v(t + deltaT) starting with v(t + deltaT/2)
        for (Particle p: particles) {
            Vector a = forceCalculator.calculateAcceleration(p, particles);
            Vector velocity = p.getVelocity().plus(a.dot(deltaT / 2.0));

            p.setVelocity(velocity);
        }

    }

}
