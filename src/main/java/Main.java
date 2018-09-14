import OsciladorAmortiguado.ForceCalculators.OscillatorForce;
import OsciladorAmortiguado.Oscillator;
import OsciladorAmortiguado.StepCalculators.LeapFrogVelvet;
import OsciladorAmortiguado.StepCalculators.StepCalculator;

public class Main {

    public static void main(String[] args) {
        Oscillator oscillator = new Oscillator();
        oscillator.start();
    }

}
