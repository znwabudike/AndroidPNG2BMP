package com.github.znwabudike.androidpng2bmp.hackactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by znwabudike on 10/22/14.
 */
public class Hacktivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView text = new TextView(this);
        text.setText("There has to be a better way to unit test");
        setContentView(text);
    }
}