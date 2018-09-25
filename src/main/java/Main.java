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
import helpers.FileManager;
import models.Particle;
import models.Vector;

import java.util.ArrayList;
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
        Double deltaT = 0.01;
        Double timeLimit = 5.0;

        int id = 0;
        Particle p;
        p = new Particle(id++, new Vector(1.0, 0.0), new Vector(-100.0/(2.0 * 70.0), 0.0), new Vector(0.0, 0.0), 70.0);

        List<Double> leapFrogErrors = new ArrayList<>();
        List<Double> beemanErrors = new ArrayList<>();
        List<Double> gearErrors = new ArrayList<>();
        List<Double> deltaTs = new ArrayList<>();

        for (; deltaT >= 0.001; deltaT -= 0.0005) {
            // LEAP FROG (VERLET)
            ExperimentsStatsAgregator<OscilatorMetrics> agregatorLeapFrog = new ExperimentsStatsAgregator<>();
            StepCalculator stepCalculatorLeapFrog = new LeapFrogVelvetCalculator(new OscillatorForce(), deltaT);
            OscillatorSimulator oscillatorLeapFrog = new OscillatorSimulator(deltaT, timeLimit, stepCalculatorLeapFrog, p);
            ExperimentStatsHolder<OscilatorMetrics> statsHolderLeapFrog = oscillatorLeapFrog.start();
            agregatorLeapFrog.addStatsHolder(statsHolderLeapFrog);
//            System.out.println(statsHolderLeapFrog.getDataSeries(OscilatorMetrics.SIM_X));
            Double meanSquaredErrorLeapFrog = statsHolderLeapFrog.getDataSeries(OscilatorMetrics.ERROR).stream()
                    .mapToDouble(dataPoint -> {
                        return dataPoint.getValue();
                    })
                    .sum()
                    / statsHolderLeapFrog.getDataSeries(OscilatorMetrics.ERROR).size();

            // BEEMAN
            ExperimentsStatsAgregator<OscilatorMetrics> agregatorBeeman = new ExperimentsStatsAgregator<>();
            StepCalculator stepCalculatorBeeman = new BeemanCalculator(new OscillatorForce(), deltaT, Arrays.asList(p));
            OscillatorSimulator oscillatorBeeman = new OscillatorSimulator(deltaT, timeLimit, stepCalculatorBeeman, p);
            ExperimentStatsHolder<OscilatorMetrics> statsHolderBeeman = oscillatorBeeman.start();
            agregatorBeeman.addStatsHolder(statsHolderBeeman);
//            System.out.println(statsHolderBeeman.getDataSeries(OscilatorMetrics.SIM_X));
            Double meanSquaredErrorBeeman = statsHolderBeeman.getDataSeries(OscilatorMetrics.ERROR).stream()
                    .mapToDouble(dataPoint -> {
                        return dataPoint.getValue();
                    })
                    .sum()
                    / statsHolderBeeman.getDataSeries(OscilatorMetrics.ERROR).size();

            // GEAR PREDICTOR CORRECTOR
            ExperimentsStatsAgregator<OscilatorMetrics> agregatorGear = new ExperimentsStatsAgregator<>();
            StepCalculator stepCalculatorGear = new GearCalculator(new OscillatorForce(), deltaT, new GearOscillatorUtils(), Collections.singletonList(p));
            OscillatorSimulator oscillatorGear = new OscillatorSimulator(deltaT, timeLimit, stepCalculatorGear, p);
            ExperimentStatsHolder<OscilatorMetrics> statsHolderGear = oscillatorGear.start();
            agregatorGear.addStatsHolder(statsHolderGear);
//            System.out.println(statsHolderGear.getDataSeries(OscilatorMetrics.SIM_X));
            Double meanSquaredErrorGear = statsHolderGear.getDataSeries(OscilatorMetrics.ERROR).stream()
                    .mapToDouble(dataPoint -> {
                        return dataPoint.getValue();
                    })
                    .sum()
                    / statsHolderGear.getDataSeries(OscilatorMetrics.ERROR).size();


//            System.out.println(statsHolderGear.getDataSeries(OscilatorMetrics.CALC_X));
//            System.out.println(statsHolderGear.getDataSeries(OscilatorMetrics.TIME));

            leapFrogErrors.add(meanSquaredErrorLeapFrog);
            beemanErrors.add(meanSquaredErrorBeeman);
            gearErrors.add(meanSquaredErrorGear);
            deltaTs.add(deltaT);
//            System.out.println(meanSquaredErrorLeapFrog);
//            System.out.println(meanSquaredErrorBeeman);
//            System.out.println(meanSquaredErrorGear);
        }

        System.out.println((leapFrogErrors.get(leapFrogErrors.size()-1) - leapFrogErrors.get(0))
                /(deltaTs.get(deltaTs.size()-1) - deltaTs.get(0)));

        System.out.println(leapFrogErrors);
        System.out.println(beemanErrors);
        System.out.println(gearErrors);
        System.out.println(deltaTs);

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
