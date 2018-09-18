package experiments;

public class Stats {
    private DataPoint min;
    private DataPoint max;
    private Double std;
    private Double mean;
    private DataPoint median;

    public Double getValueByOperation(Operation operation){
        switch (operation){
            case MIN:
                return min.getValue();
            case MAX:
                return max.getValue();
            case MEAN:
                return mean;
            case STD:
                return std;
            case MEDIAN:
                return median.getValue();
            case STD_LOW:
                return mean - std;
            case STD_HIGH:
                return mean + std;
        }
        throw new IllegalStateException("No operation found");
    }

    public Stats(DataPoint min, DataPoint max, Double std, Double mean, DataPoint median) {
        this.min = min;
        this.max = max;
        this.std = std;
        this.mean = mean;
        this.median = median;
    }
}
