package OsciladorAmortiguado;

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

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
