package helpers;

import Planets.PlanetMetrics;
import Planets.PlanetsSimulator;
import experiments.DataPoint;
import experiments.ExperimentStatsHolder;
import javafx.util.Pair;
import models.Vector;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OctaveBuilder {


    private Double au2m = 149597870000.700;
    int finalFrameCount = 1000;

    public OctaveBuilder() {
    }

    public OctaveBuilder(int finalFrameCount) {
        this.finalFrameCount = finalFrameCount;
    }

    public String getOctaveGrapher(ExperimentStatsHolder<PlanetMetrics> holder, List<Pair<List<Double>,List<Double>>> voyagerPaths){
        // Octave output
        StringBuilder sb = new StringBuilder();
        sb.append("function solar()\n");

        sb.append(getPlanetPositions("xsun",holder.getDataSeries(PlanetMetrics.SUN_X)));
        sb.append(getPlanetPositions("ysun",holder.getDataSeries(PlanetMetrics.SUN_Y)));
        sb.append(getPlanetPositions("xear",holder.getDataSeries(PlanetMetrics.EARTH_X)));
        sb.append(getPlanetPositions("year",holder.getDataSeries(PlanetMetrics.EARTH_Y)));
        sb.append(getPlanetPositions("xjup",holder.getDataSeries(PlanetMetrics.JUPITER_X)));
        sb.append(getPlanetPositions("yjup",holder.getDataSeries(PlanetMetrics.JUPITER_Y)));
        sb.append(getPlanetPositions("xsat",holder.getDataSeries(PlanetMetrics.SATURN_X)));
        sb.append(getPlanetPositions("ysat",holder.getDataSeries(PlanetMetrics.SATURN_Y)));
        sb.append(getPlanetPositions("xvoy",holder.getDataSeries(PlanetMetrics.VOYAGER_X)));
        sb.append(getPlanetPositions("yvoy",holder.getDataSeries(PlanetMetrics.VOYAGER_Y)));

        Integer index = 0;
        for (Pair<List<Double>,List<Double>> path:voyagerPaths){
            sb.append(getPlanetPositionsDoubles("xvoy"+index, path.getKey()));
            sb.append(getPlanetPositionsDoubles("yvoy"+index, path.getValue()));
            index++;
        }

        sb.append("plot(xsun, ysun, 'x', 'color', 'r', xear, year, 'b', xjup, yjup, 'm', xsat, ysat, 'k',");
        for (int i = 0; i < voyagerPaths.size(); i++) {
            sb.append("xvoy" +i+", " +"yvoy"+i+", 'c',");
        }
        sb.append("xvoy, yvoy, 'r'");
        sb.append(")\n");
        sb.append("axis([-10 10 -10 10])\n");
        sb.append("axis square\n");
        sb.append("endfunction\n");
        return sb.toString();
    }

    public String getOctaveGrapher(ExperimentStatsHolder<PlanetMetrics> holder){
        return getOctaveGrapher(holder, Collections.emptyList());
    }

    public String getPlanetPositionsDoubles(String name, List<Double> values){
        int frame = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name+"=[");
        for (Double x : values)
            if (frame++ % (values.size() / finalFrameCount) == 0) {
                stringBuilder.append(x/au2m + " ");
            }
        stringBuilder.append("];\n");
        return stringBuilder.toString();
    }

    public String getPlanetPositions(String name, List<DataPoint> values){
        return getPlanetPositionsDoubles(name,values.stream().map(DataPoint::getValue).collect(Collectors.toList()));
    }
}
