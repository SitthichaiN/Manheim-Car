package sitthichai.nudech.map.manheimcar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends FragmentActivity implements OnMapReadyCallback {

    // Explicit
    private GoogleMap mMap;
    private String nameString, latString, lngString;
    private Double latADouble, lngADouble;
    private LatLng latLng;
    private Button backButton, qrCodeButton;
    private String modeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //setContentView(R.layout.activity_detail);
        setContentView(R.layout.my_detail_laout);

        // Bind widget
        backButton = (Button) findViewById(R.id.button4);
        qrCodeButton = (Button) findViewById(R.id.button5);

        // Back controller
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // QR code controller
        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setCancelable(false);
                builder.setIcon(R.drawable.doremon48);
                builder.setTitle("Please select mode");
                builder.setMessage("Do you read BarCode or QR Code");
                builder.setNegativeButton("BarCode", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        readCode("BAR_CODE_MODE");
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("QR Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        readCode("QR_CODE_MODE");
                    }
                });
                builder.show();

            }   // On Click
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get Value from Intent
        nameString = getIntent().getStringExtra("Name");
        latString = getIntent().getStringExtra("Lat");
        lngString = getIntent().getStringExtra("Lng");

        Log.d("30octV3", "latString --> " + latString);
        Log.d("30octV3", "lngString --> " + lngString);
    }

    private void readCode(String strMode) {
        Log.d("30octV4", "Mode -->" + strMode);

        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", strMode);
            startActivityForResult(intent,0);
        } catch (Exception e) {
            Toast.makeText(DetailActivity.this, "Please Install Barcode Scanner", Toast.LENGTH_LONG).show();
        }

    }   // readCode


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 0) && (resultCode== RESULT_OK)) {
            String strResult = data.getStringExtra("SCAN_RESULT");
            Log.d("30octV4", "strResult --> " + strResult);

        }   // if
    }   // onActivityResult

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        latADouble = Double.parseDouble(latString);
        lngADouble = Double.parseDouble(lngString);
        latLng = new LatLng(latADouble, lngADouble);

        // Setup map center
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

        // Create Marker
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(nameString)
                .snippet("This is snippet"));

        // Create other maker
        LatLng connerLatLng = new LatLng(13.721060, 100.701844);
        mMap.addMarker(new MarkerOptions()
                .position(connerLatLng)
                .title("TEST")
                .snippet("Turn Left")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_build)));

        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }   // onMapReady
}   // Main
