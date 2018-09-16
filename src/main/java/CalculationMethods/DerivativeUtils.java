package CalculationMethods;

import Models.Particle;
import Models.Vector;

import java.util.List;

public interface DerivativeUtils {

    List<Vector> calculateDerivatives(Particle p);
    List<Double> getGearPredictorCoefficients();

}
