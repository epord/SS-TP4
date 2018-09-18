import CalculationMethods.Implementations.BeemanCalculator;
import CalculationMethods.Implementations.GearCalculator;
import CalculationMethods.Implementations.LeapFrogVelvetCalculator;
import CalculationMethods.StepCalculator;
import Oscillator.GearOscillatorUtils;
import Oscillator.OscilatorMetrics;
import Oscillator.OscillatorForce;
import Oscillator.OscillatorSimulator;
import Planets.PlanetsForce;
import Planets.PlanetsSimulator;
import experiments.ExperimentStatsHolder;
import experiments.ExperimentsStatsAgregator;
import experiments.Operation;
import models.Particle;
import models.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        runOscillatorSimulation();
//        runPlanetsSimulation();
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) / 1000.0 + " ms");
    }

    private static void runOscillatorSimulation() {
        Double deltaT = 0.03;
        Double timeLimit = 3.0;

        int id = 0;
        Particle p = new Particle(id++, new Vector(1.0, 0.0), new Vector(0.0, 0.0), new Vector(0.0, 0.0), 70.0);

//        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new OscillatorForce(), deltaT);
//        StepCalculator stepCalculator = new BeemanCalculator(new OscillatorForce(), deltaT,  Arrays.asList(p));
        StepCalculator stepCalculator = new GearCalculator(new OscillatorForce(), deltaT, new GearOscillatorUtils(), Collections.singletonList(p));

        OscillatorSimulator oscillator = new OscillatorSimulator(deltaT, timeLimit, stepCalculator, p);
        ExperimentsStatsAgregator<OscilatorMetrics> agregator = new ExperimentsStatsAgregator<>();
        agregator.addStatsHolder(oscillator.start());
        System.out.println(agregator.buildStatsOutput(Arrays.asList(Operation.MEAN)));
    }


    private static void runPlanetsSimulation() {
        Double au2m = 149597870700.0;         // astronomical units to meters
        Double kph2mps = 1000.0 / 60.0 / 60.0;  // km/h to m/s
        Double day2s = 86400.0;                 // day to seconds
        Double aupd2mps = au2m / day2s;         // au/day to m/s

        Double deltaT = day2s / 24.0;
        Double timeLimit = 5.0 * (365.0 * day2s);

        int id = 0;
        Particle sun = new Particle(id++,
                new Vector(0.0, 0.0),
                new Vector(0.0, 0.0),
                new Vector(0.0, 0.0),
                1.98847E30);
        Particle earth = new Particle(id++,
                new Vector(9.622458737735928E-01 * au2m, -3.008758740699761E-01 * au2m),
                new Vector( 4.860249247919289E-03 * aupd2mps, 1.635384540738565E-02 * aupd2mps),
                new Vector(0.0, 0.0),
                5.97219E24);
        Particle jupiter = new Particle(id++,
                new Vector(7.075029308889577E-01 * au2m, 5.047888436652260E+00 * au2m),
                new Vector(-7.569079811247934E-03 * aupd2mps, 1.400382906341323E-03 * aupd2mps),
                new Vector(0.0, 0.0),
                1898.13E24);
        Particle saturn = new Particle(id++,
                new Vector(-7.189894984015172E+00 * au2m, 5.711846800529872E+00 * au2m),
                new Vector(-3.778021070773957E-03 * aupd2mps, -4.383919949255058E-03 * aupd2mps),
                new Vector(0.0, 0.0),
                5.6834E26);

        Double maxVoyagerPosition = 10000000.0; // m
        Double maxVoyagerSpeed = 20000.0; // m/s

        Double voyagerSpeed = 0.4 * maxVoyagerSpeed;
        Double voyageHeight = 0.4 * maxVoyagerPosition;
        Double earthRadius = 6371010.0; // m
        Vector voyagerPosition = earth.getPosition().normalize().dot(earth.getPosition().getNorm() + earthRadius + voyageHeight);
        Vector voyagerVelocity = earth.getVelocity().normalize().dot(earth.getVelocity().getNorm() + voyagerSpeed);


        Particle voyager = new Particle(id++,
                voyagerPosition,
                voyagerVelocity,
                new Vector(0.0, 0.0),
                721.9);

        List<Particle> particles = Arrays.asList(sun, earth, jupiter, saturn, voyager);

        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new PlanetsForce(), deltaT);
//        StepCalculator stepCalculator = new BeemanCalculator(new PlanetsForce(), deltaT, particles);
        PlanetsSimulator planetsSimulator = new PlanetsSimulator(deltaT, timeLimit, stepCalculator, particles);
        planetsSimulator.start();
    }
}
