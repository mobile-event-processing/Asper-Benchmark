package asper.evaluation.utilities.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Measurement implements Serializable
{
    private double mean;

    private double variance;

    private double stdev;

    private List<Number> values;

    public double getMean()
    {
        return mean;
    }

    public void setMean(double mean)
    {
        this.mean = mean;
    }

    public double getVariance()
    {
        return variance;
    }

    public void setVariance(double variance)
    {
        this.variance = variance;
    }

    public double getStdev()
    {
        return stdev;
    }

    public void setStdev(double stdev)
    {
        this.stdev = stdev;
    }

    public List<Number> getValues()
    {
        return values;
    }

    public void setValues(List<Number> values)
    {
        this.values = values;
    }
}
