package com.kevin.piazzoli.iptvandroid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class Video extends Activity {


    private static final String TAG = "";
    VideoView reproductor;
    String url = "http://149.56.17.15/contenido/series/12monos/t1/ep01.mkv";
    TextView subtitle;
    private int position = 0;




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        subtitle = (TextView) findViewById(R.id.textView);
        reproductor = (VideoView) findViewById(R.id.videoView);

        url = getIntent().getStringExtra("ruta");

        video(url);

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void video(String url){

        reproductor.setVideoPath(url);
        MediaController controles = new MediaController(this);
        controles.setAnchorView(reproductor);
        reproductor.setMediaController(controles);

        Log.d(TAG, "mostrar video");

        //Noc como hacer funcionar esta linea dpara los subtitle
        //reproductor.addSubtitleSource(getResources(url), MediaFormat.createAudioFormat("text/vtt", Locale.ENGLISH.getLanguage()));

        //Uri sourcer_1 = Uri.parse("android.resource://com.example.wolf.alphabeta2/"
        // + R.raw.ep01sub);

        //reproductor.addSubtitleSource(sourcer_1, MediaFormat.createSubtitleFormat("text/vtt", "en"));

        //reproductor.addSubtitleSource(getSubtitleSource(url),MediaFormat.createSubtitleFormat("text/srt", Locale.ENGLISH.getLanguage()));

        reproductor.start();

    }

    private InputStream getSubtitleSource(String filepath) {

        InputStream ins = null;
        String ccFileName = filepath.substring(0,filepath.lastIndexOf('.')) + ".srt";
        File file = new File(ccFileName);
        if (file.exists() == false)
        {
            Log.d(TAG,"no cierre la carpeta del capitulo " + ccFileName);
            return null;
        }
        FileInputStream fins = null;
        try {
            fins = new FileInputStream(file);
        }catch (Exception e) {
            Log.d(TAG,"exception en el try" + e);
        }
        ins = (InputStream)fins;
        return ins;

    }


}

