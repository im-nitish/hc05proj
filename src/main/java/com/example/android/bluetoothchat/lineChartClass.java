package com.example.android.bluetoothchat;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.DatabaseClass;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class lineChartClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        LineChart lineChart = (LineChart) findViewById(R.id.line_chart);

        int a;
        int i = 0;
        Cursor c;

        DatabaseClass lineChartData = new DatabaseClass(this);
        lineChartData.open();
        int icount = lineChartData.countRows();
        if(icount==0)
        {
            Toast.makeText(this,"Database is empty so can't display the line graph",Toast.LENGTH_LONG).show();
            lineChartData.close();
            return;
        }
        else {
            c = lineChartData.getRecord();
            lineChartData.close();
        }

        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList();

        do{

            a = Integer.parseInt(c.getString(2));
            entries.add(new Entry(a, i));
            i++;
            labels.add(c.getString(1));
        }
        while(c.moveToNext());



        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        LineData data = new LineData(labels, dataset);
                lineChart.setData(data);

        lineChart.setDescription("line chart");
        dataset.setDrawCubic(true);          //for curve lines
        dataset.setDrawFilled(true);         //for filling the area below
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);   //for different colors

        lineChart.animateY(3000);
    }
}
