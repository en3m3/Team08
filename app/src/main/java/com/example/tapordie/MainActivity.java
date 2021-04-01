package com.example.tapordie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static TextView txt_score, txt_best_score, txt_score_over, txt_welcome;
    public static RelativeLayout rl_game_over;
    public static Button btn_start;
    private GameView gv;
    private RAIN rain;
    private SNOW snow;

    ImageView btn_pause, btn_play;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        RAIN rain = new RAIN();
        SNOW snow = new SNOW();

        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_main);
        txt_score = findViewById(R.id.txt_score);
        txt_best_score = findViewById(R.id.txt_best_score);
        txt_score_over = findViewById(R.id.txt_score_over);
        rl_game_over = findViewById(R.id.rl_game_over);
        txt_welcome = findViewById (R.id.txt_welcome);
        btn_start = findViewById(R.id.btn_start);
        btn_play = findViewById(R.id.btn_play);
        btn_pause = findViewById(R.id.btn_pause);

        gv = findViewById(R.id.gv);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.setStart(true);
                txt_score.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.INVISIBLE);
                txt_welcome.setVisibility(View.INVISIBLE);
                btn_pause.setVisibility (View.VISIBLE);
            }
        });
        rl_game_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.setStart(false);
                btn_start.setVisibility(View.VISIBLE);
                rl_game_over.setVisibility(View.INVISIBLE);
                btn_play.setVisibility(View.INVISIBLE);
                btn_pause.setVisibility (View.INVISIBLE);
                gv.reset();

            }
        });

        btn_pause.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                gv.setStart (false);
                btn_play.setVisibility(View.VISIBLE);
                btn_pause.setVisibility (View.INVISIBLE);
            }
        });

        btn_play.setOnClickListener (new View.OnClickListener () {

            public void onClick(View v) {
                gv.setStart (true);
                btn_play.setVisibility (View.INVISIBLE);
                btn_pause.setVisibility (View.VISIBLE);

            }
        });

        class theWeather {
            WeatherType weathertype;

            public theWeather(WeatherType weathertype) {
                this.weathertype = weathertype;
            }
            public void weatherType() {
                switch (weathertype) {
                    case RAIN:
                        new RAIN();
                        break;

                    case SNOW:
                        new SNOW();
                        break;

                    case CLOUDY:
                        break;

                    case SUNNY:
                        break;

                    default:
                        gv.setStart(true);

                }
            }
        }

    }
}