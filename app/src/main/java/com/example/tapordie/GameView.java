package com.example.tapordie;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class GameView extends View {
    private Chopper chopper;
    private Handler handler;
    private Runnable r;
    private ArrayList<BaseObject> arrObstacles;
    private ArrayList<BackgroundObject> bgObjects;
    private int sumpipe, distance;
    private int score, bestscore = 0;
    private boolean start;
    private WeatherInfo weather;
    private Context context;

    public ArrayList<Bitmap> copterOne;
    public ArrayList<Bitmap> treeOne;
    public ArrayList<Bitmap> balloonOne;
    public ArrayList<Bitmap> cloudOne;

    public GameView (Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context;

        loadResources();

        SharedPreferences sp = context.getSharedPreferences("gamesetting", context.MODE_PRIVATE);
        if(sp != null) {
            bestscore = sp.getInt("bestscore", 0);
        }
        start = false;
        score = 0;
        init();
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        weather = new WeatherInfo(context);
        Thread weatherThread = new Thread(weather);
        weatherThread.start();


    }

    private void init() {
        initClouds();
        initChopper();
        initObstacles();

        Log.i("weather available", "check value");

    }

    private void initClouds(){
        bgObjects = new ArrayList<>();
        this.bgObjects.add(new Cloud(cloudOne));
    }

    private void initObstacles() {
        arrObstacles = new ArrayList<>();
        this.arrObstacles.add(new Tree(treeOne));
        this.arrObstacles.add(new Tree(treeOne));
        this.arrObstacles.add(new Balloon(balloonOne));
        this.arrObstacles.add(new Balloon(balloonOne));
    }

    private void initChopper() {
        chopper = new Chopper(copterOne);
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        if(start) {
            for (BackgroundObject obj: bgObjects ) {
                obj.draw(canvas);
            }

            chopper.draw(canvas);
            if((random() * 10) < 5) {
                int i = (int) Math.floor(random() * arrObstacles.size());
                arrObstacles.get(i).activate();
                Log.i("pause","paused");
            }


            for(int i = 0; i < arrObstacles.size()-1; i++) {
                if(chopper.getRect().intersect(arrObstacles.get(i).getRect()) || chopper.getY()-chopper.getHeight()<0 || chopper.getY() > Constants.SCREEN_HEIGHT){
                    Pipe.speed = 0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("best: "+ bestscore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.rl_game_over.setVisibility(VISIBLE);

                }
                //reset water pipes position if out of screen.
                if(this.arrObstacles.get(i).getX() < -arrObstacles.get(i).getWidth()){
                    this.arrObstacles.get(i).reset();
                    score++;
                    if(score > bestscore) {
                        bestscore = score;
                        SharedPreferences sp = context.getSharedPreferences("gamesetting", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore", bestscore);
                        editor.apply();
                    }
                    MainActivity.txt_score.setText(""+score);
                    double rand = random();
                    this.arrObstacles.get(i).setX(Constants.SCREEN_WIDTH + ((float)rand * 2000));
                    this.setActivated(false);
                }
                this.arrObstacles.get(i).draw(canvas);
            }
        } else {
            chopper.draw(canvas);
        }
        chopper.update();
        // update every 0.01 seconds
        handler.postDelayed(r, 10);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            chopper.directionCheck(event.getY());
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.txt_score.setText("0");
        score = 0;
        Log.e("pause"," ");
    }

    public void loadResources() {
        copterOne = new ArrayList<Bitmap>();
        copterOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter1));
        copterOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter2));
        copterOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter3));
        copterOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter2));

        treeOne = new ArrayList<Bitmap>();
        treeOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.tree));

        balloonOne = new ArrayList<Bitmap>();
        balloonOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.balloon));

        cloudOne = new ArrayList<Bitmap>();
        cloudOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cloud));
    }
}
