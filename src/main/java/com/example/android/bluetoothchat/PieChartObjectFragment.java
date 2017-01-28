package com.example.android.bluetoothchat;

/**
 * Created by aman on 4/1/17.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.DatabaseClass;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.pie_chart_collection_object, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.text1)).setText(
                Integer.toString(args.getInt(ARG_OBJECT)));

        PieChart pieChart = (PieChart) rootView.findViewById(R.id.pie_chart1);

        Cursor c = null;
        DatabaseClass pieChartData = new DatabaseClass(getActivity());
        pieChartData.open();
        int icount = pieChartData.countRows();
        if (icount == 0) {
            Toast.makeText(getActivity(), "Database is empty so can't display the graph", Toast.LENGTH_LONG).show();
            pieChartData.close();
            return rootView;
        } else {
            c = pieChartData.getRecord();
            pieChartData.close();
        }
        //int icount = c.getInt(0);
        int a, i = 0;

        ArrayList<Entry> entries = new ArrayList();
        ArrayList<String> labels = new ArrayList<String>();

        do {
            a = Integer.parseInt(c.getString(2));
            entries.add(new Entry(a, i));
            i++;
            labels.add(c.getString(1));
        }
        while (c.moveToNext());

        PieDataSet dataset = new PieDataSet(entries, "# of Calls");

        PieData data = new PieData(labels, dataset);
        pieChart.setData(data);
        pieChart.setDescription("Bar Graph");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        //pieChart.animateY(3000);
        return rootView;
    }
}
