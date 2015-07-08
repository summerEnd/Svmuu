package com.slib.demo.material;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.slib.demo.SLIBTest;
import com.svmuu.slibdemo.R;


public class AndroidLTest extends SLIBTest{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_test);
    }
}
