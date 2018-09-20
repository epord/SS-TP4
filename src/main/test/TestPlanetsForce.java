import Planets.PlanetsForce;
import models.Particle;
import models.Vector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TestPlanetsForce {
    @Test
    public void testPlanets(){
        PlanetsForce pf = new PlanetsForce();

        Particle x = new Particle(0,new Vector(0.0,0.0),new Vector(0.0,0.0),new Vector(0.0,0.0),1.0);
        Particle y = new Particle(1,new Vector(2.0,0.0),new Vector(0.0,0.0),new Vector(0.0,0.0),1.0);

        List<Particle> list = Arrays.asList(x,y);

        System.out.println(pf.calculateForce(x,list));
        System.out.println(pf.calculateForce(y,list));
    }

    @Test
    public void testVector(){
        Vector x = new Vector(0.0,1.0);
        Vector y = new Vector(1.0,0.0);

        System.out.println(x.minus(y));
        System.out.println(x.minus(y).getNorm());
    }
}
