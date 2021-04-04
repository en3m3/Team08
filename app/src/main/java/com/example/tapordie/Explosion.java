package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.Random;

/**
 * Not fully implemented. Is a death animation. I made it a new type of BaseObject so that it could render still after the game over pops up but the logic in the gameview hasn't fully been
 * modified to support it.
 */
public class Explosion extends BaseObject {
    private ArrayList<Bitmap> arrBms = new ArrayList<>();
    private int count, idCurrentBitmap;
    BaseObject explodedObj;
    boolean done;

    public Explosion(ArrayList<Bitmap> explosionBitmaps) {
        this.arrBms = explosionBitmaps;
        this.width = arrBms.get(0).getWidth();
        this.height = arrBms.get(0).getHeight();
        setArrBms(arrBms);
        this.setBm(arrBms.get(0));
        this.active = false;
        this.done = false;
        this.reset();

    }

    public void triggered(BaseObject explodedObject) {
        this.explodedObj = explodedObject;
        this.setX(explodedObject.getX());
        this.setY(explodedObject.getY());
    }

    @Override
    public void draw(Canvas canvas) {
        if(active) {
            canvas.drawBitmap(this.bm, this.x, this.y, null);
        }
    }

    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }


    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
        //scale bitmaps to size of Chopper
        for(int i = 0; i < arrBms.size(); i++){
            this.arrBms.set(i, Bitmap.createScaledBitmap(this.arrBms.get(i), this.width*2, this.height*2, true));
        }
    }



    @Override
    public Bitmap getBm() {
        count++;
//        if(this.count == this.vFlap){
        for(int i = 0; i <arrBms.size(); i++){
            if(i == arrBms.size()-1) {
                this.done = true;
            } else if (this.idCurrentBitmap == i) {
                idCurrentBitmap = i + 1;
                break;
            }
        }

//        }
        Matrix matrix = new Matrix();
        matrix.postRotate(-25);
        return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight(), matrix, true);

    }
    public void die() {
        this.reset();
    }

    public void reset() {
        this.count = 0;
        this.x = Constants.SCREEN_WIDTH + this.width;
        this.done = false;
    }



}
