package OsciladorAmortiguado.ForceCalculators;

import OsciladorAmortiguado.Particle;
import OsciladorAmortiguado.Vector;

import java.util.Collection;

public class OscillatorForce implements ForceCalculator {

    private Double k = 10000.0; // Elastic constant
    private Double gamma = 300.0; // Damping coefficient

    @Override
    public Vector calculateForce(Particle p, Collection<Particle> particles) {
        return p.getPosition().dot(-k).minus(p.getVelocity().dot(gamma));
    }

    @Override
    public Vector calculateAcceleration(Particle p, Collection<Particle> particles) {
        return calculateForce(p, particles).dot(1/p.getMass());
    }

}
