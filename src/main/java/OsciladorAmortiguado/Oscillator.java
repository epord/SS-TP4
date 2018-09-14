package OsciladorAmortiguado;

import OsciladorAmortiguado.ForceCalculators.OscillatorForce;
import OsciladorAmortiguado.StepCalculators.BeemanCalculator;
import OsciladorAmortiguado.StepCalculators.LeapFrogVelvetCalculator;
import OsciladorAmortiguado.StepCalculators.StepCalculator;

import java.util.ArrayList;
import java.util.List;

public class Oscillator {

    public void start() {
        Double deltaT = 0.01;
        Double currentTime = 0.0;
        Double timeLimit = 1.0;


        List<Particle> particles = new ArrayList<>();
        int id = 0;
        Particle p = new Particle(id++, new Vector(1.0, 0.0), new Vector(0.0, 0.0), new Vector(0.0, 0.0), 70.0);
        particles.add(p);

//        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new OscillatorForce(), deltaT);
        StepCalculator stepCalculator = new BeemanCalculator(new OscillatorForce(), deltaT, particles);

        while(currentTime < timeLimit) {
            for (Particle particle : particles) {
                System.out.print(particle.getPosition().getX() + " ");
            }
            particles = stepCalculator.updateParticles(particles);
            currentTime += deltaT;
        }
    }

}
