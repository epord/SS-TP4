package OsciladorAmortiguado.StepCalculators;

import OsciladorAmortiguado.ForceCalculators.ForceCalculator;
import OsciladorAmortiguado.Particle;

import java.util.List;

public abstract class StepCalculator {

    ForceCalculator forceCalculator;

    public StepCalculator(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    public abstract void updateParticles(List<Particle> particles, Double deltaT);

}
