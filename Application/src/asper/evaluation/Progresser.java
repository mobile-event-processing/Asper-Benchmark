package asper.evaluation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import asper.evaluation.feed.Conditions;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Progresser extends Activity {

    private ProgressBar progressBar;
    private Timer timer = new Timer();
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.awaiter);

        // Sets a wake-lock on this application
        // Prevents it from stopping processing if the application is shut down.
        PowerManager mgr = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wakelock" + new Random().nextDouble());
        wakeLock.acquire();

        progress();
    }

    private void progress() {

        TimerTask task = new TimerTask()
        {

            @Override
            public void run() {

                if(Conditions.testIsFinished == true)
                {
                    timer.cancel();
                    Intent intent = new Intent(getApplicationContext(), Reporter.class);
                    startActivity(intent);
                    wakeLock.release();
                    finish();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(getApplicationContext(), "Test sequence still under progress", Toast.LENGTH_SHORT).show();
    }

}
