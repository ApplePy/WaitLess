package com.example.denis.hackwestern1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class DisplayLocation extends AppCompatActivity {
    TextView location, sublocation, wait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String estiName = getIntent().getStringExtra("estiName");
        String estiLoca = getIntent().getStringExtra("estiLoca");
        String waitt = getIntent().getStringExtra("waitt");
        location = (TextView) findViewById(R.id.textView15);
        location.setText(estiName);
        sublocation = (TextView) findViewById(R.id.textView7);
        sublocation.setText(estiLoca);
        wait = (TextView) findViewById(R.id.textView8);
        wait.setText(waitt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
