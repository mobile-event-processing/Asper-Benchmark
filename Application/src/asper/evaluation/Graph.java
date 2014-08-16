package asper.evaluation;

import android.content.Context;
import android.graphics.Color;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

import java.util.List;

import static com.jjoe64.graphview.GraphView.*;
import static com.jjoe64.graphview.GraphViewSeries.*;


public class Graph {

    private GraphView view;

    public Graph(Context context, String title) {

        view = new LineGraphView(
                context
                , title
        );

        view.setScrollable(true);
        view.setScalable(true);
        view.setShowLegend(true);
        view.setLegendWidth(200);
    }

    public void addSeries(String legend, int color, List<Number> values)
    {
        GraphViewData[] data = new GraphViewData[values.size()];

        for(int i = 0; i < values.size(); i++)
        {
            data[i] = new GraphViewData(i, values.get(i).intValue());
        }

        view.addSeries(new GraphViewSeries(legend, new GraphViewSeriesStyle(color, 10), data));
    }

    public GraphView getView()
    {
        return view;
    }
}
