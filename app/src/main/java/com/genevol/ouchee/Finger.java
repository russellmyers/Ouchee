package com.genevol.ouchee;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by RussellM on 1/06/2016.
 */
public class Finger {

    Point nail;
    Point base;
    float m;
    float c;
    Context con;

    float sizeScale = 1.0f; // reference is 300dp x 300dp

    public Finger(Point inBase,Point inNail,Context inContext) {
        nail = inNail;
        base = inBase;

        con = inContext;

        float dpi = con.getResources().getDisplayMetrics().density;
        Log.d("dpi scale: ","" +dpi);
        float r = dpi / 1.5f;
        //reference dpi ratio is 1.5

        nail.x*=r *sizeScale;
        nail.y*=r * sizeScale;
        base.x*=r * sizeScale;
        base.y*=r * sizeScale;

        if (nail.x == base.x) {
            m = 99999;
            c = 0;
        }
        else {
            m = 1.0f * (nail.y - base.y) / (nail.x - base.x);
            c = base.y - (m * base.x);
        }


    }

    public String toString() {
        return "base: " + base.toString() + " nail: " + nail.toString() + " m: " + m + " c: " + c;
    }

    public boolean pointIsLeft(Point p) {
        if (m == 99999) {
            return (p.x <= base.x);
        }
        else {
            if (m > 0) { //left on top
                return (p.y >= p.x * m + c);

            }
            else {
                return (p.y <= p.x * m + c);

            }
        }

    }
}
