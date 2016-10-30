package sitthichai.nudech.map.manheimcar;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class ListService extends AppCompatActivity {

    // Explicit
    private ListView listView;
    private String[] nameStrings, latStrings, lngStrings, imageStrings;
    private LocationManager locationManager;
    private Criteria criteria;
    private Double latADouble, lngADouble;
    private String idLoginUserString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_service);

        // Bind widget - livOfficer
        listView = (ListView) findViewById(R.id.livOfficer);

        // Get value from intent
        nameStrings = getIntent().getStringArrayExtra("Name");
        latStrings = getIntent().getStringArrayExtra("Lat");
        lngStrings = getIntent().getStringArrayExtra("Lng");
        imageStrings = getIntent().getStringArrayExtra("Image");
        idLoginUserString = getIntent().getStringExtra("id");

        // Check Array
        Log.d("24octV3", "Record Count -->" + nameStrings.length);
        for (int i = 0; i < nameStrings.length; i ++) {

            Log.d("24octV3", "nameStrings -->" + nameStrings[i]);
            Log.d("24octV3", "latStrings -->" + latStrings[i]);
            Log.d("24octV3", "lngStrings -->" + lngStrings[i]);
            Log.d("24octV3", "imageStrings -->" + imageStrings[i]);
        }
            //Log.d("24octV3", "latString -->" + latStrings.length);

        // Create ListView
        OfficerAdapter officerAdapter = new OfficerAdapter(ListService.this,
                                                            nameStrings,
                                                            latStrings,
                                                            lngStrings,
                                                            imageStrings);
        listView.setAdapter(officerAdapter);

        // Item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("24octV4", "onItemClick --> Click");
                Intent intent = new Intent(ListService.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        // Setup for Location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false); // Z-cordinate


    }   //main method

    // Manage activities


    @Override
    protected void onResume() {
        super.onResume();

        // Set default location
        locationManager.removeUpdates(locationListener);
        latADouble = 13.719747;
        lngADouble = 100.703239;

        // Find by network..
        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            latADouble = networkLocation.getLatitude();
            lngADouble = networkLocation.getLongitude();
        }

        // Find by GPS card..
        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            latADouble = gpsLocation.getLatitude();
            lngADouble = gpsLocation.getLongitude();
        }

        Log.d("30octV1", "Lat --> " + latADouble);
        Log.d("30octV1", "Lng --> " + lngADouble);

        // Edit location on server..
        EditUserLocation editUserLocation = new EditUserLocation(ListService.this);
        MyConstants myConstants = new MyConstants();
        editUserLocation.execute(myConstants.getUrlEditLocation());

    } // onResume

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);

    }

    public Location myFindLocation(String strProvider) {
        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);
        } else {
            Log.d("30octV1", "Error Cannot Find Location");
        }
        return location;
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // ตรวจสอบ การเปลี่ยนตำแหน่ง
            latADouble = location.getLatitude();
            lngADouble = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            // enable network location..

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }; // locationListener

    private class EditUserLocation extends AsyncTask<String, Void, String> {
        // Explicit..
        private Context context;
        // Create constructor..
        public EditUserLocation(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("id", idLoginUserString)
                        .add("Lat", Double.toString(latADouble))
                        .add("lng", Double.toString(lngADouble))
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("30octV1", "e doin --> " + e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("30octV1", "idUserLogin -->" + idLoginUserString);
            Log.d("30octV1", "Result -->" + s); // True = OK, False = Error
        }


    } // EditUserLocation

}   // main
