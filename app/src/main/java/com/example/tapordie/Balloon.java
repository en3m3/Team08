package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

public class Balloon extends BaseObject {
    public static int speed;

    /**
     * Sets up the balloon bitmap
     * @param balloonBitmap
     */
    public Balloon(ArrayList<Bitmap> balloonBitmap) {
        this.width = balloonBitmap.get(0).getWidth();
        this.height = balloonBitmap.get(0).getHeight();
        setBm(balloonBitmap.get(0));
        this.reset();
    }

    /**
     * draws baloon and has movement logic
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if(active) {
            canvas.drawBitmap(this.bm, this.x, this.y, null);
            this.x -= speed;
            speed = 20 * Constants.SCREEN_WIDTH / 1080;
            this.y -= 1;
        }
    }
/**
 * resets the balloon when it reaches the edge of the screen
 */
    @Override
    public void reset() {
        super.reset();

        this.y = (float) (Constants.SCREEN_HEIGHT/2 - Math.random()*Constants.SCREEN_HEIGHT/2) - this.height/4;
        this.x = Constants.SCREEN_WIDTH +  ((float)Math.random() * 1500);
    }

    /**
     * adjusts for scale if needed
     * @param bm
     */
    @Override
    public void setBm(Bitmap bm) {
        this.bm = Bitmap.createScaledBitmap(bm, width, height, true);
    }
}
