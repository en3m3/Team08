package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Balloon extends BaseObject {
    public static int speed;

    public Balloon(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        if(active) {
            canvas.drawBitmap(this.bm, this.x, this.y, null);
            this.x -= speed;
            speed = 20 * Constants.SCREEN_WIDTH / 1080;
            this.y += .5;
        }
    }

    @Override
    public void reset() {
        super.reset();
        Random r = new Random();
        this.y = r.nextInt((this.height/4) + 1) - this.height/4;
    }


    public void randomY() {

    }

    @Override
    public void setBm(Bitmap bm) {
        // sets the bitmaps = to the width and height of the water pipe
        this.bm = Bitmap.createScaledBitmap(bm, width, height, true);
    }
}
