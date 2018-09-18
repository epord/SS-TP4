package experiments;

import java.util.List;

public class AgregatedSeries<K> {
    private List<Stats> seriesData;
    private K series;

    public AgregatedSeries(List<Stats> seriesData, K series) {
        this.seriesData = seriesData;
        this.series = series;
    }

    public Integer size(){
        return seriesData.size();
    }

    public StringBuilder addHeaders(StringBuilder sb, List<Operation> operations){
        for (Operation op : operations) {
            sb.append(series + " " + op + ",");
        }
        return sb;
    }
    public StringBuilder addStats(StringBuilder sb, List<Operation> operations, Integer index){
        for (Operation op : operations) {
            if(index < 0 || index >= seriesData.size()){
                sb.append("-,");
            } else {
//                sb.append(Utils.formatDouble(seriesData.get(index).getValueByOperation(op)))
                sb.append(seriesData.get(index).getValueByOperation(op))
                        .append(",");
            }
        }
        return sb;
    }

    public K getSeriesType(){
        return series;
    }
}
