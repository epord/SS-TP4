package Planets;

import CalculationMethods.StepCalculator;
import experiments.DataPoint;
import experiments.ExperimentStatsHolder;
import models.Particle;
import models.Vector;

import java.util.ArrayList;
import java.util.List;

public class PlanetsSimulator {
    private Double deltaT;
    private Double timeLimit;
    private StepCalculator stepCalculator;
    private List<Particle> particles;
    private Double au2m = 149597870000.700;
    int finalFrameCount = 1000;

    public PlanetsSimulator(Double deltaT, Double timeLimit, StepCalculator stepCalculator, List<Particle> particles) {
        this.deltaT = deltaT;
        this.timeLimit = timeLimit;
        this.stepCalculator = stepCalculator;
        this.particles = particles;
    }

    public ExperimentStatsHolder<PlanetMetrics> start() {
        ExperimentStatsHolder<PlanetMetrics> holder = new ExperimentStatsHolder<>();
        Double currentTime = 0.0;


        Double jupiterClosestAproach = Double.MAX_VALUE;
        Double saturnClosestAproach = Double.MAX_VALUE;
        while(currentTime < timeLimit) {
            Vector sun = findById("sun").getPosition();
            Vector voyager = findById("voyager").getPosition();
            Vector jupiter = findById("jupiter").getPosition();
            Vector saturn = findById("saturn").getPosition();
            jupiterClosestAproach = getMin(jupiterClosestAproach, voyager.minus(jupiter).getNorm());
            saturnClosestAproach = getMin(saturnClosestAproach, voyager.minus(saturn).getNorm());
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
        holder.addDataPoint(PlanetMetrics.SATURN_CLOSEST_APROACH,currentTime,saturnClosestAproach);
        holder.addDataPoint(PlanetMetrics.JUPITER_CLOSEST_APROACH,currentTime,jupiterClosestAproach);
        holder.addDataPoint(PlanetMetrics.TOTAL_CLOSED,currentTime,jupiterClosestAproach+saturnClosestAproach);

//        System.out.println(getOctaveGrapher(holder));
        return holder;
    }

    Particle findById(String id){
        return particles.stream().filter(p->p.getID().equalsIgnoreCase(id)).findFirst().get();
    }

    Double getMin(Double prevMin, Double curr){
        return prevMin < curr? prevMin : curr;
    }

    String getOctaveGrapher(ExperimentStatsHolder<PlanetMetrics> holder){
        // Octave output
        StringBuilder sb = new StringBuilder();
        sb.append("function solar()\n");

        sb.append(getPlanetPositions("xsun",holder.getDataSeries(PlanetMetrics.SUN_X)));
        sb.append(getPlanetPositions("ysun",holder.getDataSeries(PlanetMetrics.SUN_Y)));
        sb.append(getPlanetPositions("xear",holder.getDataSeries(PlanetMetrics.EARTH_X)));
        sb.append(getPlanetPositions("year",holder.getDataSeries(PlanetMetrics.EARTH_Y)));
        sb.append(getPlanetPositions("xjup",holder.getDataSeries(PlanetMetrics.JUPITER_X)));
        sb.append(getPlanetPositions("yjup",holder.getDataSeries(PlanetMetrics.JUPITER_Y)));
        sb.append(getPlanetPositions("xsat",holder.getDataSeries(PlanetMetrics.SATURN_X)));
        sb.append(getPlanetPositions("ysat",holder.getDataSeries(PlanetMetrics.SATURN_Y)));
        sb.append(getPlanetPositions("xvoy",holder.getDataSeries(PlanetMetrics.VOYAGER_X)));
        sb.append(getPlanetPositions("yvoy",holder.getDataSeries(PlanetMetrics.VOYAGER_Y)));

        sb.append("plot(xsun, ysun, 'x', 'color', 'r', xear, year, 'b', xjup, yjup, 'm', xsat, ysat, 'k', xvoy, yvoy, 'c')\n");
        sb.append("axis([-10 10 -10 10])\n");
        sb.append("axis square\n");
        sb.append("endfunction\n");
        return sb.toString();
    }

    String getPlanetPositions(String name, List<DataPoint> values){
        int frame = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name+"=[");
        for (DataPoint x : values)
            if (frame++ % (values.size() / finalFrameCount) == 0) {
                stringBuilder.append(x.getValue()/au2m + " ");
            }
        stringBuilder.append("];\n");
        return stringBuilder.toString();
    }
}
