package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Pipe extends BaseObject {
    public static int speed;

    public Pipe(float x, float y, int width, int height) {
        super(x, y, width, height);
    }
    public void draw(Canvas canvas) {
        if(active) {
            canvas.drawBitmap(this.bm, this.x, this.y, null);
            this.x -= speed;
            speed = 20 * Constants.SCREEN_WIDTH / 1080;
        }
    }

    @Override
    public void setBm(Bitmap bm) {
        // sets the bitmaps = to the width and height of the water pipe
        this.bm = Bitmap.createScaledBitmap(bm, width, height, true);
    }
}
