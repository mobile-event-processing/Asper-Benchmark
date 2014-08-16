package asper.evaluation.feed;

import java.util.ArrayList;
import java.util.List;

/**
 * An access object related to
 * currently active workers in the system.
 */

public class Workers
{
    // collection of workers
    private static ArrayList<Worker> workers;

    /**
     * Constructor
     */
    static
    {
        workers = new ArrayList<Worker>();
    }

    /**
     * Appends a new worker to the
     * list of workers
     *
     * @param worker the Worker to be added
     */
    public static void add(Worker worker)
    {
        if (workers.contains(worker))
        {
            return;
        } else
        {
            workers.add(worker);
        }
    }

    /**
     * Interupts and clears the repository
     * for all current workers.
     */
    public static void reset()
    {
        for (Worker w : workers)
            w.interrupt();

        workers.clear();
    }

    public static List<Worker> getWorkers()
    {
        return workers;
    }
}
