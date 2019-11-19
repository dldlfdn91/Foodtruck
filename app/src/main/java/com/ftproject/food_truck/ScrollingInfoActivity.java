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

//    String myJSON;
  //  JSONArray truck =null;

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
                JSONObject jsonObj = new JSONObject(intent.getStringExtra("json")); //인텐트로 한개의 푸드트럭이 가진 정보가 넘어왔다.
                peoples=jsonObj.getJSONArray(TAG_RESULTS); //peoples에 담긴 정보는 한개의 푸드트럭이 가지고 있는 정보가 된다.
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

            /*Picasso.with(this)
                    .load("https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png")
                    .placeholder(R.drawable.ic_launcher);
            */

            /*for(int i=0; i<peoples.length(); i++){
                try {
                    c[i] =peoples.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    list.add(new GridViewItem(c[i].getString(TAG_MENU1),c[i].getString(TAG))
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }*/


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling_info, menu);
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

   /* private void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    //InputStream inputStream = httpURLConnection.getInputStream();
                    //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    String json;

                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");

                    }

                    bufferedReader.close(); //안되면 지울것.
                    httpURLConnection.disconnect(); //안되면 지울것.

                    return sb.toString().trim();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;

                }

            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                myJSON=s;
                showMenu();

            }
        }


        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }*/

   /* private void showMenu() {
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            truck = jsonObj.getJSONArray(TAG_RESULTS);


            String[] menu =new String[truck.length()];
            String[] price=new String[truck.length()];
           // Image[] image=new Image[truck.length()];

            //Marker[] mMarker=new Marker[peoples.length()];

            ArrayList<GridViewItem> list = new ArrayList<>();

            for(int i=0; i<truck.length(); i++){
                JSONObject c =truck.getJSONObject(i);
                //Double latitude =D    ouble.parseDouble(c.getString(TAG_LATITUDE));
                // Double longitude =Double.parseDouble(c.getString(TAG_LONGITUDE));


                menu[i] =c.getString(TAG_MENU1);
                price[i]= c.getString(TAG_PRICE1);
                menu[i] =c.getString(TAG_MENU1);
                price[i]= c.getString(TAG_PRICE1);
                //address[i]=c.getString(TAG_ADDRESS);
                //location[i]=new LatLng(latitude, longitude);



                list.add(new GridViewItem(menu[i], price[i], R.drawable.ic_launcher));


            }

        } catch (JSONException e){
            e.printStackTrace();
        }





    }*/


}
