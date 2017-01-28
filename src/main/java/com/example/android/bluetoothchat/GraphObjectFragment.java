package com.example.android.bluetoothchat;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.DatabaseClass;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aman on 1/1/17.
 */

public class GraphObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    String[] time_day = new String[]{"0-3","4-7","8-11","12-15","16-19","20-23"};
    int[] time_value = new int[]{0,0,0,0,0,0};
    String tag = "com.example.android";
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.graph_collection_object, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.text1)).setText(
                Integer.toString(args.getInt(ARG_OBJECT)));

        LineChart lineChart = (LineChart) rootView.findViewById(R.id.line_chart1);
        //  HorizontalBarChart hbarChart = (HorizontalBarChart) findViewById(R.id.hbar_graph);  for horizontal graph

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        String entry_data = date.format(new Date());

        Cursor c = null;
        DatabaseClass linechartData = new DatabaseClass(getActivity());
        linechartData.open();
        int icount = linechartData.countRows();
        if (icount == 0) {
            Toast.makeText(getActivity(), "Database is empty so can't display the graph", Toast.LENGTH_LONG).show();
            linechartData.close();
            return rootView;
        } else {
            c = linechartData.getRecord(entry_data);
            linechartData.close();
        }

        int a,counter=0, i=0;

        ArrayList<Entry> entries = new ArrayList();
        ArrayList<String> labels = new ArrayList<String>();

        do{

            String[] parts = c.getString(1).split(":");
            int hour = Integer.parseInt(parts[0]);
            Log.i(tag,parts[0]+" "+hour);

            if(hour>=0 && hour<=3){
                time_value[0] = time_value[0] + Integer.parseInt(c.getString(2));
            }
            else if(hour>=4 && hour<=7){
                time_value[1] = time_value[1] + Integer.parseInt(c.getString(2));
            }
            else if(hour>=8 && hour<=11){
                time_value[2] = time_value[2] + Integer.parseInt(c.getString(2));
            }
            else if(hour>=12 && hour<=15){
                time_value[3] = time_value[3] + Integer.parseInt(c.getString(2));
            }
            else if(hour>=16 && hour<=19){
                time_value[4] = time_value[4] + Integer.parseInt(c.getString(2));
            }
            else{
                time_value[5] = time_value[5] + Integer.parseInt(c.getString(2));
            }

        }while(c.moveToNext());

        do{
            entries.add(new BarEntry(time_value[counter],counter));
            labels.add(time_day[counter]);
            counter++;
        }while(counter<6);

       /* do {
            a = Integer.parseInt(c.getString(2));
            entries.add(new BarEntry(a, i));
            i++;
        }
        while (c.moveToNext());
        */
        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        LineData data = new LineData(labels, dataset);
        lineChart.setData(data);
        lineChart.setDescription("Bar Graph");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        //lineChart.animateY(3000);
        return rootView;
    }

}