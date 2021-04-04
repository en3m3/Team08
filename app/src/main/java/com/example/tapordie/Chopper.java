package com.example.tapordie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;


/**
 * Player object. Controls everything to do with how the helicopter behaves.
 */
public class Chopper extends BaseObject {
    private ArrayList<Bitmap> arrBms = new ArrayList<>();
    private int count, vFlap, idCurrentBitmap;
    private float drop;
    private int monkeyNums = 0;
    private int yMove = 0;
    private double speed = 5;
    private int moveTo = 0;
    int copter = 1; //number of which helicopter is being used
    private ChopperState state = ChopperState.NOTMOVING;

    public Chopper(ArrayList<Bitmap> copterSelected){
        super();
        this.count = 0;
        this.vFlap =0;
        this.idCurrentBitmap = 0;
        this.drop = 0;
        this.copter = 1;
        setWidth(copterSelected.get(0).getWidth()/18);
        setHeight(copterSelected.get(0).getHeight()/18);
        setX(10);
        setY(Constants.SCREEN_HEIGHT/2-getHeight()/2);
        setArrBms(copterSelected);
        this.reset();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
    }

    /**
     * Moves the helicopter based on it's state. Changes state to NOTMOVING when it reaches it's destination
     */
    public void update() {
        switch(this.state) {
            case MOVINGUP:
                this.y += this.speed;
                if(this.y > this.moveTo) {this.state=ChopperState.NOTMOVING;}
                break;
            case MOVINGDOWN:
                this.y -= this.speed;
                if(this.y < this.moveTo) {this.state=ChopperState.NOTMOVING;}
                break;
            default:
                break;
        }
    }

    /**
     * getter for the bitmap array
     * @return
     */
    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }

    /**
     * setter for the bitmap array
     * @param arrBms
     */
    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
        //scale bitmaps to size of Chopper
        for(int i = 0; i < arrBms.size(); i++){
            this.arrBms.set(i, Bitmap.createScaledBitmap(this.arrBms.get(i), this.width*2, this.height*2, true));
        }
    }

    /**
     * override of the base reset method. Resets to the middle of the screen.
     */
    @Override
    public void reset() {
        super.reset();
        this.y = Constants.SCREEN_HEIGHT/2 - this.height/2;
    }

    /**
     * getter for a single bitmap from the array. Controls the animation of the sprites
     * @return
     */
    @Override
    public Bitmap getBm() {
        count++;
//        if(this.count == this.vFlap){
        for(int i = 0; i <arrBms.size(); i++){
            if(i == arrBms.size()-1) {
                this.idCurrentBitmap = 0;
                break;
            } else if (this.idCurrentBitmap == i) {
                idCurrentBitmap = i + 1;
                break;
            }
        }
        count =0;
//        }
        Matrix matrix = new Matrix();
        if(state == ChopperState.MOVINGDOWN) {
            if (this.y - this.moveTo < 70) {
//                matrix.postRotate((float) (-45 + (.1* this.moveTo-this.y)));
                matrix.postRotate(-25);
            } else {
                matrix.postRotate(-25);
            }
        } else if(state == ChopperState.MOVINGUP) {
            if (this.moveTo - this.y < 70) {
//                matrix.postRotate((float) -.1* this.y-this.moveTo);
                matrix.postRotate(25);
            } else {
                matrix.postRotate(25);
            }
        } else {
            matrix.postRotate(10);
        }
        return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight(), matrix, true);
//        return this.arrBms.get(idCurrentBitmap);
    }

    /**
     *
     * @return
     */
    public int getMoveTo() {
        return this.moveTo;
    }

    /**
     *
     * @param yTarget
     */
    public void setMoveTo(int yTarget) {
        this.moveTo = yTarget;
    }

    /**
     *
     * @param newState
     */
    public void setState(ChopperState newState) {
        this.state = newState;
    }

    /**
     *
     * @return
     */
    public ChopperState getState() {
       return this.state;
    }

    /**
     *
     * @param y
     */
    public void directionCheck(float y) {
        this.moveTo = (int)y;
        if(y > this.y) {
            this.state = ChopperState.MOVINGUP;
        }
        else if(y < this.y) {
            this.state = ChopperState.MOVINGDOWN;
        } else {
            this.state = ChopperState.NOTMOVING;
        }
    }
}
