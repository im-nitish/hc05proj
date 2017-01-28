package com.example.android.bluetoothchat;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.DatabaseClass;

public class disData extends AppCompatActivity {

    DatabaseClass data = new DatabaseClass(this);
    ListView lv;
    String s = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_dis_data);
        setContentView(R.layout.display_data);
        lv = (ListView) findViewById(R.id.lv);
        data.open();
        populateListView();
    }

    public void populateListView(){
        Cursor c = data.getRecordWeek();
        TextView t = (TextView)findViewById(R.id.textView5);

        /*  String[] Field = new String[] {data.CD, data.CT, data.CVAL};
        int[] ids = new int[] {R.id.date, R.id.time, R.id.val};

       // Toast.makeText(this, data.CVAL +" "+ data.CDnT, Toast.LENGTH_LONG).show();
        SimpleCursorAdapter simpleCursorAdapter;
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.listform, c, Field, ids, 0);
        lv.setAdapter(simpleCursorAdapter);
        */

        do{
            s += c.getString(0)+" "+c.getString(1)+" "+c.getString(2)+"\n";

        }while (c.moveToNext());

        t.setText(s);
    }

    public void deleteData1(View v){
        data.eraseData();
    }

}
