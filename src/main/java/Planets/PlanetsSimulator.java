package Planets;

import CalculationMethods.StepCalculator;
import models.Particle;

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

    public void start() {
        Double currentTime = 0.0;
        List<Double> xPos0 = new ArrayList<>();
        List<Double> yPos0 = new ArrayList<>();
        List<Double> xPos1 = new ArrayList<>();
        List<Double> yPos1 = new ArrayList<>();
        List<Double> xPos2 = new ArrayList<>();
        List<Double> yPos2 = new ArrayList<>();
        List<Double> xPos3 = new ArrayList<>();
        List<Double> yPos3 = new ArrayList<>();
        List<Double> xPos4 = new ArrayList<>();
        List<Double> yPos4 = new ArrayList<>();

        while(currentTime < timeLimit) {

            xPos0.add(particles.get(0).getPosition().getX());
            yPos0.add(particles.get(0).getPosition().getY());
            xPos1.add(particles.get(1).getPosition().getX());
            yPos1.add(particles.get(1).getPosition().getY());
            xPos2.add(particles.get(2).getPosition().getX());
            yPos2.add(particles.get(2).getPosition().getY());
            xPos3.add(particles.get(3).getPosition().getX());
            yPos3.add(particles.get(3).getPosition().getY());
            xPos4.add(particles.get(4).getPosition().getX());
            yPos4.add(particles.get(4).getPosition().getY());
            particles = stepCalculator.updateParticles(particles);
            currentTime += deltaT;
        }



        // Octave output

        System.out.println("function solar()");

        StringBuilder sb = new StringBuilder();
        sb.append(getPlanetPositions("xsun",xPos0));
        sb.append(getPlanetPositions("ysun",yPos0));

        // Earth
        int frame = 0;
        System.out.print("xear=[");
        for (Double x : xPos1)
            if (frame++ % (xPos1.size() / finalFrameCount) == 0) {
                System.out.print(x/au2m + " ");
            }
        System.out.println("];");
        frame = 0;
        System.out.print("year=[");
        for (Double y: yPos1)
            if (frame++ % (xPos1.size() / finalFrameCount) == 0) {
                System.out.print(y/au2m + " ");
            }
        System.out.println("];");

        // Jupiter
        frame = 0;
        System.out.print("xjup=[");
        for (Double x : xPos2)
            if (frame++ % (xPos1.size() / finalFrameCount) == 0) {
                System.out.print(x/au2m + " ");
            }
        System.out.println("];");
        frame = 0;
        System.out.print("yjup=[");
        for (Double y: yPos2)
            if (frame++ % (xPos1.size() / finalFrameCount) == 0) {
                System.out.print(y/au2m + " ");
            }
        System.out.println("];");

        // Saturn
        frame = 0;
        System.out.print("xsat=[");
        for (Double x : xPos3)
            if (frame++ % (xPos1.size() / finalFrameCount) == 0) {
                System.out.print(x/au2m + " ");
            }
        System.out.println("];");
        frame = 0;
        System.out.print("ysat=[");
        for (Double y: yPos3)
            if (frame++ % (xPos1.size() / finalFrameCount) == 0) {
                System.out.print(y/au2m + " ");
            }
        System.out.println("];");

        // Voyager
        frame = 0;
        System.out.print("xvoy=[");
        for (Double x : xPos4)
            if (frame++ % (xPos1.size() / finalFrameCount) == 0) {
                System.out.print(x/au2m + " ");
            }
        System.out.println("];");
        frame = 0;
        System.out.print("yvoy=[");
        for (Double y: yPos4)
            if (frame++ % (xPos1.size() / finalFrameCount) == 0) {
                System.out.print(y/au2m + " ");
            }
        System.out.println("];");
        System.out.println(sb.toString());
        System.out.println("plot(xsun, ysun, 'x', 'color', 'r', xear, year, 'b', xjup, yjup, 'm', xsat, ysat, 'k', xvoy, yvoy, 'c')");
        System.out.println("axis([-10 10 -10 10])");
        System.out.println("axis square");
        System.out.println("endfunction");
    }

    String getPlanetPositions(String name, List<Double> values){
        int frame = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name+"=[");
        for (Double x : values)
            if (frame++ % (values.size() / finalFrameCount) == 0) {
                stringBuilder.append(x/au2m + " ");
            }
        stringBuilder.append("];\n");
        return stringBuilder.toString();
    }
}
