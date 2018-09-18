package Oscillator;

import models.Particle;
import CalculationMethods.StepCalculator;

import java.util.ArrayList;
import java.util.List;

public class OscillatorSimulator {

    Double deltaT;
    Double timeLimit;
    private StepCalculator stepCalculator;
    private List<Particle> particles = new ArrayList<>();

    public OscillatorSimulator(Double deltaT, Double timeLimit, StepCalculator stepCalculator, Particle particle) {
        this.deltaT = deltaT;
        this.timeLimit = timeLimit;
        this.stepCalculator = stepCalculator;
        this.particles.add(particle);
    }

    public void start() {
        Double currentTime = 0.0;
        while(currentTime < timeLimit) {
            for (Particle particle : particles) {
                System.out.print(particle.getPosition().getX() + " ");
            }
            particles = stepCalculator.updateParticles(particles);
            currentTime += deltaT;
        }
    }
}
