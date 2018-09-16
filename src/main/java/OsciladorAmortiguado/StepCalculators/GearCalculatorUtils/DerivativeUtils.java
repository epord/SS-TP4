package OsciladorAmortiguado.StepCalculators.GearCalculatorUtils;

import OsciladorAmortiguado.Particle;
import OsciladorAmortiguado.Vector;

import java.util.List;

public interface DerivativeUtils {

    List<Vector> calculateDerivatives(Particle p);
    List<Double> getGearPredictorCoefficients();

}
