package sitthichai.nudech.map.manheimcar;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListService extends AppCompatActivity {

    // Explicit
    private ListView listView;
    private String[] nameStrings, latStrings, lngStrings, imageStrings;
    private LocationManager locationManager;
    private Criteria criteria;
    private Double latADouble, lngADouble;


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
}   // main
