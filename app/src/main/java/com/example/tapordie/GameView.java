package com.example.tapordie;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<BaseObject> arrObstacles;
    private ArrayList<BackgroundObject> bgObjects;
    private int sumpipe, distance;
    private int score, bestscore = 0;
    private boolean start;
    private Context context;
    public ArrayList<Bitmap> copterOne;

    public GameView (Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context;

        copterOne = new ArrayList<Bitmap>();
        copterOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter1));
        copterOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter2));
        copterOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter3));
        copterOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter2));


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
        for (BackgroundObject bgObj:bgObjects) {
            bgObj.reset();
        }
        for (BaseObject baseObj:arrObstacles) {
            baseObj.reset();
        }
        chopper.reset();
        Log.i("weather available", "check value");

    }

    private void initClouds(){
        bgObjects = new ArrayList<>();
        this.bgObjects.add(new Cloud(Constants.SCREEN_WIDTH, 0, 600 * Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
        this.bgObjects.get(this.bgObjects.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.cloud));

    }

    private void initObstacles() {
        arrObstacles = new ArrayList<>();

        this.arrObstacles.add(new Tree(Constants.SCREEN_WIDTH,
                0, 600 * Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
        this.arrObstacles.get(this.arrObstacles.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.tree));
        this.arrObstacles.add(new Tree(Constants.SCREEN_WIDTH,
                0, 600 * Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
        this.arrObstacles.get(this.arrObstacles.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.tree));
        this.arrObstacles.add(new Balloon(Constants.SCREEN_WIDTH,
                0, 600 * Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
        this.arrObstacles.get(this.arrObstacles.size() -1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.balloon));
        this.arrObstacles.add(new Balloon(Constants.SCREEN_WIDTH,
                0, 600 * Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
        this.arrObstacles.get(this.arrObstacles.size() -1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.balloon));
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
                if(this.chopper.getX()+this.chopper.getWidth()>arrObstacles.get(i).getX()+arrObstacles.get(i).getWidth()/2
                        && this.chopper.getX()+this.chopper.getWidth()<=arrObstacles.get(i).getX()+arrObstacles.get(i).getWidth()/2 + Pipe.speed
                        && i<sumpipe/2){
                    score++;
                    if(score > bestscore) {
                        bestscore = score;
                        SharedPreferences sp = context.getSharedPreferences("gamesetting", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore", bestscore);
                        editor.apply();
                    }
                    MainActivity.txt_score.setText(""+score);
                }
                //reset water pipes position if out of screen.
                if(this.arrObstacles.get(i).getX() < -arrObstacles.get(i).getWidth()){
                    this.arrObstacles.get(i).reset();
                    double rand = random();
                    this.arrObstacles.get(i).setX(Constants.SCREEN_WIDTH + ((float)rand * 2000));
                    this.setActivated(false);
                }
                this.arrObstacles.get(i).draw(canvas);
            }
        } else {
            if(bird.getY()>Constants.SCREEN_HEIGHT/2){
                bird.setDrop(-15*Constants.SCREEN_HEIGHT/1920);
            }
            bird.draw(canvas);
        }
        // update every 0.01 seconds
        handler.postDelayed(r, 10);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            bird.setDrop(-15);
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
}
