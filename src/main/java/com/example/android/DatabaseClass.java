package com.example.android;

/**
 * Created by aman on 24/12/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.bluetoothchat.Records;
import com.example.android.bluetoothchat.Week_Records;

public class DatabaseClass {

    String tag = "com.example.android";

    private static final int DBVERSION = 2;
    private static final String DBNAME = "Record.db";
    private static final String DATABASE_TABLE = "Records";
    private static final String DATABASE_TABLE_WEEK = "WeekRecords";
    private static final String DATABASE_TABLE_MONTH = "MonthRecords";

    //day
    public static final String CVAL = "Value";
    public static final String CD = "Date";
    public static final String CT = "Time";

    //week
    public static final String WD = "WDate";
    public static final String WV = "WValue";
    public static final String WNo = "WNo";

    //month
    public static final String MWno = "MWNo";
    public static final String MV = "MValue";

    //for day
    private static final String Create_database = "Create table Records(Date text, Time text, Value text);";
    private static final String Create_database1 = "Create table if not exists Records(Date text, Time text, Value text);";

    //for week
    private static final String week_database = "Create table WeekRecords(WDate text, WValue text, WNo text);";
    private static final String week_database1 = "Create table if not exists WeekRecords(WDate text, WValue text, WNo text);";

    //for month
    private static final String month_database = "Create table MonthRecords(MWNo text, MValue text);";
    private static final String month_database1 = "Create table if not exists MonthRecords(MWNo text, MValue text);";

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Create_database);
            db.execSQL(week_database);
            db.execSQL(month_database);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " +DATABASE_TABLE_WEEK);
            db.execSQL("DROP TABLE IF EXISTS " +DATABASE_TABLE_MONTH);
            onCreate(db);
        }
    }

    public DatabaseClass(Context c){
        ourContext = c;
    }

    public DatabaseClass open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        ourDatabase.execSQL(Create_database1);
        ourDatabase.execSQL(week_database1);
        ourDatabase.execSQL(month_database1);

        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long insertdata(Records r)
    {
        ContentValues val = new ContentValues();
        val.put(CD, r.getDate());
        val.put(CT, r.getTime());
        val.put(CVAL, r.getValue());

        return ourDatabase.insert(DATABASE_TABLE, null, val);
    }

    public Cursor getRecord()
    {
        String query = "Select Date, Time, Value from "+DATABASE_TABLE;
        Cursor cursor = ourDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor;
    }

    public Cursor getRecord(String date)
    {
        String query = "Select Date, Time, Value from "+DATABASE_TABLE+" where Date = "+"\""+date+"\" ;";
        Cursor cursor = ourDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor;
    }

    public void eraseData(){

        String query = "Delete from " + DATABASE_TABLE;
        ourDatabase.execSQL(query);
        String queryweek = "Delete from " + DATABASE_TABLE_WEEK;
        ourDatabase.execSQL(queryweek);
    }

    //AG: To check Whether database is empty or not.
    public int countRows(){
        String count = "SELECT count(*) FROM "+ DATABASE_TABLE;
        Cursor mcursor = ourDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        return icount;
    }

    public int countRowsWeek(){
        String count = "SELECT count(*) FROM "+ DATABASE_TABLE_WEEK;
        Cursor mcursor = ourDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        return icount;
    }

    public String[] getDayData(String date){
        String[] data = new String[50];

        String query = "Select Time, Value from "+DATABASE_TABLE+" where Date ="+ date;
        Cursor cursor = ourDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        int x=0;
        do{
            data[x]= cursor.getString(0)+"."+cursor.getString(1);
            x++;
        }while (cursor.moveToNext());

        return data;
    }

    public long insertweekdata(Week_Records wr){

        ContentValues val = new ContentValues();
        val.put(WD, wr.getWeekDate());
        val.put(WV, wr.getWeekValue());
        val.put(WNo, wr.getWeekNo());

        String query = "Select WDate, WValue from "+DATABASE_TABLE_WEEK+" where WDate = "+"\""+wr.getWeekDate()+"\" ;";
        Cursor cursor = ourDatabase.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            String x = wr.getWeekValue();
            String y = cursor.getString(1);
            int z = Integer.parseInt(x) + Integer.parseInt(y);
            val.put(WV, Integer.toString(z));

            ourDatabase.delete(DATABASE_TABLE_WEEK,"WDate = ?",new String[]{wr.getWeekDate()});

            return ourDatabase.insert(DATABASE_TABLE_WEEK, null, val);
        }
        else
        {
            Log.i(tag,"12345");
            return ourDatabase.insert(DATABASE_TABLE_WEEK, null, val);
        }
    }

    public Cursor getRecordWeek()
    {
        String query = "Select WDate, WValue, WNo from "+DATABASE_TABLE_WEEK;
        Cursor cursor = ourDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor;
    }

    public Cursor getRecordWeek(String week_no)
    {
        String query = "Select WDate, WValue, WNo from "+DATABASE_TABLE_WEEK+" where WNo = "+"\""+week_no+"\" ;";
        Cursor cursor = ourDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor;
    }

}
