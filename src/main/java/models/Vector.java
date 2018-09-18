package models;

public class Vector {
    private Double x;
    private Double y;

    public Vector(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Vector minus(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector plus(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector dot(Double c) {
        return new Vector(x * c, y * c);
    }

    public Vector dot (Vector v) {
        return new Vector(x * v.x, y * v.y);
    }

    public Double distance (Vector v) {
        return Math.sqrt(Math.pow(x + v.x, 2) + Math.pow(y + v.y, 2));
    }

    public Double getNorm() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector normalize() {
        Double norm = getNorm();
        return this.dot(1.0 / norm);
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y + ")";
    }
}
