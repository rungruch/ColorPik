package com.rungruch.colorpik;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Vibrator;
import android.widget.Toast;

import java.util.Random;

@SuppressWarnings("deprecation")
public class GameActivity extends AppCompatActivity {
    public static final String PREFS_DATA_Settings = "Settings_PREFS";
    public static final String Pref_Score_Easy = "Score_Easy_Pref";
    public static final String Pref_Score_Hard = "Score_Hard_Pref";
    private String vibrate_status;
    private String sound_status;
    private String moving_status;

    private  static  int count_time = 0;
    private static int time_hard = 3;
    private static int time_easy = 6;

    private int currentScore = 0;
    private String highestScore;
   private  double dif_status = 0.05;
    private String diff_col ;
    private String difficulty_select;
    private int dialog_is_show = 0;

    private CountDownTimer countDownTimer = null;

    MediaPlayer correct_player;
    MediaPlayer wrong_player;
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayerResources();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        difficulty_select = getIntent().getStringExtra("diff_status");

        SharedPreferences settings_pref = getSharedPreferences(PREFS_DATA_Settings, MODE_PRIVATE);
        vibrate_status = settings_pref.getString("vibrate_status","1");
        sound_status = settings_pref.getString("sound_status","0");
        moving_status = settings_pref.getString("moving_status","1");

        SharedPreferences score_easy_pref = getSharedPreferences(Pref_Score_Easy, MODE_PRIVATE);
        SharedPreferences score_hard_pref = getSharedPreferences(Pref_Score_Hard, MODE_PRIVATE);
        final TextView highscore_label = findViewById(R.id.highscoretext);
        if(Integer.parseInt(difficulty_select) == 0){
            highestScore = score_easy_pref.getString("score_easy","0");
            highscore_label.setText(getString(R.string.EasyHighScore));
        }else{
            highestScore = score_hard_pref.getString("score_hard","0");
            highscore_label.setText(getString(R.string.HardHighScore));
        }
        final TextView highscore_val = findViewById(R.id.highscore_val);
        highscore_val.setText(String.valueOf(highestScore));


        start();
        correct_player = MediaPlayer.create(this, R.raw.correct);
        final ImageView pause = findViewById(R.id.pause);
        pause.setOnClickListener(view -> {
            Vibrator a = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if(Integer.parseInt(sound_status) == 1){correct_player.start();}
            if(Integer.parseInt(vibrate_status) == 1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    a.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                } else {
                    a.vibrate(10);
                }}
            showCustomDialog();
        });

        Button[] buttons = new Button[]{
                findViewById(R.id.btn1), findViewById(R.id.btn2), findViewById(R.id.btn3), findViewById(R.id.btn4),
                findViewById(R.id.btn5), findViewById(R.id.btn6), findViewById(R.id.btn7), findViewById(R.id.btn8),
                findViewById(R.id.btn9), findViewById(R.id.btn10), findViewById(R.id.btn11), findViewById(R.id.btn12),
                findViewById(R.id.btn13), findViewById(R.id.btn14), findViewById(R.id.btn15), findViewById(R.id.btn16)
        };

        for (int i = 0; i < buttons.length; i++) {
            final String btnName = "btn" + (i + 1);
            buttons[i].setOnClickListener(view -> check_button(btnName));
        }
    }

    @Override
    protected void onResume() {
        correct_player = MediaPlayer.create(this, R.raw.correct);
        if(dialog_is_show == 0){
        if(Integer.parseInt(difficulty_select) == 0) {time(count_time,time_easy);
        }else{time(count_time,time_hard);}}
        super.onResume();
    }

    //Function to display the custom dialog.
    void showCustomDialog() {
        final Dialog dialog = new Dialog(GameActivity.this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.trans);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
      dialog.setCancelable(false);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.custom_dialog);
        countDownTimer.cancel();
        dialog_is_show = 1;

        //Initializing the views of the dialog.
        correct_player = MediaPlayer.create(this, R.raw.correct);
        Button resume_btn = dialog.findViewById(R.id.resume);
        resume_btn.setOnClickListener(v -> {
           Vibrator a = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if(Integer.parseInt(sound_status) == 1){correct_player.start();}
            if(Integer.parseInt(vibrate_status) == 1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    a.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                } else {
                    a.vibrate(10);
                }}
            dialog_is_show = 0;
            dialog.dismiss();

            if(Integer.parseInt(difficulty_select) == 0) {time(count_time,time_easy);
            }else{time(count_time,time_hard);}
        });

        Button menu_btn = dialog.findViewById(R.id.main_menu);
        menu_btn.setOnClickListener(v -> {
            Vibrator a = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if(Integer.parseInt(sound_status) == 1){correct_player.start();}
            if(Integer.parseInt(vibrate_status) == 1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    a.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                } else {
                    a.vibrate(10);
                }}
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            intent.putExtra("diff_status",difficulty_select);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            dialog_is_show = 0;
            dialog.dismiss();
        });


        dialog.show();
    }


    @Override
    protected void onStop() {
        releaseMediaPlayerResources();
        super.onStop();
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        countDownTimer.cancel();
        super.onPause();
    }


    void releaseMediaPlayerResources() {
        if (correct_player != null) {
            // it is safe to stop playing the audio file
            // before releasing the audio file
            correct_player.stop();

            // after stop playing the audio file
            // release the audio resources
            correct_player.release();
        }

        if (wrong_player != null) {

            wrong_player.release();
        }

    }



    private void start(){
        final TextView score = findViewById(R.id.scoretext);
        score.setText(String.valueOf(0));
        random_color(dif_status);

        if(Integer.parseInt(difficulty_select) == 0) {
            setCount_time(time_easy);
            time(count_time,time_easy);
        }else{setCount_time(time_hard);
            time(count_time,time_hard);}

    }
    private void start(double dif){
        dif_status += 0.05;
        random_color(dif_status);

        if(Integer.parseInt(difficulty_select) == 0) {
            setCount_time(time_easy);
            time(count_time,time_easy);
        }else{setCount_time(time_hard);
            time(count_time,time_hard);}
    }

    private  void setCount_time(int a_time){
        count_time = a_time;
    }
    private void time(int count,int fullTime){
      //  int CurrentProgress = count;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        ProgressBar progressBar = findViewById(R.id.progressBar);
        countDownTimer = new CountDownTimer(count*1000, 1000) {
          //  final TextView timer = findViewById(R.id.time);
            public void onTick(long millisUntilFinished) {
                setCount_time((int)(millisUntilFinished / 1000));
                //timer.setText("" + millisUntilFinished / 1000);
                progressBar.setProgress((int)(millisUntilFinished / 1000));
                progressBar.setMax(fullTime-1);
            }

            public void onFinish() {
                final TextView score = findViewById(R.id.scoretext);
                Intent intent = new Intent(GameActivity.this, GameOver.class);
                String currentScore = score.getText().toString();
                intent.putExtra("param",currentScore);
                intent.putExtra("diff_status",difficulty_select);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        };
        countDownTimer.start();
    }

    private void random_color(double difc){
        Button[] buttons = new Button[]{
                findViewById(R.id.btn1), findViewById(R.id.btn2), findViewById(R.id.btn3), findViewById(R.id.btn4),
                findViewById(R.id.btn5), findViewById(R.id.btn6), findViewById(R.id.btn7), findViewById(R.id.btn8),
                findViewById(R.id.btn9), findViewById(R.id.btn10), findViewById(R.id.btn11), findViewById(R.id.btn12),
                findViewById(R.id.btn13), findViewById(R.id.btn14), findViewById(R.id.btn15), findViewById(R.id.btn16)
        };

        Random rnd = new Random();
        int r = rnd.nextInt(256);
        int g = rnd.nextInt(256);
        int b = rnd.nextInt(256);
        String hex = String.format("#%02x%02x%02x", r, g, b);
        ColorStateList baseColor = ColorStateList.valueOf(Color.parseColor(hex));

        for (Button btn : buttons) {
            btn.setBackgroundTintList(baseColor);
        }

        Resources res = getResources();
        final String[] btnList = res.getStringArray(R.array.all_button);
        int random = rnd.nextInt(16);

        // difc starts at 0.05 and increases by 0.05 per correct guess
        // We want a difference delta that gets smaller as difc gets larger
        // Base delta ranges from roughly 120 (easy) down to 15 (very hard).
        // 15 is roughly the limit of human visibility for neighboring colors.
        int targetDelta = (int) Math.max(15, 120 - (difc * 100));

        // Randomly decide whether to add or subtract the delta
        int sign = rnd.nextBoolean() ? 1 : -1;

        // Choose a random channel to modify (0=R, 1=G, 2=B)
        int channel = rnd.nextInt(3);

        int rr = r, gg = g, bb = b;
        if (channel == 0) {
            rr = clamp(r + (targetDelta * sign));
        } else if (channel == 1) {
            gg = clamp(g + (targetDelta * sign));
        } else {
            bb = clamp(b + (targetDelta * sign));
        }

        diff_col = btnList[random];
        String diffhex = String.format("#%02x%02x%02x", rr, gg, bb);

        buttons[random].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(diffhex)));

    }

    private int clamp(int val) {
        return Math.max(0, Math.min(255, val));
    }

    private void check_button(String diffString){
        final TextView score = findViewById(R.id.scoretext);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        correct_player = MediaPlayer.create(this, R.raw.correct);
        wrong_player = MediaPlayer.create(this, R.raw.wrong);



        if (diff_col.equals(diffString)){
            if(Integer.parseInt(sound_status) == 1){correct_player.start();}
            if(Integer.parseInt(vibrate_status) == 1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                } else {
                    v.vibrate(10);
                }}
            currentScore+=1;
            score.setText(String.valueOf(currentScore));
            countDownTimer.cancel();
            start(dif_status);
        }else{
            if(Integer.parseInt(sound_status) == 1){wrong_player.start();}
            if(Integer.parseInt(vibrate_status) == 1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(100);
                }}
            countDownTimer.cancel();
            if(Integer.parseInt(difficulty_select) == 0) {time(count_time-1,time_easy);
            }else{time(count_time-1,time_hard);}

        }
    }


}