package com.evgenyvyaz.audiospectr;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.evgenyvyaz.audiospectr.visualizer.VisualizerController;
import com.evgenyvyaz.audiospectr.visualizer.VisualizerView;

import java.io.IOException;

/**
 * Demo to show how to use VisualizerView
 */
public class MainActivity extends FragmentActivity {

    public static final String PATH = "PATH";
    MediaPlayer mPlayer;
    private VisualizerController controller;

    public static final String PREF_NAME = "colours";
    VisualizerView mVisualizerView;
    VisualizerView scaledVisz;
    VisualizerView wavesVis;
    private int PERMISSION_REQUEST = 1;
    private Button pauseBTN;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startActivityThis(getIntent().getStringExtra(PATH));

    }

    private void init() {
        controller = new VisualizerController();
        controller.addView(wavesVis);
        wavesVis.toggleWave();
        scaledVisz.setScaleY(-1f);
        controller.addView(scaledVisz);
        controller.addView(mVisualizerView);
    }

    public void startActivityThis(final String path) {

        mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
        scaledVisz = (VisualizerView) findViewById(R.id.scaledVisz);
        wavesVis = (VisualizerView) findViewById(R.id.wavesVis);
        pauseBTN = (Button) findViewById(R.id.pauseBTN);

        init();
        pauseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    mPlayer.pause();
                    mPlayer.release();
                }
                finish();
            }
        });


        final Uri uri = Uri.parse(path);

        System.out.println("path = " + path);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(MainActivity.this, uri);
            mPlayer.prepare();

            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    System.out.println("start");
                    mPlayer.start();
                    mPlayer.setLooping(true);


                    //Declare all views

                    setControllerLink();

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void onResume() {
        super.onResume();

    }

    public void setControllerLink() {

        controller.link(mPlayer);

    }

    /*public class ReceiveMessages extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MUSIQ_PLAYING)) {
                controller.link(Player.getPlayer());
            }
        }
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controller != null) {
            controller.release();
        }
    }




}
