package sitthichai.nudech.map.manheimcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                }

            }   // OnClick
        });

    }   // Main Method
}   // Main class
