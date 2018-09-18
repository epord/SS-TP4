package experiments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExperimentsStatsAgregator<K extends Enum> {
    List<ExperimentStatsHolder<K>> holders;
    List<Function<StringBuilder,StringBuilder>> builder = new ArrayList<>();


    public ExperimentsStatsAgregator() {
        holders = new ArrayList<>();
    }

    public void addStatsHolder(ExperimentStatsHolder<K> experimentStatsHolder){
        holders.add(experimentStatsHolder);
    }

    public StringBuilder buildStatsOutput(List<Operation> operations){
        StringBuilder stringBuilder = new StringBuilder();
        Set<K> activeSeries = new HashSet<>();
        holders.stream().forEach(serie -> activeSeries.addAll(serie.getActiveSeries()));
        
        List<AgregatedSeries<K>> timeseriesStats = activeSeries.stream().map(serie ->
            new AgregatedSeries<>(fillStats(holders.stream()
                    .map(holder -> holder.getDataSeries(serie))
                    .filter( list -> !list.isEmpty())
                    .collect(Collectors.toList())),serie)
        ).collect(Collectors.toList());

        System.out.println("Header order");
        for (AgregatedSeries<K> timeseriesStat : timeseriesStats) {
            timeseriesStat.addHeaders(stringBuilder, operations);
        }
        stringBuilder.append("\n");
        Integer maxTimeseriesLenght = timeseriesStats.stream().mapToInt(AgregatedSeries::size).max().getAsInt();
        if(timeseriesStats.size() > 0){
            for (int i = 0; i < maxTimeseriesLenght; i++) {
                for (AgregatedSeries<K> agregatedSeries : timeseriesStats) {
                    agregatedSeries.addStats(stringBuilder, operations, i);
                }
                stringBuilder.append("\n");
            }
        }
        return stringBuilder;
    }

    public static Double getStandardDeviation(List<Double> values) {
        Double mean = values.stream().mapToDouble( x -> x).average().getAsDouble();
        return getStandardDeviation(values,mean);
    }

    public static Double getStandardDeviation(List<Double> values, Double mean) {
        Double standardDeviation = values.stream()
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .sum();

        return Math.sqrt(standardDeviation /(values.size()-1));
    }

    public List<Stats> fillStats(List<List<DataPoint>> timeseries){
        List<Stats> statsList = new ArrayList<>();
        Integer minLenght = getMinLenght(timeseries);
        for (int i = 0; i < minLenght; i++) {
            DataPoint min = new DataPoint(-1.0,Double.MAX_VALUE);
            DataPoint max = new DataPoint(-1.0,-Double.MAX_VALUE);
            Double mean = 0.0;
            Double std;
            DataPoint median = null;
            List<Double> values = new ArrayList<>();
            for (int j = 0; j < timeseries.size(); j++) {
                DataPoint analized = timeseries.get(j).get(i);
                min = min.getValue() < analized.getValue()?min:analized;
                max = max.getValue() > analized.getValue()?max:analized;
                mean += analized.getValue()/timeseries.size();
                values.add(analized.getValue());
                if(j == timeseries.size()/2){
                    median = analized;
                }
            }
            std = getStandardDeviation(values,mean);
            Stats stats = new Stats(min,max,std,mean,median);
            statsList.add(stats);
        }
        return statsList;
    }

    public Integer getMinLenght(List<List<DataPoint>> timeseries){
        Integer min = Integer.MAX_VALUE;
        for(List<DataPoint> timeserie: timeseries){
            min = min < timeserie.size()?min:timeserie.size();
        }
        return min;
    }
}
