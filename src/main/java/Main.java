import CalculationMethods.Implementations.BeemanCalculator;
import CalculationMethods.Implementations.GearCalculator;
import CalculationMethods.Implementations.LeapFrogVelvetCalculator;
import CalculationMethods.StepCalculator;
import Models.Particle;
import Models.Vector;
import Oscillator.GearOscillatorUtils;
import Oscillator.OscillatorForce;
import Oscillator.OscillatorSimulator;
import Planets.PlanetsForce;
import Planets.PlanetsSimulator;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        runOscillatorSimulation();
        runPlanetsSimulation();
    }

    private static void runOscillatorSimulation() {
        Double deltaT = 0.03;
        Double timeLimit = 3.0;

        int id = 0;
        Particle p = new Particle(id++, new Vector(1.0, 0.0), new Vector(0.0, 0.0), new Vector(0.0, 0.0), 70.0);

//        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new OscillatorForce(), deltaT);
//        StepCalculator stepCalculator = new BeemanCalculator(new OscillatorForce(), deltaT,  Arrays.asList(p));
        StepCalculator stepCalculator = new GearCalculator(new OscillatorForce(), deltaT, new GearOscillatorUtils(), Arrays.asList(p));

        OscillatorSimulator oscillator = new OscillatorSimulator(deltaT, timeLimit, stepCalculator, p);
        oscillator.start();
    }


    private static void runPlanetsSimulation() {
        Double ua2m = 149597870000.700;
        Double kph2mps = 1000.0 / 60.0 / 60.0;
        Double day2s = 86400.0;
        Double uapd2mps = ua2m / day2s; // ua/day to m/s

        Double deltaT = 1 * day2s;
        Double timeLimit =70.0 * (365.0 * day2s);

        int id = 0;
        Particle sun = new Particle(id++,
                new Vector(0.0, 0.0),
                new Vector(0.0, 0.0),
                new Vector(0.0, 0.0),
                1.989E30);
        Particle earth = new Particle(id++,
                new Vector(9.622458737735928E-01 * ua2m, -3.008758740699761E-01 * ua2m),
                new Vector( 4.860249247919289E-03 * uapd2mps, 1.635384540738565E-02 * uapd2mps),
                new Vector(0.0, 0.0),
                5.972E24);
        Particle jupiter = new Particle(id++,
                new Vector(7.075029308889577E-01 * ua2m, 5.047888436652260E+00 * ua2m),
                new Vector(-7.569079811247934E-03 * uapd2mps, 1.400382906341323E-03 * uapd2mps),
                new Vector(0.0, 0.0),
                1898.13E24);
        Particle saturn = new Particle(id++,
                new Vector(-7.189894984015172E+00 * ua2m, 5.711846800529872E+00 * ua2m),
                new Vector(-3.778021070773957E-03 * uapd2mps, -4.383919949255058E-03 * uapd2mps),
                new Vector(0.0, 0.0),
                5.6834E26);
        List<Particle> particles = Arrays.asList(sun, earth, jupiter, saturn);

        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new PlanetsForce(), deltaT);
//        StepCalculator stepCalculator = new BeemanCalculator(new PlanetsForce(), deltaT, particles);
        PlanetsSimulator planetsSimulator = new PlanetsSimulator(deltaT, timeLimit, stepCalculator, particles);
        planetsSimulator.start();
    }
}
