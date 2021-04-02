package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Cloud extends BackgroundObject{
    public Cloud(ArrayList<Bitmap> cloudBitmap) {
        super();
        this.width = cloudBitmap.get(0).getWidth();
        this.height = cloudBitmap.get(0).getHeight();
        setBm(cloudBitmap.get(0));
        this.reset();
    }



    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
        this.x -= 5;
        if(this.x < -1000){
            this.x = Constants.SCREEN_WIDTH;
            int random = (int)Math.round(Math.random()*Constants.SCREEN_HEIGHT);
            if(this.y < 1500) {
                this.y = 1500;
            } else if(this.y > Constants.SCREEN_HEIGHT) {
                this.y = Constants.SCREEN_HEIGHT - 100;
            }
        }
    }
}
