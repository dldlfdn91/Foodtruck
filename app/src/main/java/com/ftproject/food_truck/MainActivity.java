package com.ftproject.food_truck;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
/*
 * 메인액티비티 맨 처음 화면은 GoogleMap을 이용하여 자기위치를 나타낸다.
 * 자기 위치 주변의 푸드트럭들을 마커로 표시해준다.
 * 방향 센서를 이용해 나침반을 화면에 표시한다.
 *
 */

public class MainActivity extends AppCompatActivity implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback
{
    private static final String TAG = "MainActivity";

    SupportMapFragment mapFragment;
    GoogleMap map;
    Marker[] mMarker=new Marker[100];


    MarkerOptions myLocationMarker;


    private SensorManager mSensorManager;
    private CompassView mCompassView;
    private boolean mCompassEnabled;

    String myJSON;
    JSONArray peoples =null;

    //아래와 같은 태그들로 구분해준다.
    private static final String TAG_RESULTS="response";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_TRUCK="truckname";
    private static final String TAG_ADDRESS="address";
    private JSONObject jsonObj;
    public static final String localhost="192.168.0.102";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button button = (Button) findViewById(R.id.button);//주변 푸드트럭
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMyLocation();
                getData("http://"+localhost+"/newfile99.php");
            }
        });


        // 센서 관리자 객체 참조
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        // 나침반을 표시할 뷰 생성
        boolean sideBottom = true;
        mCompassView = new CompassView(this);
        mCompassView.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(sideBottom ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.ALIGN_PARENT_TOP);

        ((ViewGroup)mapFragment.getView()).addView(mCompassView, params);


        mCompassEnabled = true;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //구글 맵이 준비 될때

        Log.d(TAG, "GoogleMap is ready.");

        map = googleMap;
        map.setMyLocationEnabled(true); //내 위치 표시 활성화.

        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Intent Call_Info=new Intent(getApplicationContext(), ScrollingInfoActivity.class);
        Call_Info.putExtra("json",jsonObj.toString());
        Call_Info.putExtra("index", Arrays.asList(mMarker).indexOf(marker));



        startActivity(Call_Info);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).

    }



    private void getData(String url) {
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
                showGPS();

            }
        }


        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }

    public void showGPS() { ///protected?
        try{
            jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);


            LatLng[] trucklatlng =new LatLng[peoples.length()];
            String[] truckname=new String[peoples.length()];
            String[] address=new String[peoples.length()];

            //Marker[] mMarker=new Marker[peoples.length()];

            for(int i=0; i<peoples.length(); i++){
                JSONObject c =peoples.getJSONObject(i);
                //Double latitude =D    ouble.parseDouble(c.getString(TAG_LATITUDE));
                // Double longitude =Double.parseDouble(c.getString(TAG_LONGITUDE));


                trucklatlng[i] =new LatLng(Double.parseDouble(c.getString(TAG_LATITUDE)),Double.parseDouble(c.getString(TAG_LONGITUDE)));
                truckname[i]= c.getString(TAG_TRUCK);
                address[i]=c.getString(TAG_ADDRESS);
                //location[i]=new LatLng(latitude, longitude);



                if(mMarker[i]!=null)
                    mMarker[i].remove();


                //  String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
                // Log.i("GPSListener", msg);
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                mMarker[i]= map.addMarker(new MarkerOptions()
                        .position(trucklatlng[i])
                        .title(truckname[i])
                        .snippet("*주소:"+address[i])
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation)));
                /*if (mMarker[i] == null) {
                    mMarker[i] = new MarkerOptions();
                    mMarker[i].position(location[i]);
                    mMarker[i].title("현재 위치\n");
                    mMarker[i].icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));

                    map.addMarker(myLocationMarker);
*/



            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }




    private void requestMyLocation() {
        // 내위치정보에대해 요청하는 메소드.

        // GPS 모듈 -> LocationManager에서 위치정보를 넘겨줌. -> LocationListener -> 액티비티에 디스플레이
        //                          ^----<-------------------------<-----------------------!
        //                                      (위치정보 요청)

        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);//Location manager 객체. 클래스를 직접 initiate 해줄 필요 없이 ; instead, retrieve it through Context.getSystemService(Context.LOCATION_SERVICE).
        // manager객체를 통해 스마트폰에 설치되있는 gps모듈로부터 gps정보를 가져온다. 그후 위치정보를 위치리스너(Location Listener)로 넘긴다음에 액티비티에 디스플레이할것임.

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //lastLocation  Location객체 선언. Locationmanager를 통해 예전에 알려져 있던 위치를 가지고 있는 객체이다.
            //LocationManager를 통해 위치정보를 입력받음.
            if(lastLocation != null){
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void showCurrentLocation(Location location) {

        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 35));  //줌 조정할 것.

        showMyLocationMarker(location);
    }

    private void showMyLocationMarker(Location location) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            myLocationMarker.title("현재 위치\n");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));

            map.addMarker(myLocationMarker);
        } else {
            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (map != null) {
            map.setMyLocationEnabled(false);
        }
        if(mCompassEnabled) {
            mSensorManager.unregisterListener(mListener);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (map != null) {
            map.setMyLocationEnabled(true);
        }

        if(mCompassEnabled) {
            mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * 센서의 정보를 받기 위한 리스너 객체 생성
     */
    private final SensorEventListener mListener = new SensorEventListener() {
        private int iOrientation = -1;

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        // 센서의 값을 받을 수 있도록 호출되는 메소드
        public void onSensorChanged(SensorEvent event) {
            if (iOrientation < 0) {
                iOrientation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            }

            mCompassView.setAzimuth(event.values[0] + 90 * iOrientation);
            mCompassView.invalidate();

        }

    };


}



