package OsciladorAmortiguado;

public class Particle {
    private Vector position;
    private Vector velocity;
    private Double mass;

    public Particle(Vector position, Vector velocity, Double mass) {
        this.velocity = velocity;
        this.position = position;
        this.mass = mass;
    }

    public void setPosition(Vector newPosition) {
        this.position = newPosition;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Double getMass() {
        return mass;
    }
}
