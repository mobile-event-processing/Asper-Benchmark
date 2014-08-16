package asper.evaluation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import asper.evaluation.task.Task;
import asper.evaluation.task.Tasks;

import static android.widget.AdapterView.OnItemSelectedListener;

public class Main extends Activity {
    //
    private static Context context;
    private Coordinator coordinator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = getApplicationContext();

        Listeners listeners = new Listeners();
        //
        Spinner taskSpinner = (Spinner) findViewById(R.id.taskChooser);
        ArrayAdapter<Task> adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                Tasks.getTasks()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSpinner.setAdapter(adapter);
        taskSpinner.setOnItemSelectedListener(listeners.onTaskChosen);
        taskSpinner.setSelection(0);

        Spinner workersSpinner = (Spinner) findViewById(R.id.workers);
        workersSpinner.setOnItemSelectedListener(listeners.onWorkersChosen);

        EditText rateEditText = (EditText) findViewById(R.id.rate);
        rateEditText.addTextChangedListener(listeners.onRateChange);

        CheckBox shuffleCheckBox = (CheckBox) findViewById(R.id.shuffle);
        shuffleCheckBox.setOnCheckedChangeListener(listeners.onShuffleChange);

        EditText runtimeEditText = (EditText) findViewById(R.id.runtime);
        runtimeEditText.addTextChangedListener(listeners.onRuntimeChange);

        EditText warmupEditText = (EditText) findViewById(R.id.warumup);
        warmupEditText.addTextChangedListener(listeners.onWarmupChange);

        EditText databasePopulationEditText = (EditText) findViewById(R.id.database_population);
        databasePopulationEditText.addTextChangedListener(listeners.onDatabasePopulationChange);

        EditText databaseCacheEditText = (EditText) findViewById(R.id.database_cache);
        databaseCacheEditText.addTextChangedListener(listeners.onDatabaseCacheChange);

        EditText repetitionsEditText = (EditText) findViewById(R.id.repetitions);
        repetitionsEditText.addTextChangedListener(listeners.onRepetitionChange);

        EditText restEditText = (EditText) findViewById(R.id.rest);
        restEditText.addTextChangedListener(listeners.onRestChange);

        Button initiateButton = (Button) findViewById(R.id.start);
        initiateButton.setOnClickListener(listeners.onClickStart);

    }

    class Listeners {
        View.OnClickListener onClickStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Tasks.getCurrent() == null)
                {
                    return;
                }

                if (coordinator != null) {
                    coordinator.interrupt();
                }

                coordinator = new Coordinator();
                coordinator.start();

                Intent intent = new Intent(getContext(), Progresser.class);
                startActivity(intent);
            }
        };

        OnItemSelectedListener onTaskChosen = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Tasks.setCurrent(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        };


        OnItemSelectedListener onWorkersChosen = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Settings.WORKERS = Integer.parseInt((String) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        };

        TextWatcher onRateChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    Settings.EVENTS = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        CompoundButton.OnCheckedChangeListener onShuffleChange = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.SHUFFLE = b;
            }
        };

        TextWatcher onRuntimeChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {
                    Settings.RUNTIME = Integer.parseInt(s.toString()) * 1000;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher onWarmupChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {
                    Settings.WARMUP = Integer.parseInt(s.toString()) * 1000;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        TextWatcher onDatabasePopulationChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {
                    Settings.DATABASE_POPULATION = Integer.parseInt(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        TextWatcher onDatabaseCacheChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {
                    Settings.DATABASE_CACHE = Integer.parseInt(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        TextWatcher onRepetitionChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {
                    Settings.RUNS = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        TextWatcher onRestChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {
                    Settings.REST = Integer.parseInt(s.toString()) * 1000;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public static Context getContext() {
        return context;
    }

}
