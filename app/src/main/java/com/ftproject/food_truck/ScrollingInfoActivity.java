package com.ftproject.food_truck;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ScrollingInfoActivity extends AppCompatActivity {
    private static final String TAG_RESULTS="response";
    private static final String TAG_MENU1 = "menu1";
    private static final String TAG_PRICE1 = "price1";
    private static final String TAG_URL1 = "url1";
    private static final String TAG_MENU2 = "menu2";
    private static final String TAG_PRICE2 = "price2";
    private static final String TAG_URL2 = "url2";
    private static final String TAG_MENU3 = "menu3";
    private static final String TAG_PRICE3 = "price3";
    private static final String TAG_URL3 = "url3";
    private static final String TAG_MENU4 = "menu4";
    private static final String TAG_PRICE4 = "price4";
    private static final String TAG_URL4 = "url4";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_TRUCK="truckname";
    private static final String TAG_ADDRESS="address";

    String myJSON;
    JSONArray peoples =null;
    JSONObject c;
    TextView truck_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_info);

        Log.i("click","OK");
        Intent intent = getIntent();
        if (intent != null) {
            try {
                JSONObject jsonObj = new JSONObject(intent.getStringExtra("json"));
                peoples=jsonObj.getJSONArray(TAG_RESULTS);
                int i=intent.getExtras().getInt("index");
                c =peoples.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ArrayList<GridViewItem> list = new ArrayList<>();
        try {
            list.add(new GridViewItem(c.getString(TAG_MENU1),c.getString(TAG_PRICE1),c.getString(TAG_URL1)));
            list.add(new GridViewItem(c.getString(TAG_MENU2),c.getString(TAG_PRICE2),c.getString(TAG_URL2)));
            list.add(new GridViewItem("무서운 호랑이", "a@naver.com",c.getString(TAG_URL1)));
            list.add(new GridViewItem("안무서운 사자", "b@naver.com",c.getString(TAG_URL2)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GridView grid = (GridView) findViewById(R.id.menugrid);
        GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(), R.layout.gridview, list);
        grid.setAdapter(adapter);

        int a = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        grid.measure(0, a);
        grid.getLayoutParams().height = grid.getMeasuredHeight();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
      
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
