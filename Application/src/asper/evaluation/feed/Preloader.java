package asper.evaluation.feed;

import android.util.Log;
import asper.evaluation.Settings;
import asper.evaluation.asper.Asper;
import asper.evaluation.task.Event;
import com.espertech.esper.client.EPRuntime;
import java.util.ArrayList;

/**
 * An Event pre-loader.
 * Reponsible for preloading a named-window
 * with a collection of events.
 */

public class Preloader
{
    /**
     * Preloads an installed query
     * with a specific event.
     *
     * Generates the events and
     * feeds the engine accordingly.
     *
     * @param event
     */
    public static void preload(Event event)
    {
        EPRuntime runtime = Asper.getRuntime();

        ArrayList<Data> collection = Generator.make(
                event.getPreload(),
                event
        );

        for (Data data : collection)
        {
            runtime.sendEvent(data.data, data.name);
        }

        Log.i(
                Settings.TAG,
                "Preloader completed preloading " + collection.size() + " instances of event " + event.getName()
        );
    }

}
