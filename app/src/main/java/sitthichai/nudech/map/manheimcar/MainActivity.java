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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity {

    /*
        Explicit {Access Type Name}
    */

    private Button signInButton, signUpButton;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;


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
        private String titleString, messageString;

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
        }
    }   // SynData class
}   // Main class

