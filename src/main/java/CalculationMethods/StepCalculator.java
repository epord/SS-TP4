package CalculationMethods;

import models.Particle;

import java.util.List;

public interface StepCalculator {

    List<Particle> updateParticles(List<Particle> particles);

}
