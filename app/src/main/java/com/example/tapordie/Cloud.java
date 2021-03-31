package com.example.tapordie;

import android.graphics.Canvas;

public class Cloud extends BackgroundObject{
    public Cloud(int x, int y, int width, int height) {
        super();
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
