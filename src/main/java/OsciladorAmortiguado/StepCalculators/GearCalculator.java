package OsciladorAmortiguado.StepCalculators;

import OsciladorAmortiguado.ForceCalculators.ForceCalculator;
import OsciladorAmortiguado.Particle;
import OsciladorAmortiguado.StepCalculators.GearCalculatorUtils.DerivativeUtils;
import OsciladorAmortiguado.Vector;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GearCalculator extends StepCalculator {

    private HashMap<Particle, List<Vector>> derivativesMap = new HashMap<>();
    private DerivativeUtils derivativeUtils;

    // Taylor constants
    private Double c0, c1, c2, c3, c4, c5;

    public GearCalculator(ForceCalculator forceCalculator, Double deltaT, DerivativeUtils derivativeUtils, Collection<Particle> particles) {
        super(forceCalculator, deltaT);
        this.derivativeUtils = derivativeUtils;
        for (Particle p: particles) {
            derivativesMap.put(p, derivativeUtils.calculateDerivatives(p));
        }
        c0 = 1.0;
        c1 = deltaT;
        c2 = Math.pow(deltaT, 2) / 2.0;
        c3 = Math.pow(deltaT, 3) / 6.0;
        c4 = Math.pow(deltaT, 4) / 24.0;
        c5 = Math.pow(deltaT, 5) / 120.0;
    }




    @Override
    public List<Particle> updateParticles(List<Particle> particles) {
        List<Particle> predictedParticles = particles.stream().map(p -> {
            List<Vector> derivatives = this.derivativesMap.get(p);

            Vector r = derivatives.get(0);
            Vector r1 = derivatives.get(1);
            Vector r2 = derivatives.get(2);
            Vector r3 = derivatives.get(3);
            Vector r4 = derivatives.get(4);
            Vector r5 = derivatives.get(5);

            // 1) Calculating predicted derivatives
            Vector predictedR = r.dot(c0)
                    .plus(r1.dot(c1))
                    .plus(r2.dot(c2))
                    .plus(r3.dot(c3))
                    .plus(r4.dot(c4))
                    .plus(r5.dot(c5));

            Vector predictedR1 = r1.dot(c0)
                    .plus(r2.dot(c1))
                    .plus(r3.dot(c2))
                    .plus(r4.dot(c3))
                    .plus(r5.dot(c4));

            Vector predictedR2 = r2.dot(c0)
                    .plus(r3.dot(c1))
                    .plus(r4.dot(c2))
                    .plus(r5.dot(c3));

            Vector predictedR3 = r3.dot(c0)
                    .plus(r4.dot(c1))
                    .plus(r5.dot(c2));

            Vector predictedR4 = r4.dot(c0)
                    .plus(r5.dot(c1));

            Vector predictedR5 = r5.dot(c0);

            derivativesMap.put(p, Arrays.asList(predictedR, predictedR1, predictedR2, predictedR3, predictedR4, predictedR5));

            return p.getCopyWithAcceleration(predictedR2).getCopyWithVelocity(predictedR1).getCopyWithPosition(predictedR);
        }).collect(Collectors.toList());

        return predictedParticles.stream().map(p -> {
            // 2) Caclulating deltaR2
            Vector realAcc = forceCalculator.calculateAcceleration(p, predictedParticles);
            Vector predictedAcc = p.getAcceleration();

            Vector deltaAcc = realAcc.minus(predictedAcc);
            Vector deltaR2 = deltaAcc.dot(deltaT * deltaT / 2.0);


            // 3) Correcting derivatives
            List<Double> alphas = derivativeUtils.getGearPredictorCoefficients();
            List<Vector> predictedDerivatives = derivativesMap.get(p);

            Vector correctedR = predictedDerivatives.get(0)
                    .plus(deltaR2.dot(alphas.get(0) * 1.0 / Math.pow(deltaT, 0)));

            Vector correctedR1 = predictedDerivatives.get(1)
                    .plus(deltaR2.dot(alphas.get(1) * 1.0 / Math.pow(deltaT, 1)));

            Vector correctedR2 = predictedDerivatives.get(2)
                    .plus(deltaR2.dot(alphas.get(2) * 2.0 / Math.pow(deltaT, 2)));

            Vector correctedR3 = predictedDerivatives.get(3)
                    .plus(deltaR2.dot(alphas.get(3) * 6.0 / Math.pow(deltaT, 3)));

            Vector correctedR4 = predictedDerivatives.get(4)
                    .plus(deltaR2.dot(alphas.get(4) * 24.0 / Math.pow(deltaT, 4)));

            Vector correctedR5 = predictedDerivatives.get(5)
                    .plus(deltaR2.dot(alphas.get(5) * 120.0 / Math.pow(deltaT, 5)));

            derivativesMap.put(p, Arrays.asList(correctedR, correctedR1, correctedR2, correctedR3, correctedR4, correctedR5));

            return p.getCopyWithAcceleration(correctedR2).getCopyWithVelocity(correctedR1).getCopyWithPosition(correctedR);
        }).collect(Collectors.toList());
    }
}
