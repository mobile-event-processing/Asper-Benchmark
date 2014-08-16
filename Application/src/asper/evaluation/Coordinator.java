package asper.evaluation;

import android.util.Log;
import asper.evaluation.asper.Asper;
import asper.evaluation.database.DAO;
import asper.evaluation.feed.*;
import asper.evaluation.task.Event;
import asper.evaluation.task.Task;
import asper.evaluation.task.Tasks;
import asper.evaluation.utilities.statistics.Measurements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main application coordinator.
 */

public class Coordinator extends Thread
{
    // Private timer
    private Timer timer = new Timer();
    // No. of repetitions performed
    private int runs = 0;

    /**
     * Initialisation
     */
    public void run()
    {
        Conditions.testIsFinished = false;
        Measurements.reset();
        initiateTestSequence();
    }

    /**
     * Single test sequence
     * procedure
     */
    public void initiateTestSequence()
    {
        try
        {
            runs++;

            sleep(Settings.REST);

            reset();

            setup();

            preload();

            Repository.add(generate());

            synchronized (Conditions.repositoryIsFilled)
            {
                Conditions.repositoryIsFilled.notifyAll();
            }

            Log.i(Settings.TAG, "Coordinator notified workers about repository availability");

            Conditions.testCanStart.await();

            Log.i(Settings.TAG, "Coordinator winding up timer for " + (Settings.RUNTIME / 1000) + " seconds");

            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    end();
                }
            }, Settings.RUNTIME + 450);

            if (Settings.TRACE)
            {
                Log.e("#", "Initiates tracing");
                Task task = Tasks.getCurrent();
                android.os.Debug.startMethodTracing(
                        "traces/trace-" + task.getLabel() + "-" + Settings.WORKERS,
                        1024 * 1000 * 1000
                );
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Generates and preloads the
     * appropriate data-items
     *
     * @return
     */
    private ArrayList<Data> generate()
    {
        Task task = Tasks.getCurrent();
        ArrayList<Data> collection = new ArrayList();

        for (Event event : task.getEvents())
        {
            if (!event.shouldPreload())
            {
                int count = Settings.EVENTS / task.getActiveEvents().size();
                collection.addAll(Generator.make(count, event));
            }
        }

        if (Settings.SHUFFLE)
        {
            Collections.shuffle(collection);
        }

        return collection;
    }

    /**
     * Single test sequence
     * end procedure
     */
    private void end()
    {
        try
        {

            for (Worker worker : Workers.getWorkers())
            {
                worker.pause();
            }

            if (runs < Settings.RUNS)
            {
                Log.i(Settings.TAG, "Coordinator: Run " + runs + " of " + Settings.RUNS);
                initiateTestSequence();
            } else
            {
                Conditions.testIsFinished = true;

                if (Settings.TRACE)
                {
                    android.os.Debug.stopMethodTracing();
                    Log.e("#", "Ends tracing");
                }

                Log.i(Settings.TAG, "Test completed");
            }

        } catch (Exception e)
        {

        }
    }

    /**
     * Ensures that
     * the appropriate events are
     * preloaded before the test starts
     */
    void preload()
    {
        Task task = Tasks.getCurrent();

        for (Event event : task.getPassiveEvents())
        {
            Preloader.preload(event);
        }

        Log.i(Settings.TAG, "Coordinator completed pre-loading procedure");
    }

    /**
     * Test sequence setup.
     * Ensures that all schemas
     * and queries are registered
     */
    void setup()
    {
        Task task = Tasks.getCurrent();

        // Register all schemas
        for (Event event : task.getEvents())
        {
            Asper.addEvent(event);
        }

        // Register all queries
        for (String query : task.getQueries())
        {
            Asper.addQuery(task.getLabel(), query);
        }

        for (int i = 0; i < Settings.WORKERS; i++)
        {
            Worker worker = new Worker("Worker-" + (i + 1));
            worker.start();
            Workers.add(worker);
        }

        DAO.populate();

        Log.i(Settings.TAG, "Coordinator completed setup procedure");
    }

    /**
     * Global reset procedure
     */
    void reset()
    {
        Asper.reset();

        Workers.reset();

        Repository.clear();

        Conditions.reset();

        Generator.reset();

        DAO.clear();

        System.gc();

        Log.i(Settings.TAG, "Coordinator completed reset procedure");
    }
}
