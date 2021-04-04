package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Background cloud object. Renders in the background layer, restricted to the top half of the screen.
 */
public class Cloud extends BackgroundObject{
    private int speed;
    public Cloud(ArrayList<Bitmap> cloudBitmap) {
        super();
        this.width = cloudBitmap.get(0).getWidth();
        this.height = cloudBitmap.get(0).getHeight();
        setBm(cloudBitmap.get(0));
        this.reset();
    }

    /**
     * sets random x off the screen to create variety in timing and creates a random for the y value as well
     */
    public void reset(){
        this.y = (float) (Constants.SCREEN_HEIGHT/2 - Math.random()*Constants.SCREEN_HEIGHT/2);
        this.x = (float) (Constants.SCREEN_WIDTH + Math.random()*5000);
        this.speed = (int) ((Math.random()*4) + 1);
    }

    /**
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
        this.x -=speed;
    }
}
