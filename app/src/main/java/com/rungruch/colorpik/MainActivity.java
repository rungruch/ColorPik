package com.rungruch.colorpik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_DATA_Difficulty = "Difficulty_PREFS";
    public static final String PREFS_DATA_Settings = "Settings_PREFS";

   private String diff_status;
    private String vibrate_status;
    private String sound_status;
    private String moving_status;
    MediaPlayer mediaPlayer;
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayerResources();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        SharedPreferences shared_pref = getSharedPreferences(PREFS_DATA_Difficulty, MODE_PRIVATE);
        diff_status = shared_pref.getString("level_difficulty","0");
        SharedPreferences settings_pref = getSharedPreferences(PREFS_DATA_Settings, MODE_PRIVATE);
        vibrate_status = settings_pref.getString("vibrate_status","1");
        sound_status = settings_pref.getString("sound_status","0");
        moving_status = settings_pref.getString("moving_status","1");


        mediaPlayer = MediaPlayer.create(this, R.raw.correct);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.diff_tab);
        TabLayout.Tab tab = tabLayout.getTabAt(Integer.parseInt(diff_status));
        tab.select();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(getApplicationContext(),"Pos onTabSelected: "+tab.getPosition(),Toast.LENGTH_SHORT).show();

                diff_status = String.valueOf(tab.getPosition());
                SharedPreferences.Editor editor = shared_pref.edit();
                editor.putString("level_difficulty",diff_status);
                editor.apply();

                if(Integer.parseInt(sound_status) == 1){mediaPlayer.start();}
                if(Integer.parseInt(vibrate_status) == 1){
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));

                } else {
                    //deprecated in API 26
                    v.vibrate(10);
                }}
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                diff_status = "0";
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        final Button play_btn = findViewById(R.id.playbtn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(sound_status) == 1){mediaPlayer.start();}
                if(Integer.parseInt(vibrate_status) == 1){
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));

                } else {
                    //deprecated in API 26
                    v.vibrate(10);
                }}


                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                //   String currentDiff = String.valueOf(diff_status);
                intent.putExtra("diff_status",diff_status);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }


        });

        final Button settings_btn = findViewById(R.id.setting);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(sound_status) == 1){mediaPlayer.start();}
                if(Integer.parseInt(vibrate_status) == 1){
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));

                } else {
                    //deprecated in API 26
                    v.vibrate(10);
                }}

                Intent intent_setting = new Intent(MainActivity.this, SettingsActivity.class);
                //   String currentDiff = String.valueOf(diff_status);
                startActivity(intent_setting);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();


            }


        });

}
    @Override
    protected void onStop() {

        // Before going to stop state release the
        // allocated mediaplyer resources
        releaseMediaPlayerResources();

        super.onStop();
    }

    @Override
    protected void onResume(){
        mediaPlayer = MediaPlayer.create(this, R.raw.correct);
        super.onResume();
    }

    void releaseMediaPlayerResources() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


}
