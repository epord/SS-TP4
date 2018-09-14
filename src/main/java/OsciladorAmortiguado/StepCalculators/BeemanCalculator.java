//package OsciladorAmortiguado.StepCalculators;
//
//import OsciladorAmortiguado.ForceCalculators.ForceCalculator;
//import OsciladorAmortiguado.Particle;
//import OsciladorAmortiguado.Vector;
//
//import java.util.*;
//
//public class BeemanCalculator extends StepCalculator {
//
//    List<Particle> previousState;
//
//    public BeemanCalculator(ForceCalculator forceCalculator, Double deltaT, List<Particle> particles) {
//        super(forceCalculator, deltaT);
//        previousState = particles;
//
//        // Calculate previous initial state with Euler
//        for (Particle p: previousState) {
//            Vector a = forceCalculator.calculateAcceleration(p, particles);
//            Vector prevPos = p.getPosition()
//                    .plus(p.getVelocity().dot(-deltaT / 2.0))
//                    .plus(a.dot(deltaT * deltaT / 2));
//            Vector prevVel = p.getVelocity().minus(a.dot(deltaT));
//            p.setVelocity(prevVel);
//            p.setPosition(prevPos);
//            Vector prevAcc = forceCalculator.calculateAcceleration(p, particles);
//            p.setAcceleration(prevAcc);
//        }
//    }
//
//    @Override
//    public void updateParticles(List<Particle> particles) {
//
//        List<Particle> predictedParticles = new ArrayList<>();
//
//        for (Particle p: particles) {
//            Particle previousParticleState = previousState.get(previousState.indexOf(p));
//            Vector a = forceCalculator.calculateAcceleration(p, particles);
//            Vector pos = p.getPosition()
//                    .plus(p.getVelocity().dot(deltaT))
//                    .plus(a.dot(deltaT * deltaT * (2.0 / 3.0)))
//                    .minus(previousParticleState.getVelocity().dot(1.0 / 6.0 * deltaT * deltaT));
//            Vector predictedVel = p.getVelocity()
//                    .plus(a.dot(3.0 / 2.0 * deltaT))
//                    .minus(previousParticleState.getAcceleration().dot(1.0 / 2.0 * deltaT));
//            predictedParticles.add(new Particle(p.getID(), pos, predictedVel, a, p.getMass()));
//
//            p.setPosition(pos); // New real position
//        }
//
//        // Calculating real velocity
//        for (Particle p: particles) {
//            Particle previousParticleState = previousState.get(previousState.indexOf(p));
//            Particle predictedParticleState = predictedParticles.get(predictedParticles.indexOf(p));
//
//            Vector prevAcc = previousParticleState.getAcceleration();
//            Vector currAcc = p.getAcceleration();
//            Vector predictedAcc = forceCalculator.calculateAcceleration(predictedParticleState, predictedParticles); // Acceleration using predicted velocity
//
//            Vector correctedVelocity = p.getVelocity()
//                    .plus(predictedAcc.dot(1.0 / 3.0 * deltaT))
//                    .plus(currAcc.dot(5.0 / 6.0 * deltaT))
//                    .minus(prevAcc.dot(1.0 / 6.0 * deltaT));
//
//            p.setVelocity(correctedVelocity); // New real velocity
//            p.setAcceleration(predictedAcc); // New real acceleration
//        }
//
//        previousState = particles;
//
//    }
//}
