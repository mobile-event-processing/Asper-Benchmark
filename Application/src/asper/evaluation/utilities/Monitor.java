package asper.evaluation.utilities;

import android.util.Log;
import asper.evaluation.Settings;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Monitor {

    private volatile long i;
    private Timer timer;
    private TimerTask task;
    private final Monitor reference;
    private LinkedList<Number> measurements;

    public Monitor() {
        reference = this;
        timer = new Timer();
        measurements = new LinkedList();

        }

    /**
     * Initiates the Monitor
     * timer. Ensures saving cycles.
     */
    public void initiate()
    {
        task = new TimerTask()
        {
            @Override
            public void run() {
                reference.save();
            }
        };

        timer.scheduleAtFixedRate(
                task,
                Settings.MONITORGRANUALITY,
                Settings.MONITORGRANUALITY
        );
    }

    /**
     * Increments the local counter
     * that withholds a notion of how many
     * data-items a Worker has processed for
     * a given time-span.
     */
    public void increment()
    {
        ++i;
    }

    /**
     * Persists the current counter
     * value and resets it.
     */
    private void save()
    {
        measurements.add(new Long(i));
        i = 0;
    }

    public void cancel()
    {
        timer.cancel();
        timer = new Timer();
        i = 0;
    }

    public void reset()
    {
       cancel();
       measurements.clear();
    }

    public LinkedList<Number> getMeasurements()
    {
        return measurements;
    }
}
