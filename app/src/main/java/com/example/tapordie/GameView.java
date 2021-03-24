package com.example.tapordie;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameView extends View {
    private Chopper chopper;
    private Handler handler;
    private Runnable r;
    private  ArrayList<Pipe> arrPipes;
    private int sumpipe, distance;
    private int score, bestscore = 0;
    private boolean start;
    private WeatherInfo weather;
    private Context context;

    public GameView (Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting", context.MODE_PRIVATE);
        if(sp != null) {
            bestscore = sp.getInt("bestscore", 0);
        }
        start = false;
        score = 0;
        initChopper();
        initPipe();
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

//        weather = new WeatherInfo(context);
    }

    private void initPipe() {
        sumpipe = 6;
//        sumpipe = 0;
        distance = 350 *Constants.SCREEN_HEIGHT/1920;
        arrPipes = new ArrayList<>();
        for (int i = 0; i < sumpipe; i++) {
            if(i < sumpipe/2) {
                this.arrPipes.add(new Pipe(Constants.SCREEN_WIDTH+i*((Constants.SCREEN_WIDTH+600*Constants.SCREEN_WIDTH/1080)/(sumpipe/2)),
                        0, 600 * Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
//                this.arrPipes.get(this.arrPipes.size() -1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe2));
                this.arrPipes.get(this.arrPipes.size() -1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.balloon));
                this.arrPipes.get(this.arrPipes.size()-1).randomY();
            } else {
                this.arrPipes.add(new Pipe(this.arrPipes.get(i-sumpipe/2).getX(), this.arrPipes.get(i-sumpipe/2).getY()
                + this.arrPipes.get(i-sumpipe/2).getHeight() + this.distance, 600 * Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
//                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe1));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.tree));
            }
        }
    }

    private void initChopper() {
        chopper = new Chopper();
        chopper.setWidth(100*Constants.SCREEN_WIDTH/1000);
        chopper.setHeight(100*Constants.SCREEN_HEIGHT/1980);
        chopper.setX(100*Constants.SCREEN_WIDTH/1080);
        chopper.setY(Constants.SCREEN_HEIGHT/2-chopper.getHeight()/2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter2));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter3));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.copter2));
        chopper.setArrBms(arrBms);
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        if(start) {
            chopper.draw(canvas);
            for(int i = 0; i < sumpipe; i++) {
                if(chopper.getRect().intersect(arrPipes.get(i).getRect()) || chopper.getY()-chopper.getHeight()<0 || chopper.getY() > Constants.SCREEN_HEIGHT){
                    Pipe.speed = 0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("best: "+ bestscore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.rl_game_over.setVisibility(VISIBLE);

                }
                if(this.chopper.getX()+this.chopper.getWidth()>arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2
                        && this.chopper.getX()+this.chopper.getWidth()<=arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2 + Pipe.speed
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
                if(this.arrPipes.get(i).getX() < -arrPipes.get(i).getWidth()){
                    this.arrPipes.get(i).setX(Constants.SCREEN_WIDTH);
                    if(i < sumpipe/2) {
                        arrPipes.get(i).randomY();
                    } else {
                        arrPipes.get(i).setY(this.arrPipes.get(i - sumpipe/2).getY()
                                +this.arrPipes.get(i-sumpipe/2).getHeight() + this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);
            }
        } else {
            if(chopper.getY()>Constants.SCREEN_HEIGHT/2){
                chopper.setDrop(-15*Constants.SCREEN_HEIGHT/1920);
            }
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
        initPipe();
        initChopper();
    }
}
