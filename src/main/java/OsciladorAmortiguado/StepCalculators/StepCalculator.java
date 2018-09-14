package OsciladorAmortiguado.StepCalculators;

import OsciladorAmortiguado.ForceCalculators.ForceCalculator;
import OsciladorAmortiguado.Particle;

import java.util.List;

public abstract class StepCalculator {

    ForceCalculator forceCalculator;
    Double deltaT;

    public StepCalculator(ForceCalculator forceCalculator, Double deltaT) {
        this.forceCalculator = forceCalculator;
        this.deltaT = deltaT;
    }

    public abstract List<Particle> updateParticles(List<Particle> particles);

}
