package experiments;

import java.util.*;

public class ExperimentStatsHolder<K extends Enum> {
    private Map<K,List<DataPoint>> dataSeries;

    public ExperimentStatsHolder() {
        this.dataSeries = new HashMap<>();
    }

    public void addDataPoint(K series, Double time, Double value){
        if(dataSeries.get(series) == null){
            dataSeries.put(series,new ArrayList<>());
        }
        dataSeries.get(series).add(new DataPoint(time,value));
    }


    public Set<K> getActiveSeries(){
        return dataSeries.keySet();
    }

    public List<DataPoint> getDataSeries(K serie) {
        if(!dataSeries.containsKey(serie)){
            return Collections.emptyList();
        }
        return dataSeries.get(serie);
    }
}
