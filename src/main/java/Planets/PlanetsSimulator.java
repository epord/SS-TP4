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
    private final Double oneDay = 24*60*60.0;

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
        Double lastTimeRecorded = currentTime;
        while(currentTime < timeLimit) {
            Particle voyager = findById("voyager");
            Vector jupiter = findById("jupiter").getPosition();
            Vector saturn = findById("saturn").getPosition();
            //Get the current distance from voyager to jupiter/saturn
            DataPoint saturnDistance = new DataPoint(currentTime,voyager.getPosition().distance(saturn));
            DataPoint jupiterDistance = new DataPoint(currentTime,voyager.getPosition().distance(jupiter));
            if(voyager.getPosition().distance(saturn) < 60268000.0){
                saturnDistance = new DataPoint(currentTime,Double.MAX_VALUE);
            }
            if(voyager.getPosition().distance(jupiter) < 71492000.0){
                jupiterDistance = new DataPoint(currentTime,Double.MAX_VALUE);
            }
            //Add it to the metrics
            if(currentTime - lastTimeRecorded > oneDay ){
                holder.addDataPoint(PlanetMetrics.VOYAGER_MEAN_SPEED,currentTime,voyager.getVelocity().getNorm());
                holder.addDataPoint(PlanetMetrics.TIME,currentTime,currentTime);
                holder.addDataPoint(PlanetMetrics.SATURN_DISTANCE,currentTime,saturnDistance.getValue());
                holder.addDataPoint(PlanetMetrics.JUPITER_DISTANCE,currentTime,jupiterDistance.getValue());
                fillHolderWithPlanetPositions(holder,currentTime);
                lastTimeRecorded = currentTime;
            }

            //See if it is the closest, and save it if it is
            saturnClosestAproach = getMin(saturnClosestAproach, saturnDistance);
            jupiterClosestAproach = getMin(jupiterClosestAproach, jupiterDistance);
            particles = stepCalculator.updateParticles(particles);
            currentTime += deltaT;
        }
        //Add the closest distance to the holder
        holder.addDataPoint(PlanetMetrics.SATURN_CLOSEST_APROACH,saturnClosestAproach.getTime(),saturnClosestAproach.getValue());
        holder.addDataPoint(PlanetMetrics.JUPITER_CLOSEST_APROACH,jupiterClosestAproach.getTime(),jupiterClosestAproach.getValue());

        Double totalDistanceToMinimize = jupiterClosestAproach.getValue() + saturnClosestAproach.getValue();
//        Double totalDistanceToMinimize = Math.sqrt(Math.pow(jupiterClosestAproach.getValue(),2)+Math.pow(saturnClosestAproach.getValue(),2));
        holder.addDataPoint(PlanetMetrics.TOTAL_CLOSED,currentTime, totalDistanceToMinimize);

        return holder;
    }

    Particle findById(String id){
        return particles.stream().filter(p->p.getID().equalsIgnoreCase(id)).findFirst().get();
    }

    DataPoint getMin(DataPoint prevMin, DataPoint curr){
        return prevMin.getValue() < curr.getValue()? prevMin : curr;
    }

    private void fillHolderWithPlanetPositions(ExperimentStatsHolder<PlanetMetrics> holder, Double currentTime){
        holder.addDataPoint(PlanetMetrics.SUN_X,currentTime,findById("sun").getPosition().getX());
        holder.addDataPoint(PlanetMetrics.SUN_Y,currentTime,findById("sun").getPosition().getY());
        holder.addDataPoint(PlanetMetrics.EARTH_X,currentTime,findById("earth").getPosition().getX());
        holder.addDataPoint(PlanetMetrics.EARTH_Y,currentTime,findById("earth").getPosition().getY());
        holder.addDataPoint(PlanetMetrics.JUPITER_X,currentTime,findById("jupiter").getPosition().getX());
        holder.addDataPoint(PlanetMetrics.JUPITER_Y,currentTime,findById("jupiter").getPosition().getY());
        holder.addDataPoint(PlanetMetrics.SATURN_X,currentTime,findById("saturn").getPosition().getX());
        holder.addDataPoint(PlanetMetrics.SATURN_Y,currentTime,findById("saturn").getPosition().getY());
        holder.addDataPoint(PlanetMetrics.VOYAGER_X,currentTime,findById("voyager").getPosition().getX());
        holder.addDataPoint(PlanetMetrics.VOYAGER_Y,currentTime,findById("voyager").getPosition().getY());
    }
}
