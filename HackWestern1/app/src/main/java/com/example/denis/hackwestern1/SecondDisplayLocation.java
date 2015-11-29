package com.example.denis.hackwestern1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class SecondDisplayLocation extends AppCompatActivity {
TextView location1, sublocation1, wait1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_display_location);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        String estiName1 = getIntent().getStringExtra("estiName1");
        String estiLoca1= getIntent().getStringExtra("estiLoca1");
        String waitt1= getIntent().getStringExtra("waitt1");
        location1 = (TextView) findViewById(R.id.textView11);
        location1.setText(estiName1);
        sublocation1 = (TextView) findViewById(R.id.textView9);
        sublocation1.setText(estiLoca1);
        wait1 = (TextView) findViewById(R.id.textView10);
        wait1.setText(waitt1);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}