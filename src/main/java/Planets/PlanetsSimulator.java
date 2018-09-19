package Planets;

import CalculationMethods.StepCalculator;
import experiments.DataPoint;
import experiments.ExperimentStatsHolder;
import models.Particle;
import models.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PlanetsSimulator {
    private Double deltaT;
    private Double timeLimit;
    private StepCalculator stepCalculator;
    private List<Particle> particles;

    public PlanetsSimulator(Double deltaT, Double timeLimit, StepCalculator stepCalculator, List<Particle> particles) {
        this.deltaT = deltaT;
        this.timeLimit = timeLimit;
        this.stepCalculator = stepCalculator;
        this.particles = particles;
    }

    public ExperimentStatsHolder<PlanetMetrics> start() {
        ExperimentStatsHolder<PlanetMetrics> holder = new ExperimentStatsHolder<>();
        Double currentTime = 0.0;


        DataPoint jupiterClosestAproach = new DataPoint(0.0,Double.MAX_VALUE);
        DataPoint saturnClosestAproach = new DataPoint(0.0,Double.MAX_VALUE);
        while(currentTime < timeLimit) {
            Vector sun = findById("sun").getPosition();
            Vector voyager = findById("voyager").getPosition();
            Vector jupiter = findById("jupiter").getPosition();
            Vector saturn = findById("saturn").getPosition();
            //This is suposed to be like that?
            jupiterClosestAproach = getMin(jupiterClosestAproach, new DataPoint(currentTime,voyager.minus(jupiter).getNorm()));
            saturnClosestAproach = getMin(saturnClosestAproach, new DataPoint(currentTime,voyager.minus(saturn).getNorm()));
            holder.addDataPoint(PlanetMetrics.SUN_X,currentTime,sun.getX());
            holder.addDataPoint(PlanetMetrics.SUN_Y,currentTime,sun.getY());
            holder.addDataPoint(PlanetMetrics.EARTH_X,currentTime,findById("earth").getPosition().getX());
            holder.addDataPoint(PlanetMetrics.EARTH_Y,currentTime,findById("earth").getPosition().getY());
            holder.addDataPoint(PlanetMetrics.JUPITER_X,currentTime,jupiter.getX());
            holder.addDataPoint(PlanetMetrics.JUPITER_Y,currentTime,jupiter.getY());
            holder.addDataPoint(PlanetMetrics.SATURN_X,currentTime,saturn.getX());
            holder.addDataPoint(PlanetMetrics.SATURN_Y,currentTime,saturn.getY());
            holder.addDataPoint(PlanetMetrics.VOYAGER_X,currentTime,findById("voyager").getPosition().getX());
            holder.addDataPoint(PlanetMetrics.VOYAGER_Y,currentTime,findById("voyager").getPosition().getY());
            particles = stepCalculator.updateParticles(particles);
            currentTime += deltaT;
        }
        holder.addDataPoint(PlanetMetrics.SATURN_CLOSEST_APROACH,saturnClosestAproach.getTime(),saturnClosestAproach.getValue());
        holder.addDataPoint(PlanetMetrics.JUPITER_CLOSEST_APROACH,jupiterClosestAproach.getTime(),jupiterClosestAproach.getValue());
        holder.addDataPoint(PlanetMetrics.TOTAL_CLOSED,currentTime,
                Math.sqrt(jupiterClosestAproach.getValue()*jupiterClosestAproach.getValue()+saturnClosestAproach.getValue()*saturnClosestAproach.getValue()));

        return holder;
    }

    Particle findById(String id){
        return particles.stream().filter(p->p.getID().equalsIgnoreCase(id)).findFirst().get();
    }

    DataPoint getMin(DataPoint prevMin, DataPoint curr){
        return prevMin.getValue() < curr.getValue()? prevMin : curr;
    }

}
