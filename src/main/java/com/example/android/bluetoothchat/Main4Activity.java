package com.example.android.bluetoothchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.DatabaseClass;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Main4Activity extends BaseActivity {

    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;

    private static final String DEVICE_NAME = "DEVICE_NAME";
    private static final String LOG = "LOG";

    Records r;
    Week_Records wr;

    private static final SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss.SSS");

    private static String MSG_NOT_CONNECTED;
    private static String MSG_CONNECTING;
    private static String MSG_CONNECTED;

    private static DeviceConnector connector;
    private static BluetoothResponseHandler mHandler;
    private String deviceName;

    TextView recieved_packet;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mCustomPagerAdapter = new CustomPagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        if (mHandler == null)
            mHandler = new BluetoothResponseHandler(this);
        else mHandler.setTarget(this);

        MSG_NOT_CONNECTED = getString(R.string.msg_not_connected);
        MSG_CONNECTING = getString(R.string.msg_connecting);
        MSG_CONNECTED = getString(R.string.msg_connected);

        recieved_packet = (TextView) findViewById(R.id.recieved_packet);

        if (isConnected() && (savedInstanceState != null)) {
            setDeviceName(savedInstanceState.getString(DEVICE_NAME));
        } else getSupportActionBar().setSubtitle(MSG_NOT_CONNECTED);
    }

    public void Imageclicked(View v){
        Intent i =  new Intent(this, Main3Activity.class);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DEVICE_NAME, deviceName);

    }
    public void connect1(View v){
        sendCommand("hello @ Banaoo\n");
    }

    @SuppressLint("DefaultLocale")
    private boolean isConnected() {
        return (connector != null) && (connector.getState() == DeviceConnector.STATE_CONNECTED);
    }
    private void stopConnection() {
        if (connector != null) {
            connector.stop();
            connector = null;
            deviceName = null;
        }
    }
    private void startDeviceListActivity() {
        stopConnection();
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    @Override
    public boolean onSearchRequested()
    {
        if (super.isAdapterReady()) startDeviceListActivity();
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.device_control_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_search:
                if (super.isAdapterReady()) {
                    if (isConnected()) stopConnection();
                    else startDeviceListActivity();
                } else {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                return true;
            case R.id.testing:
                Intent i = new Intent(this,Main2Activity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // ============================================================================


    void setRecieveed_data(String data){
        recieved_packet.setText(data);
        try
        {
            float data1 = Float.valueOf(data);
            int datasend = (int)data1;
            insertData(String.valueOf(datasend));
        }
        catch (Exception a)
        {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice device = btAdapter.getRemoteDevice(address);
                    if (super.isAdapterReady() && (connector == null)) setupConnector(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                super.pendingRequestEnableBt = false;
                if (resultCode != Activity.RESULT_OK) {
                    Utils.log("BT not enabled");
                }
                break;
        }
    }
    private void setupConnector(BluetoothDevice connectedDevice) {
        stopConnection();
        try {
            String emptyName = getString(R.string.empty_device_name);
            DeviceData data = new DeviceData(connectedDevice, emptyName);
            connector = new DeviceConnector(data, mHandler);
            connector.connect();
        } catch (IllegalArgumentException e) {
            Utils.log("setupConnector failed: " + e.getMessage());
        }
    }

    void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        getSupportActionBar().setSubtitle(deviceName);
    }
    public void sendCommand(String commandString) {

        byte[] command = commandString.getBytes();
        if (isConnected()) {
            connector.write(command);
        }
    }


    private class BluetoothResponseHandler extends Handler {
        private WeakReference<Main4Activity> mActivity;

        public BluetoothResponseHandler(Main4Activity activity) {
            mActivity = new WeakReference<Main4Activity>(activity);
        }

        public void setTarget(Main4Activity target) {
            mActivity.clear();
            mActivity = new WeakReference<Main4Activity>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            Main4Activity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case MESSAGE_STATE_CHANGE:

                        Utils.log("MESSAGE_STATE_CHANGE: " + msg.arg1);
                        final android.support.v7.app.ActionBar bar = activity.getSupportActionBar();
                        switch (msg.arg1) {
                            case DeviceConnector.STATE_CONNECTED:
                                if (bar != null) {
                                    bar.setSubtitle(MSG_CONNECTED);
                                }
                                break;
                            case DeviceConnector.STATE_CONNECTING:
                                if (bar != null) {
                                    bar.setSubtitle(MSG_CONNECTING);
                                }
                                break;
                            case DeviceConnector.STATE_NONE:
                                if (bar != null) {
                                    bar.setSubtitle(MSG_NOT_CONNECTED);
                                }
                                break;
                        }
                        activity.invalidateOptionsMenu();
                        break;
                    case MESSAGE_READ:
                        final String readMessage = (String) msg.obj;
                        activity.setRecieveed_data(readMessage);
                        break;

                    case MESSAGE_DEVICE_NAME:
                        activity.setDeviceName((String) msg.obj);
                        break;

                    case MESSAGE_WRITE:
                        // stub
                        break;

                    case MESSAGE_TOAST:
                        // stub
                        break;
                }
            }
        }
    }

    public void insertData(String receivedData) {
        r = new Records();
        wr = new Week_Records();

        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        String entry_data = date.format(cal1.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:MM:SS");
        String entry_time = time.format(new Date());

        r.setDate(entry_data);
        r.setTime(entry_time);
        r.setValue(receivedData);

        wr.setWeekDate(entry_data);
        wr.setWeekValue(receivedData);

        DatabaseClass insData = new DatabaseClass(this);
        insData.open();
        insData.insertdata(r);

        int x = insData.countRowsWeek();
        x = x/7;
        x++;
        wr.setWeekNo("week"+x);
        insData.insertweekdata(wr);
        insData.close();
    }

}
