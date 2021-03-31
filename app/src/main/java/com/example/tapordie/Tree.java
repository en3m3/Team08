package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Tree extends BaseObject {
    public static int speed;

    public Tree(float x, float y, int width, int height) {
        super(x, y, width, height);
    }
    public void draw(Canvas canvas) {
        if(active) {
            canvas.drawBitmap(this.bm, this.x, this.y, null);
            this.x -= speed;
//        speed = 10*Constants.SCREEN_WIDTH/1080;
            speed = 20 * Constants.SCREEN_WIDTH / 1080;
        }
    }

    @Override
    public void reset() {
        super.reset();

    }

    public void randomY() {
//        Random r = new Random();
//        this.y = r.nextInt((this.height/4) + 1) - this.height/4;
        this.y = 0 - this.height;
    }

    @Override
    public void setBm(Bitmap bm) {
        // sets the bitmaps = to the width and height of the water pipe
        this.bm = Bitmap.createScaledBitmap(bm, width, height, true);
    }
}
