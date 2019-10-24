package com.justin.medical;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
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

public class SignupAcitvity extends Activity implements TaskListner {

    ProgressDialog loading;
    ImageView imgdispensary;
    TextView tv;
    SharedPreferences pref1;
    String typeOfUser;
    SharedPreferences.Editor editor1;
    JsonRequester requester;
    String className;
    int status;
    String token;
    String text, dispensary;
    Button register, login;
    private ProgressDialog pDialog;
    private static final String TAG = SignupAcitvity.class.getSimpleName();
    EditText edfirstname, edlastname, edemail, edpassword, edphone, edaddress;
    String registerurl = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/authentication/register";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRES = "address";
    public static final String KEY_ROLE = "role";
    public static final String KEY_CTYPE = "Content-Type";

    // public static final String KEY_DISPENSARY="dispensary";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_acitvity);
        edfirstname = (EditText) findViewById(R.id.edtxt_firstname);
        edlastname = (EditText) findViewById(R.id.edtxt_lastname);
        edemail = (EditText) findViewById(R.id.edtxt_email);
        edpassword = (EditText) findViewById(R.id.edtxt_password);
        edphone = (EditText) findViewById(R.id.edtxt_phone);
        login = (Button) findViewById(R.id.btnlogin);
        edaddress = (EditText) findViewById(R.id.edaddress);
        register = (Button) findViewById(R.id.btnregister);
        edfirstname.setText("gaurang");
        edlastname.setText("chhatbar");
        edemail.setText("androidtest707@gmail.com");
        edfirstname.setText("gaurang");
        edlastname.setText("chhatbar");
        edpassword.setText("12345");
        edaddress.setText("abc sng");
        edphone.setText("8888888888");
        requester = new JsonRequester(this);
        className = getLocalClassName();

//         typeOfUser =  pref1.getString("userType",null);


        pref1 = getApplicationContext().getSharedPreferences("Medical", 0); // 0 - for private mode
        editor1 = pref1.edit();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // makePostRequest();
                Toast.makeText(SignupAcitvity.this, "Clicked", Toast.LENGTH_LONG).show();

                registerUser();
//                Intent intent = new Intent(SignupAcitvity.this, MainActivity.class);
//                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // makePostRequest();
                Intent intent = new Intent(SignupAcitvity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    public void onSignupSuccess() {
        register.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "User registration failed", Toast.LENGTH_LONG).show();
        register.setEnabled(true);
    }

    private void registerUser() {
        // final String username =  usernameEditText1.getText().toString().trim();
        final String emails = edemail.getText().toString().trim();
        final String passwords = edpassword.getText().toString().trim();
        final String firstn = edfirstname.getText().toString().trim();
        final String lastnm = edlastname.getText().toString().trim();
        final String phoneno = edphone.getText().toString().trim();
        final String add = edaddress.getText().toString().trim();

        //Showing a dialog till we get the route
        loading = ProgressDialog.show(this, "Registering User", "Please wait...", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", "abc@gmail.com");
        params.put("password", "1234");
        params.put("first_name", "abc");
        params.put("last_name", "xyz");
        params.put("phone", "45794646");
        params.put("address", "adha");
        params.put("role", "user");
        requester.StringRequesterFormValues(registerurl, Request.Method.POST, className, "REGISTER", params, "REGISTER_TAG");
    }

    public void onBackPressed() {
        finish();
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {
            if (cd == 00) {
                loading.dismiss();
                onSignupFailed();
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("REGISTER")) {
                    loading.dismiss();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {

        }
    }
}