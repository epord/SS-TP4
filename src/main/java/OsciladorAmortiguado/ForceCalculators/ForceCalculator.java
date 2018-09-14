package OsciladorAmortiguado.ForceCalculators;

import OsciladorAmortiguado.Particle;
import OsciladorAmortiguado.Vector;

import java.util.Collection;

public interface ForceCalculator {

    Vector calculateForce(Particle p, Collection<Particle> particles);

    Vector calculateAcceleration(Particle p, Collection<Particle> particles);

}
