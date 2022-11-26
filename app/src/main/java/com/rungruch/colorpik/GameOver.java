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

public class GameOver extends AppCompatActivity {
    public static final String PREFS_DATA_Settings = "Settings_PREFS";
    public static final String Pref_Score_Easy = "Score_Easy_Pref";
    public static final String Pref_Score_Hard = "Score_Hard_Pref";

    private String vibrate_status;
    private String sound_status;


    private String difficulty_select;
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
        setContentView(R.layout.activity_game_over);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences settings_pref = getSharedPreferences(PREFS_DATA_Settings, MODE_PRIVATE);
        vibrate_status = settings_pref.getString("vibrate_status","1");
        sound_status = settings_pref.getString("sound_status","0");

        TextView txt = findViewById(R.id.scoretext);
        String extra = getIntent().getStringExtra("param");
        txt.setText(extra);
        difficulty_select = getIntent().getStringExtra("diff_status");

        SharedPreferences Score_Easy = getSharedPreferences(Pref_Score_Easy, MODE_PRIVATE);
        SharedPreferences Score_Hard = getSharedPreferences(Pref_Score_Hard, MODE_PRIVATE);
        int old_highscore_easy = Integer.parseInt(Score_Easy.getString("score_easy","0"));
        int old_highscore_hard = Integer.parseInt(Score_Hard.getString("score_hard","0"));

        TextView highscore_text = findViewById(R.id.highscoretext3);
        if(Integer.parseInt(difficulty_select) == 0 && Integer.parseInt(extra) > old_highscore_easy ){
            highscore_text.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = Score_Easy.edit();
            editor.putString("score_easy",extra);
            editor.apply();
        }else if (Integer.parseInt(difficulty_select) == 1 && Integer.parseInt(extra) > old_highscore_hard){
            highscore_text.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = Score_Hard.edit();
            editor.putString("score_hard",extra);
            editor.apply();
        }


        mediaPlayer = MediaPlayer.create(this, R.raw.correct);

        final Button tryagain_btn = findViewById(R.id.tryagain);
        tryagain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(Integer.parseInt(sound_status) == 1){mediaPlayer.start();}
                if(Integer.parseInt(vibrate_status) == 1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                    } else {
                        v.vibrate(10);
                    }}
                Intent intent = new Intent(GameOver.this, GameActivity.class);
                intent.putExtra("diff_status",difficulty_select);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });



        final Button MainMenu = findViewById(R.id.mainmenu);
        MainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(Integer.parseInt(sound_status) == 1){mediaPlayer.start();}
                if(Integer.parseInt(vibrate_status) == 1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                    } else {
                        v.vibrate(10);
                    }}
                Intent intent = new Intent(GameOver.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        mediaPlayer = MediaPlayer.create(this, R.raw.correct);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onStop() {

        releaseMediaPlayerResources();
        super.onStop();
    }
    void releaseMediaPlayerResources() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}