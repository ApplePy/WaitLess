package com.example.denis.hackwestern1;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    /*@Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }*/
    private GoogleApiClient mGoogleApiClient;
    private Region region;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    TextView location, location1;
    TextView sublocation, sublocation1;
    TextView wait, wait1;
    LinearLayout colour, colour1;
    String red = "#f42b2b", green = "#129c08";
    String estiName = null;
    String estiName1 = null;
    String estiLoca = null;
    String estiLoca1 = null;
    String waitt = null;
    String waitt1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .build();
        beaconManager = new BeaconManager(this);
        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.FirstLocation);
        layout1.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DisplayLocation.class);
                i.putExtra("estiName", estiName);
                i.putExtra("estiLoca", estiLoca);
                i.putExtra("waitt", waitt);
                startActivity(i);
            }
        }));
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.SecondLocation);
        layout2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(), SecondDisplayLocation.class);
                j.putExtra("estiName1", estiName1);
                j.putExtra("estiLoca1", estiLoca1);
                j.putExtra("waitt1", waitt1);
                startActivity(j);
            }
        }));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://incode.ca:8888/output.php")
                .build();
        final AppCompatActivity z = this;
        Response response = null;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String x = response.body().string();
                JSONArray jObj = null;
                try {
                    jObj = new JSONArray(x);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final JSONArray kObj = jObj;

                z.runOnUiThread(new Runnable() {
                    public void run() {
                        setUI(kObj);
                    }
                });
            }

        });
    }

        @Override
        protected void onResume(){
            super.onResume();

            SystemRequirementsChecker.checkWithDefaultDialogs(this);

            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(region);
                }
            });

        }

    public void setUI(JSONArray jObj){

        JSONObject jObj1 = null;
        try {
            jObj1 = (JSONObject) jObj.get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        location = (TextView) findViewById(R.id.location1);
        try {
            estiName = jObj1.getString("EstimoteName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        location.setText(estiName);

        sublocation = (TextView) findViewById(R.id.textView2);
        try {
            estiLoca = jObj1.getString("EstimoteLocationName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sublocation.setText(estiLoca);

        //
        try {
            waitt = jObj1.getString("WaitTime");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        colour = (LinearLayout) findViewById(R.id.FirstLocation);

        if((Integer.parseInt(waitt)) <= 5){
            colour.setBackgroundColor(Color.GREEN);
        }
        else{
            colour.setBackgroundColor(Color.RED);
        }

        JSONObject jObj2 = null;
        try {
            jObj2 =(JSONObject)jObj.get(1);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        location1 = (TextView) findViewById(R.id.textView4);
        try {
            estiName1 = jObj2.getString("EstimoteName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        location1.setText(estiName1);

        sublocation1 = (TextView) findViewById(R.id.textView5);
        try {
            estiLoca1 = jObj2.getString("EstimoteLocationName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sublocation1.setText(estiLoca1);
        try {
            waitt1 = jObj2.getString("WaitTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        colour1 = (LinearLayout) findViewById(R.id.SecondLocation);
        if((Integer.parseInt(waitt1)) < 5){
            colour.setBackgroundColor(Color.GREEN);
        }
        else{
            colour.setBackgroundColor(Color.RED);
        }




        PebbleDictionary data = new PebbleDictionary();
        boolean isConnected = PebbleKit.isWatchConnected(this);
        Toast.makeText(this, "Pebble " + (isConnected ? "is" : "is not") + " connected!", Toast.LENGTH_LONG).show();
        final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");

        final Map data1 = new HashMap();
        data1.put("title", "Shortest Tims Line:");
        if(Integer.parseInt(waitt) < Integer.parseInt(waitt1)) {
            data1.put("body", estiName + " \n " + estiLoca);
        }
        else{
            data1.put("body", estiName1 + " \n " + estiLoca1);
        }
        final JSONObject jsonData = new JSONObject(data1);
        final String notificationData = new JSONArray().put(jsonData).toString();

        i.putExtra("messageType", "PEBBLE_ALERT");
        i.putExtra("sender", "PebbleKit Android");
        i.putExtra("notificationData", notificationData);
        sendBroadcast(i);
// Add a key of 0, and a uint8_t (byte) of value 42.
        data.addUint8(0, (byte) 42);

// Add a key of 1, and a string value.
        data.addString(1, "A string");
        PebbleKit.sendDataToPebble(getApplicationContext(),UUID.fromString("5809926e-d1a1-4682-b1af-61db90dd5c37"), data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BeaconManager beaconManager;

    public void BeaconFinder(List<Beacon> list) {
        Beacon i = list.get(0);
    }



    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    public void onConnected(Bundle connectionHint) {
       Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
               mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }




}
