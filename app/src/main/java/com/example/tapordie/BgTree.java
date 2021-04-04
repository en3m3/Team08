package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * a background version of the tree object. It does not collide with the chopper and it is limited to the bottom area of the screen
 */
public class BgTree extends BackgroundObject{
    public BgTree(ArrayList<Bitmap> treeBitmap) {
        super();
        this.width = treeBitmap.get(0).getWidth();
        this.height = treeBitmap.get(0).getHeight();
        setBm(treeBitmap.get(0));
        this.reset();
    }

    public void reset(){
        this.y = Constants.SCREEN_HEIGHT- this.height/2;
        this.x = (float) (Constants.SCREEN_WIDTH + Math.random()*5000);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
        this.x -= 5;
    }
}