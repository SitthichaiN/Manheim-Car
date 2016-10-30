package sitthichai.nudech.map.manheimcar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    /*
        Explicit {Access Type Name}
    */

    private Button signInButton, signUpButton;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;
    private String[] nameStrings, imageStrings, latStrings, lngStrings;
    private boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Widget
        signInButton = (Button) findViewById(R.id.button);
        signUpButton = (Button) findViewById(R.id.button2);
        userEditText = (EditText) findViewById(R.id.editText4);
        passwordEditText = (EditText) findViewById(R.id.editText5);


        // SignUp Controller
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent = move from page to other page.
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        // Sign controller
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get value from signin
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                // Check space
                if (userString.length() == 0 || passwordString.length() == 0 ) {
                    MyAlert myAlert = new MyAlert(MainActivity.this, R.drawable.nobita48,
                            "Have space", "Please fill value");
                    myAlert.myDialog();
                } else {

                    // NoSpace
                    MyConstants myConstants = new MyConstants();
                    SynData synData = new SynData(MainActivity.this);
                    synData.execute(myConstants.getUrlJSONString(),
                            myConstants.getTestTitleString(),
                            myConstants.getTestMessageString());
                }

            }   // OnClick
        });

    }   // Main Method

    // Get data from json service...
    private class SynData extends AsyncTask<String, Void, String> {

        // Explicit
        private Context context; // Alt+Insert
        private String titleString, messageString, truePasswordString, idString;

        public SynData(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // overide on post
            try {
                titleString = params[1];
                messageString = params[2];

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();    // *****

            } catch (Exception e) {
                Log.d("24octV1", "doInBackground --> " + e.toString());
                return null;
            }
        }   // doInBackground

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("24octV1", "JSON --> " + s);

            try {
                JSONArray jsonArray = new JSONArray(s);
                nameStrings = new String[jsonArray.length()];
                imageStrings = new String[jsonArray.length()];
                latStrings= new String[jsonArray.length()];
                lngStrings = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i ++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    // Check user
                    if (userString.equals(jsonObject.getString("User"))) {
                        aBoolean = false;
                        truePasswordString = jsonObject.getString("Password");
                        idString = jsonObject.getString("id");
                    }   // if

                    // set up array
                    nameStrings[i] = jsonObject.getString("Name");
                    imageStrings[i] = jsonObject.getString("Image");
                    latStrings[i] = jsonObject.getString("Lat");
                    lngStrings[i] = jsonObject.getString("Lng");

                    Log.d("24octV5", "getString:Name -->" + nameStrings[i]);
                    Log.d("24octV5", "getString:Image -->" + imageStrings[i]);

                }   // for

                Log.d("24octV5", "Boolean -->" + aBoolean);
                if (aBoolean) {
                    MyAlert myAlert = new MyAlert(context, R.drawable.bird48, titleString, messageString);
                    myAlert.myDialog();

                } else if (passwordString.equals(truePasswordString)) {

                    // Password True
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ListService.class);

                    // Push data ListService
                    //Log.d("24octV5", "Name -->" + nameStrings);
                    intent.putExtra("id", idString);            // For where edit location..
                    intent.putExtra("Name", nameStrings);       // For create ListView..
                    intent.putExtra("Image", imageStrings);     // For create ListView..
                    intent.putExtra("Lat", latStrings);         // For create ListView..
                    intent.putExtra("Lng", lngStrings);         // For create ListView..

                    startActivity(intent);
                    finish();
                } else {
                    // Password False
                    MyAlert myAlert = new MyAlert(context, R.drawable.doremon48, "Password False", "Please try again");
                    myAlert.myDialog();
                }
            } catch (Exception e) {
                Log.d("24octV2", "e OnPost --> " + e.toString());
            }
        }
    }   // SynData class
}   // Main class

