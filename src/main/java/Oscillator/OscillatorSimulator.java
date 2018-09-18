package Oscillator;

import experiments.DataPoint;
import experiments.ExperimentStatsHolder;
import models.Particle;
import CalculationMethods.StepCalculator;
import models.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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

    public ExperimentStatsHolder<OscilatorMetrics> start() {
        Double currentTime = 0.0;
        ExperimentStatsHolder<OscilatorMetrics> holder = new ExperimentStatsHolder<>();

        while(currentTime < timeLimit) {
            for (Particle particle : particles) {
                holder.addDataPoint(OscilatorMetrics.SIM_X,currentTime,particle.getPosition().getX());
                Double calcX = OscillatorForce.stepCalculator.apply(particle,currentTime).getX();
                holder.addDataPoint(OscilatorMetrics.CALC_X,currentTime,calcX);
                holder.addDataPoint(OscilatorMetrics.ERROR,currentTime,Math.pow(particle.getPosition().getX() - calcX,2));
                holder.addDataPoint(OscilatorMetrics.TIME,currentTime,currentTime);
                System.out.print(particle.getPosition().getX() + " ");
            }
            particles = stepCalculator.updateParticles(particles);
            currentTime += deltaT;
        }
        Double totalError = holder.getDataSeries(OscilatorMetrics.ERROR)
                .stream()
                .mapToDouble(DataPoint::getValue).average().orElse(0.0);
        holder.addDataPoint(OscilatorMetrics.TOTAL_ERROR,currentTime,totalError);
        return holder;
    }
}
