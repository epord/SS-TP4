package experiments;

public class DataPoint {
    private Double time;
    private Double value;

    public Double getTime() {
        return time;
    }

    public Double getValue() {
        return value;
    }

    public DataPoint(Double time, Double value) {
        this.time = time;
        this.value = value;
    }
}
