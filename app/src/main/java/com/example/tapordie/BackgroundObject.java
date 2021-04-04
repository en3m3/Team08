package com.example.tapordie;

import android.graphics.Bitmap;
import android.widget.BaseAdapter;

/**
 * base class for background objects that draw before the helicopter and obstacles
 */
public class BackgroundObject extends BaseObject {
    public BackgroundObject() {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        active = true;
    }

}
