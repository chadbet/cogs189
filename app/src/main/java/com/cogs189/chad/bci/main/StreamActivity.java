package com.cogs189.chad.bci.main;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cogs189.chad.bci.BaseActivity;
import com.cogs189.chad.bci.R;
import com.cogs189.chad.bci.controllers.navigation.NavigationControllerObserver;
import com.cogs189.chad.bci.controllers.navigation.Page;
import com.neurosky.AlgoSdk.NskAlgoSdk;
import com.neurosky.AlgoSdk.NskAlgoType;
import com.neurosky.connection.ConnectionStates;
import com.neurosky.connection.DataType.MindDataType;
import com.neurosky.connection.TgStreamHandler;
import com.neurosky.connection.TgStreamReader;

import java.util.Calendar;

/**
 * Created by Chad on 3/8/17.
 */

public class StreamActivity extends BaseActivity implements NavigationControllerObserver {


    public static final String TAG = StreamActivity.class.getName();

    private int badPacketCount = 0;

    private NskAlgoSdk nskAlgoSdk;
    private BluetoothAdapter mBluetoothAdapter;
    private TgStreamReader tgStreamReader;

    private boolean bInited = false;
    private boolean bRunning = false;

    private Button backToMainBtn;
    private Button connectHeadset;
    private Button startAlg;
    private Button stopAlg;
    private TextView tvAttentionValue;
    private TextView tvSignalQuality;
    private TextView totalScore;

    private int attVal;

    private int signalA = attVal;//signal from neurosky
    private int distortionVol = 50;
    private int song;
    private int distortion;
    private long score = 0;
    private int scoreTime = 0;
    private Calendar clock = Calendar.getInstance();
    private long songTime = clock.getTimeInMillis();
    private float volume;
    private final int MAX_VOLUME = 100;
    private MediaPlayer[] players = new MediaPlayer[2];

    private long startTime;
    private long elapsedTime;

    private PlayThread[] playThreads = new PlayThread[2];

    public StreamActivity() {
        page = Page.MINDWAVE_STREAM;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);


        nskAlgoSdk = new NskAlgoSdk();

        try {
            // (1) Make sure that the device supports Bluetooth and Bluetooth is on
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Toast.makeText(
                        this,
                        "Please enable your Bluetooth and re-run this program !",
                        Toast.LENGTH_LONG).show();
                finish();
//				return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "error:" + e.getMessage());
            return;
        }

        // Example of constructor public TgStreamReader(BluetoothAdapter ba, TgStreamHandler tgStreamHandler)
        tgStreamReader = new TgStreamReader(mBluetoothAdapter,callback);
        // (2) Demo of setGetDataTimeOutTime, the default time is 5s, please call it before connect() of connectAndStart()
        tgStreamReader.setGetDataTimeOutTime(6);
        tgStreamReader.startLog();

        nskAlgoSdk.NskAlgoInit(NskAlgoType.NSK_ALGO_TYPE_ATT.value, "");

        backToMainBtn = (Button) findViewById(R.id.b_go_main);
        connectHeadset = (Button) findViewById(R.id.b_connect);
        tvAttentionValue = (TextView) findViewById(R.id.sa_attention_value);
        tvSignalQuality = (TextView) findViewById(R.id.sa_signal_quality);
        startAlg = (Button) findViewById(R.id.b_start);
        stopAlg = (Button) findViewById(R.id.b_stop);


        totalScore = (TextView) findViewById(R.id.sa_signal_quality);
        totalScore.setVisibility(View.INVISIBLE);

        backToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToMainActivity();
            }
        });

        connectHeadset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // (5) demo of isBTConnected
                connectHeadset.setTextColor(getResources().getColor(R.color.red));
                if(tgStreamReader != null && tgStreamReader.isBTConnected()){

                    // Prepare for connecting
                    tgStreamReader.stop();
                    tgStreamReader.close();
                }

                // (4) Demo of  using connect() and start() to replace connectAndStart(),
                // please call start() when the state is changed to STATE_CONNECTED
                tgStreamReader.connect();
//				tgStreamReader.connectAndStart();


                players[0] = MediaPlayer.create(StreamActivity.this, R.raw.songg);
                players[1] = MediaPlayer.create(StreamActivity.this, R.raw.distortion);

                for (int i = 0; i < 2; i++){
                    playThreads[i] = new PlayThread();
                }
                for (int i = 0; i < 2; i++){
                    playThreads[i].executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, players[i]);
                }

                startTime = System.currentTimeMillis();

            }
        });

        startAlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        stopAlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectHeadset.setTextColor(getResources().getColor(R.color.black));
                long now = System.currentTimeMillis();
                elapsedTime = (now - startTime)/1000;
                playThreads[0].onCancelled();
                playThreads[1].onCancelled();
                totalScore.setText("Your score is: " + returnScore(elapsedTime));
                totalScore.setVisibility(View.VISIBLE);
            }
        });


    }

    private String returnScore(long elapsedTime) {
        long score = elapsedTime *1000;
        String val = String.valueOf(score);
        return val;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getControllerFactory().getNavigationController().addObserver(this);
    }

    @Override
    protected void onStop() {
        getControllerFactory().getNavigationController().removeObserver(this);
        super.onStop();
    }


    @Override
    public void onPageTransition(Page fromPage, Page toPage) {
        if (toPage == Page.MAIN_HOME) {
            getControllerFactory().getNavigationController().overrideCurrentPage(fromPage);
            transitionToMainActivity();
            return;
        }
    }

    private void transitionToMainActivity() {
        getControllerFactory().getNavigationController().removeObserver(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // (7) demo of TgStreamHandler
    private TgStreamHandler callback = new TgStreamHandler() {

        @Override
        public void onStatesChanged(int connectionStates) {
            // TODO Auto-generated method stub
            Log.d(TAG, "connectionStates change to: " + connectionStates);
            switch (connectionStates) {
                case ConnectionStates.STATE_CONNECTING:
                    // Do something when connecting
                    break;
                case ConnectionStates.STATE_CONNECTED:
                    // Do something when connected
                    tgStreamReader.start();
                    showToast("Connected", Toast.LENGTH_SHORT);
                    break;
                case ConnectionStates.STATE_WORKING:
                    // Do something when working

                    //(9) demo of recording raw data , stop() will call stopRecordRawData,
                    //or you can add a button to control it.
                    //You can change the save path by calling setRecordStreamFilePath(String filePath) before startRecordRawData
                    tgStreamReader.startRecordRawData();

                    break;
                case ConnectionStates.STATE_GET_DATA_TIME_OUT:
                    // Do something when getting data timeout

                    //(9) demo of recording raw data, exception handling
                    tgStreamReader.stopRecordRawData();

                    showToast("Get data time out!", Toast.LENGTH_SHORT);
                    break;
                case ConnectionStates.STATE_STOPPED:
                    // Do something when stopped
                    // We have to call tgStreamReader.stop() and tgStreamReader.close() much more than
                    // tgStreamReader.connectAndstart(), because we have to prepare for that.

                    break;
                case ConnectionStates.STATE_DISCONNECTED:
                    // Do something when disconnected
                    break;
                case ConnectionStates.STATE_ERROR:
                    // Do something when you get error message
                    break;
                case ConnectionStates.STATE_FAILED:
                    // Do something when you get failed message
                    // It always happens when open the BluetoothSocket error or timeout
                    // Maybe the device is not working normal.
                    // Maybe you have to try again
                    break;
            }
            Message msg = LinkDetectedHandler.obtainMessage();
            msg.what = MSG_UPDATE_STATE;
            msg.arg1 = connectionStates;
            LinkDetectedHandler.sendMessage(msg);
        }

        @Override
        public void onRecordFail(int flag) {
            // You can handle the record error message here
            Log.e(TAG,"onRecordFail: " +flag);

        }

        @Override
        public void onChecksumFail(byte[] payload, int length, int checksum) {
            // You can handle the bad packets here.
            badPacketCount ++;
            Message msg = LinkDetectedHandler.obtainMessage();
            msg.what = MSG_UPDATE_BAD_PACKET;
            msg.arg1 = badPacketCount;
            LinkDetectedHandler.sendMessage(msg);

        }

        @Override
        public void onDataReceived(int datatype, int data, Object obj) {
            // You can handle the received data here
            // You can feed the raw data to algo sdk here if necessary.

            Message msg = LinkDetectedHandler.obtainMessage();
            msg.what = datatype;
            msg.arg1 = data;
            msg.obj = obj;
            LinkDetectedHandler.sendMessage(msg);

            //Log.i(TAG,"onDataReceived");
        }

    };

    private boolean isPressing = false;
    private static final int MSG_UPDATE_BAD_PACKET = 1001;
    private static final int MSG_UPDATE_STATE = 1002;

    int raw;
    private Handler LinkDetectedHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // (8) demo of MindDataType
            switch (msg.what) {
                case MindDataType.CODE_RAW:
                    break;
                case MindDataType.CODE_MEDITATION:
                    break;
                case MindDataType.CODE_ATTENTION:
                    Log.d(TAG, "CODE_ATTENTION " + msg.arg1);
                    tvAttentionValue.setText("" + msg.arg1);
                    attVal = msg.arg1;
                    if(attVal > 60){
                        scoreTime = scoreTime + 1;
                        if(distortionVol > 0){
                            distortionVol = distortionVol - 10;
                            volume = (float) (1 - (Math.log(MAX_VOLUME - distortionVol) / Math.log(MAX_VOLUME)));
                            players[1].setVolume(volume, volume);
                        }
                    }
                    else{
                        if(distortionVol < 100){
                            distortionVol = distortionVol + 10;
                            volume = (float) (1 - (Math.log(MAX_VOLUME - distortionVol) / Math.log(MAX_VOLUME)));
                            players[1].setVolume(volume, volume);
                        }
                    }
                    Log.d(TAG, "Distortion level: " + distortionVol);
                    break;
                case MindDataType.CODE_EEGPOWER:
                    break;
                case MindDataType.CODE_POOR_SIGNAL://
                    int poorSignal = msg.arg1;
                    break;
                case MSG_UPDATE_BAD_PACKET:

                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };


    public void SongThing(int level, int song, int distortion){ // song is R.raw.sound_file_1





        //play two songs at once


        distortionVol = 50;

        score = ((scoreTime*1000)/songTime) * 100;
        System.out.println("Score acheived was: " + score);

    }




    class PlayThread extends AsyncTask<MediaPlayer, Void, Void>
    {
        @Override
        protected Void doInBackground(MediaPlayer... player) {

            player[0].start();
            return null;
        }

        @Override
        protected void onCancelled() {
            players[0].stop();
            players[1].stop();
            super.onCancelled();
        }
    }



    public void showToast(final String msg,final int timeStyle){
        StreamActivity.this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), msg, timeStyle).show();
            }

        });
    }

}
