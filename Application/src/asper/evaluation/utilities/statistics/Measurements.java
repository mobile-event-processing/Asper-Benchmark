package asper.evaluation.utilities.statistics;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Measurements
{
    private static ConcurrentHashMap<String, ArrayList<Measurement>> collection = new ConcurrentHashMap();

    public static void add(String key, Measurement value)
    {
        if (!collection.containsKey(key))
        {
            collection.put(key, new ArrayList<Measurement>());
        }

        collection.get(key).add(value);
    }

    public static ArrayList<Measurement> getMeasurements(String key)
    {
        return collection.get(key);
    }

    public static ConcurrentHashMap<String, ArrayList<Measurement>> getCollection()
    {
        return collection;
    }

    public static void reset()
    {
        collection.clear();
    }
}
