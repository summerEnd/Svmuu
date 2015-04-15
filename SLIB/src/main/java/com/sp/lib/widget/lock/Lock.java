package com.sp.lib.widget.lock;

import android.graphics.Point;

public class Lock {

    private char value;

    private int x;
    private int y;

    private char[] mSecret = new char[9];

    private int length = 0;

    public void appendSecret(char secret) {
        if (length >= mSecret.length) {
            return;
        }
        mSecret[length] = secret;
        length++;
    }

}
