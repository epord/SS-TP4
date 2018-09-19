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
        PlanetSimulatorSuite planetSimulatorSuite =
                new PlanetSimulatorSuite(1.0,1.0,0.2,0.4,0.6,0.02);
        planetSimulatorSuite.run(10);
    }


}
