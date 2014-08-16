package asper.evaluation.feed;

import asper.evaluation.Settings;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * General, public Data/Event repository.
 * Retains generated event instances
 */

public class Repository
{
    // Storage refrence
    private static LinkedBlockingDeque<Data> storage;

    /**
     * Constructor
     */
    static
    {
        storage = new LinkedBlockingDeque<Data>();
    }

    /**
     * Returns a calcutated amount of events based on
     * the current amount and workers and events. Ensures
     * to a large degree - fareness.
     *
     * @return Data-items
     */
    public static synchronized ArrayList get()
    {
        return get((Settings.EVENTS / Settings.WORKERS));
    }

    /**
     * Retrives a specified amount of items from
     * the repository.
     * <p/>
     * If amount is larger than the current size -
     * then only the remaining set of data-items
     * will be returned
     *
     * @param amount the amount of wish to retrieves
     * @return
     */
    private static synchronized ArrayList get(int amount)
    {
        ArrayList<Data> list = new ArrayList(amount);

        if (storage.size() < amount)
        {
            storage.drainTo(list);
        }
        else
        {
            storage.drainTo(list, amount);
        }

        return list;
    }

    /**
     * Appends a set of data to the repository
     *
     * @param data the set of data to be added
     */
    public static synchronized void add(ArrayList data)
    {
        storage.addAll(data);
    }

    /**
     * Clears the repository
     */
    public static synchronized void clear()
    {
        storage.clear();
    }
}
