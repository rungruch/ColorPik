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
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFS_DATA_Settings = "Settings_PREFS";
    private String vibrate_status;
    private String sound_status;
    private String moving_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences settings_pref = getSharedPreferences(PREFS_DATA_Settings, MODE_PRIVATE);
        vibrate_status = settings_pref.getString("vibrate_status","1");
        sound_status = settings_pref.getString("sound_status","0");
        moving_status = settings_pref.getString("moving_status","1");

        SwitchMaterial sound = findViewById(R.id.sound);
        SwitchMaterial vibrate = findViewById(R.id.vibration);
        SwitchMaterial moving = findViewById(R.id.moving);

        if(Integer.parseInt(sound_status) == 0){sound.setChecked(sound.isChecked());}else{sound.setChecked(!sound.isChecked());}
        if(Integer.parseInt(vibrate_status) == 0){vibrate.setChecked(vibrate.isChecked());}else{vibrate.setChecked(!vibrate.isChecked());}
        if(Integer.parseInt(moving_status) == 0){moving.setChecked(moving.isChecked());}else{moving.setChecked(!moving.isChecked());}

        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings_pref.edit();
                if(b){
                  //  Log.d(TAG, "onCheckedChanged: = true//"+b);
                    sound_status = "1";
                    editor.putString("sound_status","1");
                }else{ //Log.d(TAG, "onCheckedChanged: = false//"+b);
                    sound_status = "0";
                    editor.putString("sound_status","0");
                }
                editor.putString("vibrate_status",vibrate_status);
                editor.putString("moving_status",moving_status);
                editor.apply();

            }
        });

        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings_pref.edit();
                if(b){
                    //  Log.d(TAG, "onCheckedChanged: = true//"+b);
                    editor.putString("vibrate_status","1");
                }else{ //Log.d(TAG, "onCheckedChanged: = false//"+b);
                    editor.putString("vibrate_status","0");
                }
                editor.putString("sound_status",sound_status);
                editor.putString("moving_status",moving_status);
                editor.apply();

            }
        });

        moving.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings_pref.edit();
                if(b){
                    //  Log.d(TAG, "onCheckedChanged: = true//"+b);
                    editor.putString("moving_status","1");
                }else{ //Log.d(TAG, "onCheckedChanged: = false//"+b);
                    editor.putString("moving_status","0");
                }
                editor.putString("vibrate_status",vibrate_status);
                editor.putString("sound_status",sound_status);
                editor.apply();

            }
        });
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.correct);
        ImageView prev = findViewById(R.id.previous);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Integer.parseInt(sound_status) == 1){mp.start();}

                if(Integer.parseInt(vibrate_status) == 1){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));

                    } else {
                        //deprecated in API 26
                        v.vibrate(10);
                    }}
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

    }


    @Override
    protected void onResume() {
        MediaPlayer.create(this, R.raw.correct);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}