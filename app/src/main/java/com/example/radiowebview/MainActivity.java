package com.example.radiowebview;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Objects;

//public class MainActivity extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnPreparedListener{
public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private WebView webViewRoi;

    int permission;
    private ProgressBar playSeekBar;


    private Button buttonStopPlay;

    private MediaPlayer player;

    String stream="http://188.165.53.101:8056/stream";

    MediaPlayer mediaPlayer;

    boolean prepared=false;

    boolean started=false;
    private ImageView buttonPlay;

    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

//        initializeUIElements();
//
//        initializeMediaPlayer();

        buttonPlay=findViewById(R.id.buttonPlay);
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
        new PlayerTask().execute(stream);

        buttonPlay.setEnabled(false);
        buttonPlay.setBackgroundResource(R.drawable.loading);
        buttonPlay.setOnClickListener(this);


        CurvedBottomNavigationView curvedBottomNavigationView=(CurvedBottomNavigationView) findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(this);


        //To increase and decrease volume
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);





        webView=findViewById(R.id.webView);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("https://app.filseka.net/");
        webView.setWebViewClient(new WebViewClient());

    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "00201094338881"));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onClick(View v) {
        if(started){

            started=false;
            mediaPlayer.pause();
            buttonPlay.setBackgroundResource(R.drawable.play);

        }else {

            started=true;
            mediaPlayer.start();
            buttonPlay.setBackgroundResource(R.drawable.pause);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_increase:
                Log.e("volume","plus");
                //To increase media player volume
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

                break;

            case R.id.action_decrease:
                Log.e("volume","minus");
                //To decrease media player volume
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

                break;


        }
        return true;
    }

//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        /** Called when MediaPlayer is ready */
//            mediaPlayer.start();
//            Log.e("onPrepared","onPrepared");
//            prepared=true;
//
//
//    }

    private  class PlayerTask extends AsyncTask <String,Void,Boolean>{
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared=true;
                Log.e("doInBackground","doInBackground");

            } catch (IOException e) {
                e.printStackTrace();
            }


            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            buttonPlay.setEnabled(true);
            mediaPlayer.start();
            buttonPlay.setBackgroundResource(R.drawable.pause);
            Log.e("onPostExecute","onPostExecute");

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            mediaPlayer.pause();
            Log.e("onPause","onPause");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(started){
            mediaPlayer.start();
            Log.e("onResume","onResume");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prepared){
            mediaPlayer.release();
            Log.e("onDestroy","onDestroy");

        }
    }

    //    private void initializeUIElements() {
//
//        playSeekBar = (ProgressBar) findViewById(R.id.progressBar1);
//        playSeekBar.setMax(100);
//        playSeekBar.setVisibility(View.INVISIBLE);
//
//        buttonPlay = (Button) findViewById(R.id.buttonPlay);
//        buttonPlay.setOnClickListener(this);
//
//        buttonStopPlay = (Button) findViewById(R.id.buttonStopPlay);
//        buttonStopPlay.setEnabled(false);
//        buttonStopPlay.setOnClickListener(this);
//
//    }
//
//
//    public void onClick(View v) {
//        if (v == buttonPlay) {
//            startPlaying();
//        } else if (v == buttonStopPlay) {
//            stopPlaying();
//        }
//    }
//
//    private void startPlaying() {
//        buttonStopPlay.setEnabled(true);
//        buttonPlay.setEnabled(false);
//
//        playSeekBar.setVisibility(View.VISIBLE);
//
//        player.prepareAsync();
//
//        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//            public void onPrepared(MediaPlayer mp) {
//                player.start();
//            }
//        });
//
//    }
//
//    private void stopPlaying() {
//        if (player.isPlaying()) {
//            player.stop();
//            player.release();
//            initializeMediaPlayer();
//        }
//
//        buttonPlay.setEnabled(true);
//        buttonStopPlay.setEnabled(false);
//        playSeekBar.setVisibility(View.INVISIBLE);
//    }
//
//    private void initializeMediaPlayer() {
//        player = new MediaPlayer();
//        try {
//            player.setDataSource("http://radio.hvips.com:8056/stream");
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//
//            public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                playSeekBar.setSecondaryProgress(percent);
//                Log.i("Buffering", "" + percent);
//            }
//        });
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (player.isPlaying()) {
//            player.stop();
//        }
//    }
//
}

