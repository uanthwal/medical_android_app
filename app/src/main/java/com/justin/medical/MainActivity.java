package com.justin.medical;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements TaskListner {
    private static final String TAG = MainActivity.class.getSimpleName();
    String password;
    String email;
    private EditText usernameEditText1;
    private EditText passwordEditText1;
    private Button buttonlogin;
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;
    JsonRequester requester;
    String className;
    String loginurl = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/authentication/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref1 = getApplicationContext().getSharedPreferences("Medical", 0); // 0 - for private mode
        editor1 = pref1.edit();

        requester = new JsonRequester(this);
        className = getLocalClassName();

        usernameEditText1 = (EditText) findViewById(R.id.edusername);
        passwordEditText1 = (EditText) findViewById(R.id.edpassword);
        buttonlogin = (Button) findViewById(R.id.btnlogin);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        // final String username =  usernameEditText1.getText().toString().trim();
        email = usernameEditText1.getText().toString().trim();
        password = passwordEditText1.getText().toString().trim();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameEditText1.setError("enter a valid email address");
            // valid = false;
        } else {
            usernameEditText1.setError(null);
        }

       /* if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText1.setError("between 4 and 10 alphanumeric characters");
            //valid = false;
        } else {
            passwordEditText1.setError(null);
        }*/
        getAuthenticationToken();
    }

    public void getAuthenticationToken() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        requester.StringRequesterFormValues(loginurl, Request.Method.POST, className, "LOGIN", params, "LOGIN_TAG");
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {
            if (cd == 00) {

            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("LOGIN")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        SharedPreferences.Editor editor = getSharedPreferences("MEDICAL_SHARED_PREFS", MODE_PRIVATE).edit();
                        editor.putString("TOKEN", "" + jsonObject.getString("token"));
                        editor.commit();
                        Intent intent = new Intent(this, Productlist.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}