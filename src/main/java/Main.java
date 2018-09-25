import CalculationMethods.Implementations.BeemanCalculator;
import CalculationMethods.Implementations.GearCalculator;
import CalculationMethods.Implementations.LeapFrogVelvetCalculator;
import CalculationMethods.StepCalculator;
import Oscillator.GearOscillatorUtils;
import Oscillator.OscilatorMetrics;
import Oscillator.OscillatorForce;
import Oscillator.OscillatorSimulator;
import Planets.PlanetSimulatorSuite;
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
//        runOscillatorSimulation();
        runPlanetsSimulation();
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
//        PlanetSimulatorSuite planetSimulatorSuite = new PlanetSimulatorSuite(0.2,0.01);
        //Run a suite of tests to determine the best configuration. It will iterate from start to end,
        //incrementing every time by the step. If start = end, only that value will be tested.
        //It will test every speed with every height, so be careful with the steps
        PlanetSimulatorSuite planetSimulatorSuite =
//                new PlanetSimulatorSuite(1.0,1.0,0.2,0.5278,0.5280,0.00001);
//                new PlanetSimulatorSuite(1.0,1.0,0.2,0.52788,0.52790,0.000001);
//                new PlanetSimulatorSuite(1.0,1.0,0.2,0.527884,0.527886,0.0000001);
//                new PlanetSimulatorSuite(0.5,0.5,0.1,0.6,0.8,0.02);
//                new PlanetSimulatorSuite(0.5,0.5,0.02,0.779,0.781,0.0002);
//                new PlanetSimulatorSuite(0.5,0.5,0.02,0.7797,0.7799,0.00002);
                new PlanetSimulatorSuite(0.5,0.5,0.02,0.77974,0.77974,0.00002);
//                new PlanetSimulatorSuite(0.499,0.501,0.0002,0.7797,0.7797,0.00002);
//                new PlanetSimulatorSuite(0.562189,1.1,1.1,0.773,0.8,0.1);
//                new PlanetSimulatorSuite(0.0,1.0,0.2,0.70,0.90,0.01);
        //Set the max suboptimal trayectories to draw
        planetSimulatorSuite.run(10);
    }

    private static void runWithSearch(){

    }


}
