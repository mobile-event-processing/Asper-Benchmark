package asper.evaluation.utilities;

import android.os.Environment;
import asper.evaluation.Settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Files
{
    static
    {
        try
        {
            File a = new File(Settings.IO.DIRECTORY);
            File b = new File(Settings.IO.MEASUREMENTS);
            File c = new File(Settings.IO.TASKS);

            // Confirm that the base
            // directory is present and accessible
            if (!a.exists())
                a.mkdirs();

            if (!b.exists())
                b.mkdirs();

            if (!c.exists())
                c.mkdirs();


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static File open(String filename)
    {
        try
        {
            File file = new File(filename);
            if(!file.exists())
                file.createNewFile();

            return file;

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public synchronized static void write(String filename, String content)
    {
        try
        {

            FileWriter writer = new FileWriter(open(filename));
            writer.write(content);
            writer.flush();
            writer.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static String timestamp()
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(new Date());
    }
}
