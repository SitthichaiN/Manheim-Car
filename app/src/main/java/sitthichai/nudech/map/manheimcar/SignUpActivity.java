package sitthichai.nudech.map.manheimcar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import static sitthichai.nudech.map.manheimcar.R.id.button;

public class SignUpActivity extends AppCompatActivity {

    // Explicit

    private EditText nameEditText, userEditText, passwordEditText;
    private ImageView imageView;
    private Button button;
    private String nameString, userString, passwordString, imageString, imagePathString, imageNameString;
    private Uri uri;
    private boolean aBoolean = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Bind widget
        nameEditText = (EditText) findViewById(R.id.editText);
        userEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.editText3);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Gey value from edit text
                nameString = nameEditText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                // Check space
                if (nameString.length() == 0 ||
                        userString.length() == 0 ||
                        passwordString.length() == 0) {
                    // Have space
                    MyAlert myAlert = new MyAlert(SignUpActivity.this,
                            R.drawable.bird48, "มีช่องว่าง", "กรุณากรอกทุกช่อง เด้อ!");
                    myAlert.myDialog();
                }  else if (aBoolean) {
                    // Non choose image
                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.doremon48,
                            "No image", "Please select image");
                    myAlert.myDialog();
                } else {
                    // Choose image finished


                }
            }//OnClick
        });

        // Image controller
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // Every picture
                startActivityForResult(Intent.createChooser(intent, "Please select Picture"), 1);
            }
        });

    }   // Method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Log.d("23octV1", "RESULT_OK");
            aBoolean = false;

            // Setup image
            uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            } // Try

            // Find path and name
            imagePathString = myFindPath(uri);
            Log.d("23octV1", "imagePathString -->" + imagePathString);
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d("23octV1","imageNameString --> " + imageNameString);
        } // if

    }  // onActivityResult

    private String myFindPath(Uri uri) {
        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,strings,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            int i = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(i);

        } else {
            result = uri.getPath();
        }
        return result;
    }

}   // Main
