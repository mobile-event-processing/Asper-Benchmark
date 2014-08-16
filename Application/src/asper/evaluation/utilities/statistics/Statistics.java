package asper.evaluation.utilities.statistics;

import java.util.ArrayList;
import java.util.List;

public class Statistics
{

    /**
     * Calculates the mean, variance and standard deviation
     * for a collection of numbers.
     *
     * @param numbers the numbers to be calculated
     * @return
     */
    public static Measurement calculate(List<Number> numbers)
    {
        Measurement measurements = new Measurement();

        // Calculates the average
        measurements.setMean(
                sum(numbers) / numbers.size()
        );

        // Calculates and summarizes the variance
        ArrayList<Number> variances = new ArrayList<Number>();


        for (Number n : numbers)
        {
            double v = (n.doubleValue() - measurements.getMean());
            variances.add(v * v);
        }

        measurements.setVariance(
                sum(variances) / numbers.size()
        );

        // Calculates the standard deviation
        measurements.setStdev(
                Math.sqrt(measurements.getVariance())
        );

        // Retains the original numbers
        measurements.setValues(numbers);

        return measurements;
    }

    public static Double sum(List<Number> numbers)
    {
        double sum = 0;

        for (Number value : numbers)
        {
            sum += value.doubleValue();
        }

        return sum;
    }

}
