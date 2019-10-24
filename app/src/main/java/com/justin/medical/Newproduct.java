package com.justin.medical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.conn.scheme.HostNameResolver;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Newproduct extends AppCompatActivity implements TaskListner {
    ImageView camera, camera1;
    Button edit;
    Button add, upload;
    private Bitmap bitmap;
    JsonRequester requester;
    String className;
    String TOKEN;
    ProgressDialog loading;
    EditText productname, price, description;
    private int PICK_IMAGE_REQUEST = 1;
    String addProductURL = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/dispensary/addproduct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproduct);
//        edit = (Button) findViewById(R.id.btnedit);

        requester = new JsonRequester(this);
        className = getLocalClassName();

        SharedPreferences prefs = getSharedPreferences("MEDICAL_SHARED_PREFS", MODE_PRIVATE);
        TOKEN = prefs.getString("TOKEN", null);

       /* edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
        camera = (ImageView) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        camera1 = (ImageView) findViewById(R.id.images);
        upload = (Button) findViewById(R.id.btnupload);
        productname = (EditText) findViewById(R.id.productedit);
        price = (EditText) findViewById(R.id.priceedit);
        description = (EditText) findViewById(R.id.descriptionedit);
        add = (Button) findViewById(R.id.btnadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void addProduct() {
        loading = ProgressDialog.show(this, "Adding Product...", "Please wait...", false, false);
        Map<String, String> params = new HashMap<String, String>();
//        params.put("title", "");
        params.put("name", productname.getText().toString());
        params.put("description", description.getText().toString());
        params.put("price", price.getText().toString());
        params.put("quantity", "2");
        params.put("image", getStringImage(bitmap));
        Map<String, String>  paramsHeaders = new HashMap<String, String>();
        paramsHeaders.put("Authorization", TOKEN);
        requester.StringRequesterFormValuesWithHeaders(addProductURL, Request.Method.POST, className, "ADD_NEW_PRODUCT", params, "PRODUCT_LIST_TAG", paramsHeaders);
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {
            if (cd == 00) {

            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("ADD_NEW_PRODUCT")) {
                    loading = ProgressDialog.show(this, "Product Status", "Product added successfully.", false, false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            loading.dismiss();
                            Intent intent = new Intent(Newproduct.this, Product.class);
                            startActivity(intent);
                        }
                    }, 5000);
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                camera1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}



