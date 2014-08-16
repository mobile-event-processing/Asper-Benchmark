package asper.evaluation.task;

import android.content.res.AssetManager;
import android.util.Log;
import asper.evaluation.Main;
import asper.evaluation.Settings;
import asper.evaluation.utilities.Json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;

public class Tasks
{
    //
    static ArrayList<Task> tasks;

    static Task current;

    static
    {
        tasks = new ArrayList();
        loadTasks();
    }

    static void loadTasks()
    {
        try
        {
            File dir = new File(Settings.IO.TASKS);

            for (File f : dir.listFiles())
            {
                    FileInputStream stream = new FileInputStream(f);

                    byte[] content = new byte[stream.available()];
                    stream.read(content);
                    stream.close();

                    Task task = Json.fromJson(new String(content), Task.class);

                    Variable sequence = new Variable("SEQUENCE", 0);

                    for (Event event : task.getEvents())
                    {
                        event.getVariables().add(sequence);
                    }

                    tasks.add(task);
            }

            current = tasks.get(0);

            Log.i(Settings.TAG, "Located and loaded " + tasks.size() + " tasks");

        } catch (Exception e)
        {
            Log.e(Settings.TAG, "Error while loading tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setCurrent(int index)
    {
        current = tasks.get(index);
    }

    public static void setCurrent(Task task)
    {
        current = task;
    }

    public static ArrayList<Task> getTasks()
    {
        return tasks;
    }

    public static Task getCurrent()
    {
        return current;
    }
}
