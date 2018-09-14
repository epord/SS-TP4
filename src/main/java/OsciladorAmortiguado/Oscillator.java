package OsciladorAmortiguado;

import OsciladorAmortiguado.ForceCalculators.OscillatorForce;
import OsciladorAmortiguado.StepCalculators.LeapFrogVelvet;
import OsciladorAmortiguado.StepCalculators.StepCalculator;

import java.util.ArrayList;
import java.util.List;

public class Oscillator {

    public void start() {
        StepCalculator stepCalculator = new LeapFrogVelvet(new OscillatorForce());

        List<Particle> particles = new ArrayList<>();
        Particle p = new Particle(new Vector(1.0, 0.0), new Vector(0.0, 0.0), 70.0);
        particles.add(p);

        Double deltaT = 0.01;
        Double currentTime = 0.0;
        Double timeLimit = 1.0;

        while(currentTime < timeLimit) {
            for (Particle particle : particles) {
                System.out.print(particle.getPosition().x + " ");
            }
            stepCalculator.updateParticles(particles, deltaT);
            currentTime += deltaT;
        }
    }

}
