package Planets;

import CalculationMethods.Implementations.LeapFrogVelvetCalculator;
import CalculationMethods.StepCalculator;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import experiments.DataPoint;
import experiments.ExperimentStatsHolder;
import experiments.ExperimentsStatsAgregator;
import experiments.Operation;
import helpers.OctaveBuilder;
import javafx.util.Pair;
import models.Particle;
import models.Vector;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlanetSimulatorSuite {
    Double au2m = 149597870700.0;         // astronomical units to meters
    Double kph2mps = 1000.0 / 60.0 / 60.0;  // km/h to m/s
    Double day2s = 86400.0;                 // day to seconds
    Double aupd2mps = au2m / day2s;         // au/day to m/s

    Double deltaT = day2s / 24.0;
    Double timeLimit = 5.0 * (365.0 * day2s);

    Double maxVoyagerPosition = 10000000.0; // m
    Double maxVoyagerSpeed = 20000.0; // m/s
    Double earthRadius = 6371010.0; // m

    Double heightStep;
    Double speedStep;

    Double startHeight= 0.0;
    Double endHeight = 1.0;
    Double startSpeed = 0.0;
    Double endSpeed = 1.0;
    public PlanetSimulatorSuite(Double heightStep, Double speedStep) {
        this.heightStep = heightStep;
        this.speedStep = speedStep;
    }

    public PlanetSimulatorSuite(Double startHeight, Double endHeight, Double heightStep, Double startSpeed, Double endSpeed, Double speedStep) {
        this.heightStep = heightStep;
        this.speedStep = speedStep;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.startSpeed = startSpeed;
        this.endSpeed = endSpeed;
    }

    public void run(Integer maxOtherTrayectories){
        ExperimentsStatsAgregator<PlanetMetrics> agregator = new ExperimentsStatsAgregator<>();
        ExperimentStatsHolder<PlanetMetrics> suiteHolder = new ExperimentStatsHolder<>();
        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new PlanetsForce(), deltaT);
//        StepCalculator stepCalculator = new BeemanCalculator(new PlanetsForce(), deltaT, particles);
//        String closestJourney = "Null Journey, Error";
        Double closestDistance = Double.MAX_VALUE;
        ExperimentStatsHolder<PlanetMetrics> bestTrayectory = new ExperimentStatsHolder<>();
        OctaveBuilder octaveBuilder = new OctaveBuilder();
        String statusString = "no status";
        Integer closestIndex = -1;
        List<Pair<List<Double>,List<Double>>> otherTrajectories = new ArrayList<>();

        for (Double speed = startSpeed; speed <= endSpeed; speed += speedStep) {
            for(Double height = startHeight; height <= endHeight; height += heightStep){
                System.out.println(getStatusString(height,speed));
                List<Particle> particles = getStartConfiguration(height,speed);
                PlanetsSimulator planetsSimulator = new PlanetsSimulator(deltaT, timeLimit, stepCalculator, particles);
                ExperimentStatsHolder<PlanetMetrics> singleHolder = planetsSimulator.start();
                //Extract the results from the single experiment holder
                DataPoint saturnClosestAproach = singleHolder.getDataSeries(PlanetMetrics.SATURN_CLOSEST_APROACH).get(0);
                DataPoint jupiterClosestAproach = singleHolder.getDataSeries(PlanetMetrics.JUPITER_CLOSEST_APROACH).get(0);
                Double totalDistance = singleHolder.getDataSeries(PlanetMetrics.TOTAL_CLOSED).get(0).getValue();

                if(otherTrajectories.size() < maxOtherTrayectories ){
                    otherTrajectories.add(new Pair<>(singleHolder.getDataSeries(PlanetMetrics.VOYAGER_X)
                            .stream().map(DataPoint::getValue).collect(Collectors.toList()),
                            singleHolder.getDataSeries(PlanetMetrics.VOYAGER_Y)
                                    .stream().map(DataPoint::getValue).collect(Collectors.toList())));
                }
                if(closestDistance > totalDistance){
                    closestDistance = totalDistance;
                    statusString = getStatusString(height,speed);
                    //This is to get the index of the best configuration
                    closestIndex = suiteHolder.getDataSeries(PlanetMetrics.SATURN_CLOSEST_APROACH).size();
                    bestTrayectory = singleHolder;
                }
                //Add those results to the experiment suite holder
                suiteHolder.addDataPoint(PlanetMetrics.SATURN_CLOSEST_APROACH,0.0,saturnClosestAproach.getValue());
                suiteHolder.addDataPoint(PlanetMetrics.SATURN_CLOSEST_APROACH_TIME,0.0,saturnClosestAproach.getTime()    );
                suiteHolder.addDataPoint(PlanetMetrics.JUPITER_CLOSEST_APROACH,0.0,jupiterClosestAproach.getValue());
                suiteHolder.addDataPoint(PlanetMetrics.JUPITER_CLOSEST_APROACH_TIME,0.0,jupiterClosestAproach.getTime());
                suiteHolder.addDataPoint(PlanetMetrics.TOTAL_CLOSED,0.0,totalDistance);
                suiteHolder.addDataPoint(PlanetMetrics.VOYAGER_HEIGHT,0.0,height);
                suiteHolder.addDataPoint(PlanetMetrics.VOYAGER_SPEED,0.0,speed);
            }
        }
        agregator.addStatsHolder(suiteHolder);
        System.out.println(agregator.buildStatsOutput(Arrays.asList(Operation.MEAN)));
        System.out.println("Closest Journey");
        System.out.println(statusString);
        for (PlanetMetrics pm : suiteHolder.getActiveSeries()){
            System.out.println(pm + " = " + suiteHolder.getDataSeries(pm).get(closestIndex).getValue());
        }
        System.out.println(octaveBuilder.getOctaveGrapher(bestTrayectory,otherTrajectories));
    }

    private String getStatusString(Double height, Double speed){
        return  "Height = "+ height +"*"+ maxVoyagerPosition +" = "+height*maxVoyagerPosition +
                " meters \nSpeed = " + speed +"*"+maxVoyagerSpeed + " = " +speed*maxVoyagerSpeed + "m/s";
    }

    private List<Particle> getStartConfiguration(Double voyagerHeightPercentage, Double voyagerSpeedPercentage){
        Particle sun = new Particle("sun",
                new Vector(0.0, 0.0),
                new Vector(0.0, 0.0),
                new Vector(0.0, 0.0),
                1.98847E30);
        Particle earth = new Particle("earth",
                new Vector(9.622458737735928E-01 * au2m, -3.008758740699761E-01 * au2m),
                new Vector( 4.860249247919289E-03 * aupd2mps, 1.635384540738565E-02 * aupd2mps),
                new Vector(0.0, 0.0),
                5.97219E24);
        Particle jupiter = new Particle("jupiter",
                new Vector(7.075029308889577E-01 * au2m, 5.047888436652260E+00 * au2m),
                new Vector(-7.569079811247934E-03 * aupd2mps, 1.400382906341323E-03 * aupd2mps),
                new Vector(0.0, 0.0),
                1898.13E24);
        Particle saturn = new Particle("saturn",
                new Vector(-7.189894984015172E+00 * au2m, 5.711846800529872E+00 * au2m),
                new Vector(-3.778021070773957E-03 * aupd2mps, -4.383919949255058E-03 * aupd2mps),
                new Vector(0.0, 0.0),
                5.6834E26);


        Double voyagerSpeed = voyagerSpeedPercentage * maxVoyagerSpeed;
        Double voyageHeight = voyagerHeightPercentage * maxVoyagerPosition;
        Vector voyagerPosition = earth.getPosition().normalize().dot(earth.getPosition().getNorm() + earthRadius + voyageHeight);
        Vector voyagerVelocity = earth.getVelocity().normalize().dot(earth.getVelocity().getNorm() + voyagerSpeed);

        Particle voyager = new Particle("voyager",
                voyagerPosition,
                voyagerVelocity,
                new Vector(0.0, 0.0),
                721.9);

//        System.out.println("h: "+ voyagerHeightPercentage + ",s:" + voyagerSpeedPercentage + "||"+ voyager);
        return Arrays.asList(sun, earth, jupiter, saturn, voyager);
    }
}