package models;

import java.util.Objects;

public class Particle {
    private String id;
    private Vector position;
    private Vector velocity;
    private Vector acceleration;
    private Double mass;

    public Particle(Integer id, Vector position, Vector velocity, Vector acceleration, Double mass) {
        this(id.toString(),position,velocity,acceleration,mass);
    }

    public Particle(String id, Vector position, Vector velocity, Vector acceleration, Double mass) {
        this.id =id;
        this.velocity = velocity;
        this.position = position;
        this.acceleration = acceleration;
        this.mass = mass;
    }


    public Particle getCopyWithPosition(Vector newPosition) {
        return new Particle(id, newPosition, velocity, acceleration, mass);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Particle getCopyWithVelocity(Vector newVelocity) {
        return new Particle(id, position, newVelocity, acceleration, mass);
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public Particle getCopyWithAcceleration(Vector newAcceleration) {
        return new Particle(id, position, velocity, newAcceleration, mass);
    }

    public Double getMass() {
        return mass;
    }

    public String getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return Objects.equals(id, particle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id='" + id + '\'' +
                ", position=" + position +
                ", velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", mass=" + mass +
                '}';
    }
}
