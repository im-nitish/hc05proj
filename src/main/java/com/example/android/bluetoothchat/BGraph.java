package com.example.android.bluetoothchat;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.DatabaseClass;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import static android.R.attr.entries;

public class BGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgraph);

        BarChart barChart = (BarChart) findViewById(R.id.bar_graph);
      //  HorizontalBarChart hbarChart = (HorizontalBarChart) findViewById(R.id.hbar_graph);  for horizontal graph

        Cursor c ;
        DatabaseClass bgraphData = new DatabaseClass(this);
        bgraphData.open();
        int icount = bgraphData.countRows();
        if(icount==0)
        {
            Toast.makeText(this,"Database is empty so can't display the graph",Toast.LENGTH_LONG).show();
            bgraphData.close();
            return;
        }
        else {
            c = bgraphData.getRecord();
            bgraphData.close();
        }

        //int icount = c.getInt(0);

        int a, i=0;

        ArrayList <BarEntry> entries = new ArrayList();
        ArrayList<String> labels = new ArrayList<String>();

        do{
            a = Integer.parseInt(c.getString(2));
            entries.add(new BarEntry(a, i));
            i++;
            labels.add(c.getString(1));
        }
        while(c.moveToNext());

        BarDataSet dataset = new BarDataSet(entries,"# of Calls" );

        BarData data = new BarData(labels, dataset);
                barChart.setData(data);
        barChart.setDescription("Bar Graph");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(3000); //animator
    }
}
