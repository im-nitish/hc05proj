package com.example.android.bluetoothchat;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.DatabaseClass;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        com.github.mikephil.charting.charts.PieChart pieChart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.pie_chart);

        int a;
        int i = 0;
        Cursor c ;

        DatabaseClass pieChartData = new DatabaseClass(this);
        pieChartData.open();
        int icount = pieChartData.countRows();
        if(icount==0)
        {
            Toast.makeText(this,"Database is empty, so can't display the Pie chart",Toast.LENGTH_LONG).show();
            pieChartData.close();
            return;
        }
        else {
            c = pieChartData.getRecord();
            pieChartData.close();
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


        PieDataSet dataset = new PieDataSet(entries,"# of Calls" );

        PieData data = new PieData(labels, dataset); // initialize Piedata
        pieChart.setData(data);
        pieChart.setDescription("Pie Chart");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.animateY(3000);
    }
}
