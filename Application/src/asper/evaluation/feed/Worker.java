package asper.evaluation.feed;

import android.util.Log;
import asper.evaluation.Settings;
import asper.evaluation.asper.Asper;
import asper.evaluation.utilities.Monitor;
import asper.evaluation.utilities.statistics.Measurements;
import asper.evaluation.utilities.statistics.Statistics;
import com.espertech.esper.client.EPRuntime;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Processor / Worker
 * Feeds Asper with a set of events
 */

public class Worker extends Thread
{
    // Local storage of events
    private ArrayList<Data> storage;
    // Work flag/indicator
    private volatile boolean keepWorking = true;
    // Local refrence to the CEP - engine
    private EPRuntime engine;
    // Local monitor refrence
    private Monitor monitor;

    /**
     * Constuctor
     *
     * @param name this thread / workers name
     */
    public Worker(String name)
    {

        super.setName(name);
        storage = new ArrayList<Data>();
        engine = Asper.getRuntime();
        monitor = new Monitor();

    }

    /**
     * Main run procedure.
     * Initiated upon Thread start.
     * <p/>
     * Ensures local repository fillment,
     * warmup procedure invocation
     * and the actual test invokation.
     */
    public void run()
    {
        try
        {
            storage.clear();

            Log.i(
                    Settings.TAG,
                    "Worker awaiting repository availability"
            );

            // Waits for a notion about
            // data-repository content availablity
            synchronized (Conditions.repositoryIsFilled)
            {
                Conditions.repositoryIsFilled.wait();
            }

            storage.addAll(Repository.get());

            Log.i(
                    Settings.TAG,
                    "Worker retrieved " + storage.size() + " events"
            );

            // Initates warm-up procedure
            warmup();

            // Arrives at barrier related to
            // test invokaction.
            Conditions.testCanStart.await();

            Log.i(
                    Settings.TAG,
                    this.getName() + " initiating work procedure"
            );

            Log.i(Settings.TAG, "Rests before work");
            //sleep(5000);
            Log.i(Settings.TAG, "Begins working");

            // Initates the monitor timer
            monitor.reset();
            monitor.initiate();

            // Starts work procedure
            work();

            // Derive and persist current measurements.
            Measurements.add(getName(), Statistics.calculate(monitor.getMeasurements()));

        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e(Settings.TAG, "Worker encountered unexpected exception.");
        }
    }

    /**
     * Engine/device warmup procedure.
     * Performs work for a fixed period of time
     * without the involvement of a monitor.
     */
    private void warmup()
    {
        if (Settings.WARMUP <= 0)
            return;

        Log.i(
                Settings.TAG,
                this.getName() + " initiating warmup procedure"
        );

        new Timer().schedule(

                new TimerTask()
                {
                    public void run()
                    {
                        pause();
                    }
                },

                Settings.WARMUP
        );

        work();
         }

    /**
     * Performs the actual engine
     * feeding / processing procedure.
     * <p/>
     * Loops continuously through a set of
     * data-items until paused.
     */
    private void work()
    {
        // Current storage index
        int index = 0;
        // Continuity indicator
        keepWorking = true;
        // Storage size
        int size = storage.size();

        while (keepWorking)
        {
            // Resets the index if
            // it surpasses the storage size.
            if (index >= size)
                index = 0;

            // Retrieves the next data-item
            Data e = storage.get(index++);
            // Sends / processes the event
            engine.sendEvent(e.data.clone(), e.name);
            // Notifies the monitor
            monitor.increment();
        }
    }

    /**
     * Pauses the current execution
     */
    public void pause()
    {
        keepWorking = false;
        monitor.cancel();

        Log.i(
                Settings.TAG,
                this.getName() + " pausing"
        );
    }

    public Monitor getMonitor()
    {
        return monitor;
    }
}
