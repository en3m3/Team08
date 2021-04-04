package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

/**
 * tree obstacle. Stays at the bottom area of the screen
 */
public class Tree extends BaseObject {
    public static int speed;

    public Tree(ArrayList<Bitmap> treeBitmap){
        this.width = treeBitmap.get(0).getWidth();
        this.height = treeBitmap.get(0).getHeight();
        setBm(treeBitmap.get(0));

        this.reset();
    }
    public void draw(Canvas canvas) {
        if(active) {
            canvas.drawBitmap(this.bm, this.x, this.y, null);
            this.x -= speed;
            speed = 20 * Constants.SCREEN_WIDTH / 1080;
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.y = Constants.SCREEN_HEIGHT - ((float)Math.random() * 500);
        this.x = Constants.SCREEN_WIDTH +  ((float)Math.random() * 1500);
    }



    @Override
    public void setBm(Bitmap bm) {
        // sets the bitmaps = to the width and height of the water pipe
        this.bm = Bitmap.createScaledBitmap(bm, width, height, true);
    }
}
