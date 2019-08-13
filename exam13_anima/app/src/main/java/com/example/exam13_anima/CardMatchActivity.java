package com.example.exam13_anima;

import android.app.Activity;
import android.os.Bundle;

public class CardMatchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CardGameView(this));
    }
}
