package OsciladorAmortiguado;

public class Vector {
    public Double x;
    public Double y;

    public Vector(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Vector minus(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector plus(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector dot(Double c) {
        return new Vector(x * c, y * x);
    }

    public Vector dot (Vector v) {
        return new Vector(x * v.x, y * v.y);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
