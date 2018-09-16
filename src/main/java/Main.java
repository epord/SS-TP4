import CalculationMethods.Implementations.BeemanCalculator;
import CalculationMethods.Implementations.GearCalculator;
import CalculationMethods.Implementations.LeapFrogVelvetCalculator;
import CalculationMethods.StepCalculator;
import Models.Particle;
import Models.Vector;
import Oscillator.GearOscillatorUtils;
import Oscillator.OscillatorForce;
import Oscillator.OscillatorSimulator;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Double deltaT = 0.01;
        Double timeLimit = 1.0;

        int id = 0;
        Particle p = new Particle(id++, new Vector(1.0, 0.0), new Vector(0.0, 0.0), new Vector(0.0, 0.0), 70.0);

//        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new OscillatorForce(), deltaT);
//        StepCalculator stepCalculator = new BeemanCalculator(new OscillatorForce(), deltaT,  Arrays.asList(p));
        StepCalculator stepCalculator = new GearCalculator(new OscillatorForce(), deltaT, new GearOscillatorUtils(), Arrays.asList(p));

        OscillatorSimulator oscillator = new OscillatorSimulator(deltaT, timeLimit, stepCalculator, p);
        oscillator.start();
    }
}
