package asper.evaluation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import asper.evaluation.feed.Worker;
import asper.evaluation.feed.Workers;
import asper.evaluation.task.Task;
import asper.evaluation.task.Tasks;
import asper.evaluation.utilities.Files;
import asper.evaluation.utilities.statistics.Measurement;
import asper.evaluation.utilities.statistics.Measurements;
import asper.evaluation.utilities.statistics.Statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Reporter extends Activity
{

    private TextView content;
    private Button saveButton;
    private LinearLayout graphPlaceholder;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.reporter);

        //
        content = (TextView) findViewById(R.id.reporter_content);

        graphPlaceholder = (LinearLayout) findViewById(R.id.graph_content);

        saveButton = (Button) findViewById(R.id.save_measurements);

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                save();
            }
        });

        render();
    }

    private void render()
    {

        Random random = new Random();
        StringBuilder string = new StringBuilder();
        Graph graph = new Graph(getApplicationContext(), "Measurements");

        for (Worker worker : Workers.getWorkers())
        {

            String key = worker.getName();
            ArrayList<Measurement> measurements = Measurements.getMeasurements(key);

            final int samples = Settings.RUNTIME / Settings.MONITORGRANUALITY;

            // Calculates the local, total average throughput

            List<Number> combined = new ArrayList();

            for (int i = 0; i < samples; i++)
            {
                for (Measurement m : measurements)
                {
                    combined.addAll(m.getValues());
                }
            }

            Measurement combinedMeasures = Statistics.calculate(combined);

            string.append(
                    "Worker: " + worker.getName() + "\n"
            );

            string.append(
                    String.format("Average throughput: %.2f\n", combinedMeasures.getMean())
            );

            string.append(
                    String.format("Std.dev : %.2f\n\n", combinedMeasures.getStdev())
            );

            ArrayList<Measurement> summarized = new ArrayList<Measurement>();

            // For each sample monitored, calculate the average;
            for (int i = 0; i < samples; i++)
            {
                List<Number> l = new ArrayList();

                for (Measurement m : measurements)
                {
                    l.add(m.getValues().get(i));
                }

                summarized.add(Statistics.calculate(l));
            }

            ArrayList<Number> serie = new ArrayList<Number>();

            for (Measurement m : summarized)
            {
                serie.add(m.getMean());

                string.append(
                        String.format("%.2f", m.getMean())
                );

                string.append(
                        String.format(" (+- %.2f)\n", m.getStdev())
                );
            }

            graph.addSeries(
                    key,
                    Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)),
                    serie
            );

        }


        Task task = Tasks.getCurrent();

        String queries = "";

        for (String query : task.getQueries())
        {
            queries += query + "\n";
        }

        string.insert(
                0,
                "#\n"
                        + "Task: " + task.getLabel() + "\n"
                        + "Queries : \n[\n" + queries + "]\n"
                        + "On : " + Settings.WORKERS + " workers\n"
                        + "Warmup : " + Settings.WARMUP / 1000 + " s.\n"
                        + "Runtime : " + Settings.RUNTIME / 1000 + " s.\n"
                        + "#\n"
        );

        content.setText(string.toString());
        graphPlaceholder.addView(graph.getView());

        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(2500);

    }

    private void save()
    {
        StringBuilder string = new StringBuilder();

        string.append(content.getText());

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        String file = Settings.IO.MEASUREMENTS
                + "/"
                + Tasks.getCurrent().getLabel()
                + "-" + format.format(new Date());

        Files.write(
                file,
                string.toString()
        );

        Toast.makeText(
                getApplicationContext(),
                "Results saved.",
                Toast.LENGTH_SHORT
        ).show();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.finish();
    }

}
