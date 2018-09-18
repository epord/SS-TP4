package CalculationMethods;

import models.Particle;
import models.Vector;

import java.util.List;

public interface DerivativeUtils {

    List<Vector> calculateDerivatives(Particle p);
    List<Double> getGearPredictorCoefficients();

}
