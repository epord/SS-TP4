package helpers;

import Planets.PlanetMetrics;
import experiments.DataPoint;
import experiments.ExperimentStatsHolder;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AnimationBuilder {


    private Double au2m = 149597870000.700;
    int finalFrameCount = 1000;
    int precision = 8;

    public AnimationBuilder() {
    }

    public AnimationBuilder(int finalFrameCount, int precision) {
        this.precision = precision;
        this.finalFrameCount = finalFrameCount;
    }

    public String getAnimationOutput(ExperimentStatsHolder<PlanetMetrics> holder, List<Pair<List<Double>,List<Double>>> voyagerPaths){
        // Octave output
        StringBuilder sb = new StringBuilder();

        List<DataPoint> sunX = holder.getDataSeries(PlanetMetrics.SUN_X);
        List<DataPoint> sunY = holder.getDataSeries(PlanetMetrics.SUN_Y);
        List<DataPoint> earthX = holder.getDataSeries(PlanetMetrics.EARTH_X);
        List<DataPoint> earthY = holder.getDataSeries(PlanetMetrics.EARTH_Y);
        List<DataPoint> jupiterX = holder.getDataSeries(PlanetMetrics.JUPITER_X);
        List<DataPoint> jupiterY = holder.getDataSeries(PlanetMetrics.JUPITER_Y);
        List<DataPoint> saturnX = holder.getDataSeries(PlanetMetrics.SATURN_X);
        List<DataPoint> saturnY = holder.getDataSeries(PlanetMetrics.SATURN_Y);
        List<DataPoint> voyagerX= holder.getDataSeries(PlanetMetrics.VOYAGER_X);
        List<DataPoint> voyagerY = holder.getDataSeries(PlanetMetrics.VOYAGER_Y);

        sb.append(finalFrameCount + "\n");

        sb.append(getPlanetPositions(sunX));
        sb.append(getPlanetPositions(sunY));
        sb.append(getPlanetPositions(earthX));
        sb.append(getPlanetPositions(earthY));
        sb.append(getPlanetPositions(jupiterX));
        sb.append(getPlanetPositions(jupiterY));
        sb.append(getPlanetPositions(saturnX));
        sb.append(getPlanetPositions(saturnY));
        sb.append(getPlanetPositions(voyagerX));
        sb.append(getPlanetPositions(voyagerY));


        return sb.toString();
    }

    public String getPlanetPositions(List<DataPoint> values){
        StringBuilder sb = new StringBuilder();
        int frame = 0;
        for (DataPoint p : values) {
            if (frame++ % (values.size() / finalFrameCount) == 0) {
                sb.append(String.format(new Locale("en", "us"),"%."+precision+"f ", p.getValue()/au2m));
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
