package com.example.i1413.containerstorage;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by i1413 on 2017/10/31.
 */

public class SettingsActivity extends AppCompatActivity implements Runnable, View.OnClickListener{

    private static final String TAG = "BluetoothSample";

    /* Bluetooth Adapter */
    private BluetoothAdapter mAdapter;

    /* Bluetoothデバイス */
    private BluetoothDevice mDevice;

    /* Bluetooth UUID */
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /* デバイス名 */
    private final String DEVICE_NAME = "itead1";

    /* Soket */
    private BluetoothSocket mSocket;

    /* Thread */
    private Thread mThread;

    /* Threadの状態を表す */
    private boolean isRunning;

    /** 接続ボタン. */
    private Button connectButton;

    /** 書込みボタン. */
    private Button writeButton;

    /** ステータス. */
    private TextView mStatusTextView;

    /** Bluetoothから受信した値. */
    private TextView mInputTextView;

    /** Action(ステータス表示). */
    private static final int VIEW_STATUS = 0;

    /** Action(取得文字列). */
    private static final int VIEW_INPUT = 1;

    /** Connect確認用フラグ */
    private boolean connectFlg = false;

    /** BluetoothのOutputStream. */
    OutputStream mmOutputStream = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    intent = new Intent(SettingsActivity.this,HomeActivity.class);
                    startActivity(intent);
                    Log.d("★","home");
                    return true;

                case R.id.navigation_record:
                    //mTextMessage.setText(R.string.title_record);
                    intent = new Intent(SettingsActivity.this,RecordActivity.class);
                    startActivity(intent);
                    Log.d("★","record");
                    return true;

                case R.id.navigation_settings:
                    Log.d("★","settings");
                    return true;
            }
            return false;
        }

    };

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mInputTextView = (TextView)findViewById(R.id.inputValue);
        mStatusTextView = (TextView)findViewById(R.id.statusValue);

        connectButton = (Button)findViewById(R.id.connectButton);
        writeButton = (Button)findViewById(R.id.writeButton);

        connectButton.setOnClickListener(this);
        writeButton.setOnClickListener(this);

        // Bluetoothのデバイス名を取得
        // デバイス名は、RNBT-XXXXになるため、
        // DVICE_NAMEでデバイス名を定義
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mStatusTextView.setText("SearchDevice");

        try {
            Set<BluetoothDevice> devices = mAdapter.getBondedDevices();

            for (BluetoothDevice device : devices) {

                if (device.getName().equals(DEVICE_NAME)) {
                    mStatusTextView.setText("find: " + device.getName());
                    mDevice = device;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();

        isRunning = false;
        try{
            mSocket.close();
        }
        catch(Exception e){}
    }

    @Override

    public void run(){
        InputStream mmInStream = null;

        Message valueMsg = new Message();
        valueMsg.what = VIEW_STATUS;
        valueMsg.obj = "connecting...";
        mHandler.sendMessage(valueMsg);

        try{

            // 取得したデバイス名を使ってBluetoothでSocket接続
            mSocket = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
            mSocket.connect();
            mmInStream = mSocket.getInputStream();
            mmOutputStream = mSocket.getOutputStream();

            // InputStreamのバッファを格納
            byte[] buffer = new byte[1024];

            // 取得したバッファのサイズを格納
            int bytes;
            valueMsg = new Message();
            valueMsg.what = VIEW_STATUS;
            valueMsg.obj = "connected.";
            mHandler.sendMessage(valueMsg);

            connectFlg = true;

            int sensor[] = new int[3];
            int sehead = 0;

            int fc = -1;
            int value[] = new int[16];
            for(int n=0;n<value.length;n++){
                value[n] = -1;
            }
            int vhead = 0;

            while(isRunning){

                // InputStreamの読み込み
                bytes = mmInStream.read(buffer);

                // String型に変換
                String readMsg = new String(buffer, 0, bytes);

                // null以外なら表示
                if(readMsg.trim() != null && !readMsg.trim().equals("")){
                    Log.i(TAG,"sens:"+readMsg+","+readMsg.toString()+","+readMsg.trim());

                    Log.i(TAG,"value:"+readMsg.trim());
                    String sts[] = readMsg.trim().split("\n",0);
                    String st[] = new String[sts.length];
                    for(int n=0;n<sts.length;n++){

                        if(sts[n].length()==1){
                            st[n] = sts[n];
                        }

                        if(sts[n].length() == 2) {
                            if (!(sts[n].charAt(1) - '0' >= 0 && sts[n].charAt(1) - '0' <= 9)){
                                //Log.i(TAG,"value:ii:"+sts[n].charAt(0)+","+sts[n].charAt(1));
                                Log.i(TAG, "value:001");
                                st[n] = String.valueOf(sts[n].charAt(0));
                            } else{
                                st[n] = sts[n];
                            }
                        }

                        if (sts[n].length() == 3) {
                            if (!(sts[n].charAt(2) - '0' >= 0 && sts[n].charAt(2) - '0' <= 9)) {
                                Log.i(TAG, "value:002");
                                st[n] = String.valueOf((sts[n].charAt(0) - '0') * 10 + (sts[n].charAt(1) - '0'));
                                break;
                            } else {
                                st[n] = sts[n];
                            }
                        }

                        if (sts[n].length() == 4){
                            if(!(sts[n].charAt(3) - '0' >= 0 && sts[n].charAt(3) - '0' <= 9)) {
                                Log.i(TAG,"value:003");
                                st[n] = String.valueOf((sts[n].charAt(0) - '0')*100 + (sts[n].charAt(1) - '0')*10 + (sts[n].charAt(2) - '0'));
                            } else {
                            st[n] = sts[n];
                            }
                        }

                        Log.i(TAG,"value:RE:"+st[n]);
                    }

                    for(int n=0;n<st.length;n++){
                        Log.i(TAG,"value:i:"+st[n]+","+st[n].length());
                        if(st[n].length() == 1){
                           if(fc == -1){
                               fc = Integer.parseInt(st[n]);
                           }
                           else {
                               fc = fc * 10 + Integer.parseInt(st[n]);
                               value[vhead++] = fc;
                               fc = -1;
                           }
                        }
                        else{
                            value[vhead++] = Integer.parseInt(st[n]);
                        }
                        Log.i(TAG,"value:"+n+":"+st[n]);
                    }
                    valueMsg = new Message();
                    valueMsg.what = VIEW_INPUT;
                    valueMsg.obj = readMsg;
                    mHandler.sendMessage(valueMsg);

                }
                else{
                    // Log.i(TAG,"value=nodata");
                }

                int k=0;
                while(value[k]!=-1){
                    Log.i(TAG,"value::"+value[k]);
                    k++;
                }

            }
            Log.i(TAG,"sensor:"+sensor[0]+","+sensor[1]+","+sensor[2]);
        }catch(Exception e){

            valueMsg = new Message();
            valueMsg.what = VIEW_STATUS;
            valueMsg.obj = "Error1:" + e;
            mHandler.sendMessage(valueMsg);

            try{
                mSocket.close();
            }catch(Exception ee){}
            isRunning = false;
            connectFlg = false;
        }


    }

    @Override

    public void onClick(View v) {
        if (v.equals(connectButton)) {
            // 接続されていない場合のみ
            if (!connectFlg) {
                mStatusTextView.setText("try connect");

                mThread = new Thread(this);
                // Threadを起動し、Bluetooth接続
                isRunning = true;
                mThread.start();
            }
        } else if (v.equals(writeButton)) {
            // 接続中のみ書込みを行う
            if (connectFlg) {
                try {
                    mmOutputStream.write("2".getBytes());
                    mStatusTextView.setText("Write:");
                } catch (IOException e) {
                    Message valueMsg = new Message();
                    valueMsg.what = VIEW_STATUS;
                    valueMsg.obj = "Error3:" + e;
                    mHandler.sendMessage(valueMsg);
                }
            } else {
                mStatusTextView.setText("Please push the connect button");
            }
        }
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int action = msg.what;
            String msgStr = (String)msg.obj;
            if(action == VIEW_INPUT){
                mInputTextView.setText(msgStr);
            }
            else if(action == VIEW_STATUS){
                mStatusTextView.setText(msgStr);
            }
        }
    };
}
