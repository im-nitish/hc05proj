package com.example.android.bluetoothchat;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.DatabaseClass;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main3Activity extends ActionBarActivity {

    String TAG = "com.example.android";

    private static BarChart barChart;
    private static BarDataSet dataset;
    private static BarData data;
    private String[] tabs = { "Day", "Week", "Month" };
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    GraphPageAdapter graphPageAdapter;
    PieChartPageAdapter pieChartPageAdapter;

    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        final ActionBar actionBar = getSupportActionBar();
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                // mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0) {
                    //Toast.makeText(getApplication(), "tab0", Toast.LENGTH_LONG).show();
                    graphPageAdapter =
                            new GraphPageAdapter(
                                    getSupportFragmentManager());
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(graphPageAdapter);
                    mViewPager.setCurrentItem(100);
                }

                if(tab.getPosition()==1) {
                   // Toast.makeText(getApplication(), "tab1", Toast.LENGTH_LONG).show();
                    mDemoCollectionPagerAdapter =
                            new DemoCollectionPagerAdapter(
                                    getSupportFragmentManager());
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(mDemoCollectionPagerAdapter);
                    mViewPager.setCurrentItem(100);
                }
                if(tab.getPosition()==2){
                    pieChartPageAdapter =
                            new PieChartPageAdapter(
                                    getSupportFragmentManager());
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(pieChartPageAdapter);
                    mViewPager.setCurrentItem(100);
                }
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for(String tab_name : tabs) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(tab_name)
                            .setTabListener(tabListener));
        }

    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        String[] weekdays = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.fragment_collection_object, container, false);

            Bundle args = getArguments();
            ((TextView) rootView.findViewById(R.id.text1)).setText(
                    Integer.toString(args.getInt(ARG_OBJECT)));

            barChart = (BarChart) rootView.findViewById(R.id.bar_graph1);

            Cursor c = null;

           /* String[] d;
            SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            String entry_data = date.format(new Date());
            */

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            DatabaseClass bgraphData = new DatabaseClass(getActivity());
            bgraphData.open();
            int icount = bgraphData.countRowsWeek();
            int x = icount/7;
            x++;
            int y = icount%7;
            if(icount==0)
            {
                Toast.makeText(getActivity(),"Database is empty so can't display the graph",Toast.LENGTH_LONG).show();
                bgraphData.close();
                return rootView;
            }
            else {
                c = bgraphData.getRecordWeek("week"+x);
               // d = bgraphData.getDayData(entry_data);
                bgraphData.close();
            }
            //int icount = c.getInt(0);
            int a, i=0;
           // int b=1;
            ArrayList<BarEntry> entries = new ArrayList();
            ArrayList<String> labels = new ArrayList<String>();

           /* do{
                a = Integer.parseInt(c.getString(1));
                entries.add(new BarEntry(a, i));
                i++;
                labels.add(c.getString(2));
            }
            while(c.moveToNext());
            */
            day = day - y;
            if(day<0)
                day = day+7;

            do{
                a = Integer.parseInt(c.getString(1));
                entries.add(new BarEntry(a, i));
                i++;
                labels.add(weekdays[day]);
                day++;
                if(day==7){
                    day=0;
                }
            }while(c.moveToNext());

           /* do{
                if(d[b-1]==null)
                    break;

                String[] parts = d[b-1].split(".");
                b++;
                a = Integer.parseInt(parts[1]);
                entries.add(new BarEntry(a, i));
                i++;
                labels.add(parts[0]);
            }while (b!= d.length);
            */

            dataset = new BarDataSet(entries,"Water Level per day");

            data = new BarData(labels, dataset);
            barChart.setData(data);
            barChart.setDescription("Bar Graph");
            dataset.setColors(ColorTemplate.COLORFUL_COLORS);

            //barChart.animateY(3000);
            return rootView;
        }


    }
}

/*

Bundle args = getArguments();
            Typeface typeface= Typeface.createFromAsset(rootView.findViewById(R.id.text1).getContext().getAssets(), "fonts/OpenSansRegular.ttf");
            TextView t = (TextView) rootView.findViewById(R.id.text1);
            t.setTypeface(typeface);

            ((TextView) rootView.findViewById(R.id.text1)).setText(
                    Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;

*/