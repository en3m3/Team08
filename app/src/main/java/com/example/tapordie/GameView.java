package com.example.tapordie;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
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
    private ArrayList<BaseObject> arrEffects;

    private int score, bestscore = 0;
    private boolean start;
    private WeatherInfo weather;
    private Context context;
    private Explosion chopperExplode;
    Thread weatherThread;

    public ArrayList<Bitmap> copterOne;
    public ArrayList<Bitmap> treeOne;
    public ArrayList<Bitmap> balloonOne;
    public ArrayList<Bitmap> cloudOne;
    public ArrayList<Bitmap> bgTreeOne;
    public ArrayList<Bitmap> treeTwo;
    public ArrayList<Bitmap> cloudTwo;
    public ArrayList<Bitmap> darkCloud;
    public ArrayList<Bitmap> explosion;

    /**
     * Contains the game loop
     * has the game logic and manages all aspects of the game
     * @param context
     * @param attrs
     */
    public GameView (Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        weather = new WeatherInfo(context);

        weatherThread = new Thread(weather);
        weatherThread.start();

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
    }

    /**
     * calls the initialize method for each object array
     * Waits for the weather api thread to complete then calls the function to change the weather
     */
    private void init() {
        initBgObjects();
        initChopper();
        initObstacles();
        initEffects();
            Log.i("weather available", "check value");
        try {
            weatherThread.join();
            theWeather();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the background objects
     */
    private void initBgObjects(){
        bgObjects = new ArrayList<>();
        this.bgObjects.add(new Cloud(cloudOne));
        this.bgObjects.add(new Cloud(cloudOne));
        this.bgObjects.add(new Cloud(cloudTwo));
        this.bgObjects.add(new Cloud(cloudTwo));
        this.bgObjects.add(new Cloud(darkCloud));
        this.bgObjects.add(new BgTree(bgTreeOne));
        this.bgObjects.add(new BgTree(bgTreeOne));
        this.bgObjects.add(new BgTree(bgTreeOne));
        this.bgObjects.add(new BgTree(bgTreeOne));
    }


    /**
     * creates the obstacle objects
     */
    private void initObstacles() {
        arrObstacles = new ArrayList<>();
        this.arrObstacles.add(new Tree(treeOne));
        this.arrObstacles.add(new Tree(treeOne));
        this.arrObstacles.add(new Tree(treeTwo));
        this.arrObstacles.add(new Tree(treeTwo));
        this.arrObstacles.add(new Balloon(balloonOne));
        this.arrObstacles.add(new Balloon(balloonOne));
    }
    /**
     * creates the effect objects
     */
    private void initEffects() {
        arrEffects = new ArrayList<>();
        chopperExplode = new Explosion(explosion);
        this.arrEffects.add(chopperExplode);
    }
    /**
     * creates the helicopter objects
     */
    private void initChopper() {
        chopper = new Chopper(copterOne);
    }

    /**
     * Contains most of the logic of movement and collision detection/score/game over
     * @param canvas
     */
    public void draw(Canvas canvas){
        super.draw(canvas);

        for (BaseObject obj: arrEffects ) {
            if(obj.active) {
                obj.draw(canvas);
            }
        }
        if(chopperExplode.done) {
            reset();
        }
        if(start) {
            if((random() * 10) < 10) {
                int i = (int) floor(random() * bgObjects.size());
                if(!bgObjects.get(i).isActive()) {
                    bgObjects.get(i).activate();
                }
            }
            for (BackgroundObject obj: bgObjects ) {
                obj.draw(canvas);
            }
            chopper.draw(canvas);
            if((random() * 10) < 5) {
                int i = (int) floor(random() * arrObstacles.size());
                if(!arrObstacles.get(i).isActive()) {
                    arrObstacles.get(i).activate();
                }
            }
            for (BackgroundObject bgObj :bgObjects ) {
                if(bgObj.getX() < 0 - bgObj.width) {
                    bgObj.reset();
                }
            }

            for(int i = 0; i < arrObstacles.size()-1; i++) {
                /* Game Over */
                if(chopper.getRect().intersect(arrObstacles.get(i).getRect()) || chopper.getY()-chopper.getHeight()<0 || chopper.getY() > Constants.SCREEN_HEIGHT){
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("best: "+ bestscore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.rl_game_over.setVisibility(VISIBLE);
                    chopperExplode.triggered(chopper);
                    start = false;

                }
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
            if(start) {
                chopper.draw(canvas);
            }
        }
        chopper.update();
        // update every 0.01 seconds
        handler.postDelayed(r, 10);

    }

    /**
     * Player touch input
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(start) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                chopper.directionCheck(event.getY());
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    public boolean isStart() {
        return start;
    }

    /**
     *
     * @param start
     */
    public void setStart(boolean start) {
        this.start = start;
    }

    /**
     * resets all objects, typically after a gameover
     */
    public void reset() {
        MainActivity.txt_score.setText("0");
        score = 0;
        chopper.reset();
        for (BackgroundObject bgObj :bgObjects) {
            bgObj.reset();
        }
        for (BaseObject baseObj :arrObstacles) {
            baseObj.reset();
        }
        for (BaseObject baseObj :arrEffects) {
            baseObj.reset();
        }

        Log.e("pause"," ");
    }

    /**
     * receives the type of weather from the WeatherInfo and calls the constuctors for the weather types
     */
    public void theWeather(){
        WeatherType weathertype = this.weather.getWeatherDescription();
        weathertype = WeatherType.RAIN;
        switch (weathertype) {
            case RAIN:
                RAIN rain = new RAIN();
                break;
            case SNOW:
                SNOW snow = new SNOW ();
                break;
            case CLOUDY:
                break;
            case SUNNY:
                break;
            default:
                break;
        }
    }

    /**
     * Loads all image resources into variables
     */
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

        cloudTwo = new ArrayList<Bitmap>();
        cloudTwo.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cloud2));


        bgTreeOne = new ArrayList<Bitmap>();
        bgTreeOne.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bgtrees));

        treeTwo = new ArrayList<Bitmap>();
        treeTwo.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.tree3));

        darkCloud = new ArrayList<Bitmap>();
        darkCloud.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.darkcloud));

        explosion = new ArrayList<Bitmap>();
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0014_1));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0013_2));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0012_3));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0011_4));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0010_5));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0009_6));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0008_7));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0007_8));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0006_9));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0005_10));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0004_11));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0003_12));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0002_13));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0001_14));
        explosion.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion_0000_15));



    }
}
