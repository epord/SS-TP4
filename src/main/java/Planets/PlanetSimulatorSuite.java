package Planets;

import CalculationMethods.Implementations.LeapFrogVelvetCalculator;
import CalculationMethods.StepCalculator;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import experiments.DataPoint;
import experiments.ExperimentStatsHolder;
import experiments.ExperimentsStatsAgregator;
import experiments.Operation;
import helpers.AnimationBuilder;
import helpers.FileManager;
import helpers.OctaveBuilder;
import javafx.util.Pair;
import models.Particle;
import models.Vector;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlanetSimulatorSuite {
    Double au2m = 149597870700.0;         // astronomical units to meters
    Double kph2mps = 1000.0 / 60.0 / 60.0;  // km/h to m/s
    Double day2s = 86400.0;                 // day to seconds
    Double aupd2mps = au2m / day2s;         // au/day to m/s

    Double deltaT = day2s / 24.0 / 60.0;
    Double timeLimit = 3.0 * (365.0 * day2s) * 2;

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

        Double closestDistance = Double.MAX_VALUE;
        ExperimentStatsHolder<PlanetMetrics> bestTrayectory = new ExperimentStatsHolder<>();
        OctaveBuilder octaveBuilder = new OctaveBuilder();
        AnimationBuilder animationBuilder = new AnimationBuilder();
        String statusString = "no status";
        Integer closestIndex = -1;
        //List to save the other trayectories, first X and then Y
        List<Pair<List<Double>,List<Double>>> otherTrajectories = new ArrayList<>();

        for (Double speed = startSpeed; speed <= endSpeed; speed += speedStep) {
            for(Double height = startHeight; height <= endHeight; height += heightStep){
//                for(Double days = -500.0; days <= 500.0; days += 50.0){
                for(Double days = 0.0; days <= 0.0; days += 50.0){
                    System.out.println(getStatusString(height,speed,days));
                    //Get the correct start configuration for the start parameters
                    List<Particle> particles = getConfigurationEvolved(height,speed,day2s*days);
                    PlanetsSimulator planetsSimulator = new PlanetsSimulator(deltaT, timeLimit, stepCalculator, particles);
                    ExperimentStatsHolder<PlanetMetrics> singleHolder = planetsSimulator.start();
                    //Extract the results from the single experiment holder
                    DataPoint saturnClosestAproach = singleHolder.getDataSeries(PlanetMetrics.SATURN_CLOSEST_APROACH).get(0);
                    DataPoint jupiterClosestAproach = singleHolder.getDataSeries(PlanetMetrics.JUPITER_CLOSEST_APROACH).get(0);
                    Double totalDistance = singleHolder.getDataSeries(PlanetMetrics.TOTAL_CLOSED).get(0).getValue();

                    //Add the trayectories as an extra, if there is room for other trayectories
                    if(otherTrajectories.size() < maxOtherTrayectories ){
                        otherTrajectories.add(getVoyagerTrayectory(singleHolder));
                    }
                    if(closestDistance > totalDistance){
                        closestDistance = totalDistance;
                        statusString = getStatusString(height,speed,days);
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
        }
        agregator.addStatsHolder(suiteHolder);
        System.out.println(agregator.buildStatsOutput(Arrays.asList(Operation.MEAN)));
        System.out.println("Closest Journey");
        System.out.println(statusString);

        System.out.println("Closest posible distance = " + (60268000.0 + 71492000.0));
        for (PlanetMetrics pm : suiteHolder.getActiveSeries()){
            System.out.println(pm + " = " + suiteHolder.getDataSeries(pm).get(closestIndex).getValue());
        }
        FileManager.writeString("out.csv",ExperimentsStatsAgregator.getFromHolders(bestTrayectory).buildStatsOutput(Operation.MEAN).toString());
        System.out.println("Octave Graph");
        FileManager.writeString("ss4.m",octaveBuilder.getOctaveGrapher(bestTrayectory,otherTrajectories));
        FileManager.writeString("p5/empty-example/output.txt",animationBuilder.getAnimationOutput(bestTrayectory,otherTrajectories));
        System.out.println("Saving the octave grapher function in ss4.m");
        System.out.println(octaveBuilder.getOctaveGrapher(bestTrayectory,otherTrajectories));
//        System.out.println(animationBuilder.getAnimationOutput(bestTrayectory,otherTrajectories));
    }

    public Pair<Double, List<Particle>> findNextSimilarPlanetDisposition(Double endingTime) {
        Double initialAngleSum = null;
        Double initialAngleJup = null;
        Double initialAngleSat = null;
        Double minAngleSumDifference = Double.MAX_VALUE;
        List<Particle> similarDisposition = new ArrayList<>();
        Double timeWithSimilarDisposition = 0.0;

        List<Particle> initialPlanets = getSolarSystemConfiguration(0.0);
        List<Particle> planets = initialPlanets;
        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new PlanetsForce(), deltaT);
        Double currentTime = 0.0;
        while (Math.abs(currentTime) < Math.abs(endingTime)) {
            Vector earth = findById("earth", planets).getPosition();
            Vector jupiter = findById("jupiter", planets).getPosition();
            Vector saturn = findById("saturn", planets).getPosition();

            Double earthAngle = Math.atan2(earth.getY(), earth.getX());
            Double jupiterAngle = Math.atan2(jupiter.getY(), jupiter.getX());
            Double saturnAngle = Math.atan2(saturn.getY(), saturn.getX());

            Double angleJup = jupiterAngle - earthAngle;
            Double angleSat = saturnAngle - earthAngle;
            Double angleSum = angleJup + angleSat;
            if (initialAngleSum == null){
                initialAngleSum = angleSum;
                initialAngleJup = angleJup;
                initialAngleSat = angleSat;
                System.out.println("===INIITAL:===");
                System.out.println("j: " + (jupiterAngle - earthAngle) + " s: " + (saturnAngle - earthAngle));
            } else {
                Double angleSumDifference = Math.abs(initialAngleSum - angleSum);
                if (angleSumDifference < minAngleSumDifference
                        && Math.abs(angleJup - initialAngleJup) < 0.0872665 && Math.abs(angleSat - initialAngleSat) <  0.0872665){
                    minAngleSumDifference = angleSumDifference;
                    similarDisposition = planets;
                    timeWithSimilarDisposition = currentTime;
                    System.out.println("===NEW:" + angleSumDifference + "===");
                    System.out.println("j: " + (jupiterAngle - earthAngle) + " s: " + (saturnAngle - earthAngle));
                }
            }

//            System.out.println(currentTime + "/" + endingTime);
            planets = stepCalculator.updateParticles(planets);
            currentTime += deltaT;
        }

        System.out.println(minAngleSumDifference);
        return new Pair<>(timeWithSimilarDisposition, similarDisposition);
    }

    private Pair<List<Double>,List<Double>> getVoyagerTrayectory(ExperimentStatsHolder<PlanetMetrics> holder){
        return new Pair<>(holder.getDataSeries(PlanetMetrics.VOYAGER_X)
                .stream().map(DataPoint::getValue).collect(Collectors.toList()),
                holder.getDataSeries(PlanetMetrics.VOYAGER_Y)
                        .stream().map(DataPoint::getValue).collect(Collectors.toList()));
    }

    private String getStatusString(Double height, Double speed, Double zeroTime){
        return  "Height = "+ height +"*"+ maxVoyagerPosition +" = "+height*maxVoyagerPosition +
                " meters \nSpeed = " + speed +"*"+maxVoyagerSpeed + " = " +speed*maxVoyagerSpeed + "m/s" +
                "\nStart day = " + zeroTime +" days";
    }

    private List<Particle> getConfigurationEvolved(Double voyagerHeightPercentage, Double voyagerSpeedPercentage, Double zeroTIme){
        List<Particle> bodies = getSolarSystemConfiguration(zeroTIme);
        Particle earth = findById("earth",bodies);
        bodies.add(getVoyager(earth,voyagerSpeedPercentage,voyagerHeightPercentage));
        return bodies;
    }

    private List<Particle> getSolarSystemConfiguration(Double zeroTime) {
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

        List<Particle> bodies = Stream.of(sun,earth,jupiter,saturn).collect(Collectors.toList());
        if(zeroTime != 0){
            StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new PlanetsForce(),(zeroTime<0?-1:1)*deltaT);
            Double currentTime = 0.0;
            while (Math.abs(currentTime) < Math.abs(zeroTime) ){
                bodies = stepCalculator.updateParticles(bodies);
                currentTime += (zeroTime<0?-1:1)*deltaT;
            }
        }

        return bodies;
    }
    private List<Particle> getStartConfiguration(Double voyagerHeightPercentage, Double voyagerSpeedPercentage){
        List<Particle> bodies = getSolarSystemConfiguration(0.0);
        Particle earth = findById("earth",bodies);
        bodies.add(getVoyager(earth,voyagerSpeedPercentage,voyagerHeightPercentage));
//        System.out.println("h: "+ voyagerHeightPercentage + ",s:" + voyagerSpeedPercentage + "||"+ voyager);
        return bodies;
    }

    Particle getVoyager(Particle earth, Double voyagerSpeedPercentage, Double voyagerHeightPercentage){
        Double voyagerSpeed = voyagerSpeedPercentage * maxVoyagerSpeed;
        Double voyageHeight = voyagerHeightPercentage * maxVoyagerPosition;
        Vector voyagerPosition = earth.getPosition().normalize().dot(earth.getPosition().getNorm() + earthRadius + voyageHeight);
        Vector voyagerVelocity = earth.getVelocity().normalize().dot(earth.getVelocity().getNorm() + voyagerSpeed);

        Particle voyager = new Particle("voyager",
                voyagerPosition,
                voyagerVelocity,
                new Vector(0.0, 0.0),
                721.9);
        return voyager;
    }

    Particle findById(String id, List<Particle> particles){
        return particles.stream().filter(p->p.getID().equalsIgnoreCase(id)).findFirst().get();
    }
}
