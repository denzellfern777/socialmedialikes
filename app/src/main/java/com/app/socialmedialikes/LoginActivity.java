package com.app.socialmedialikes;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String url = "https://script.google.com/macros/s/AKfycbw3IM-c3qpwnv5kTlX3PrzBA3NIDAuca2aW4g8aYkz6Lp0Rrw/exec";
    private EditText login_editTextUsername;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText login_editTextPassword;
    private Button signin;
    private TextView signup;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        login_editTextUsername = findViewById(R.id.username_login);
        login_editTextPassword = findViewById(R.id.password_login);

        usernameLayout = findViewById(R.id.username_wrapper);
        passwordLayout = findViewById(R.id.password_wrapper);
        ConstraintLayout backgoundLoginActivity = findViewById(R.id.background_color_login_activity);

        signin = findViewById(R.id.signin);
        signin.setOnClickListener(this);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == signin) {
            checkFields();
        } else if (v == signup) {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }
    }

    private void checkFields() {
        int goodToGo = 1;

        if (TextUtils.isEmpty(login_editTextUsername.getText().toString())) {
            usernameLayout.setError("Required Field");
            goodToGo = 0;
        } else {
            usernameLayout.setError(null);
        }
        if (TextUtils.isEmpty(login_editTextPassword.getText().toString())) {
            passwordLayout.setError("Required Field");
            goodToGo = 0;
        } else {
            passwordLayout.setError(null);
        }

        if (goodToGo == 1) {
            login();
        }
    }

    private void login() {

        loading = ProgressDialog.show(this, "Sigining You in", "Please wait...");
        final String username = login_editTextUsername.getText().toString().trim();
        final String password = login_editTextPassword.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?action=login&usrnm=" + username + "&pswd=" + password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("404")) {
                            loading.dismiss();
                            incorrectLoginCredentialsAlertDialog();
                        } else if (response.equals("401")) {
                            loading.dismiss();
                            incorrectLoginCredentialsAlertDialog();
                        } else {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            parseItems(response);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();

                        new MaterialAlertDialogBuilder(LoginActivity.this)
                                .setTitle("Your internet connection seems to be down. Please try again")
                                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                    }
                                })
                                .show();
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void parseItems(String jsonResposnce) {

        String fname = null, email = null, pswd = null, uname = null, wbal = null;

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                fname = jo.getString("fname");
                email = jo.getString("email");
                pswd = jo.getString("pswd");
                uname = jo.getString("uname");
                wbal = jo.getString("wbal");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SaveSharedPreference.setUserName(this, uname);
        SaveSharedPreference.setPrefFullName(this, fname);
        SaveSharedPreference.setPrefEmail(this, email);
        SaveSharedPreference.setPrefPassword(this, pswd);
        SaveSharedPreference.setPrefWalletBalance(this, wbal);

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    private void incorrectLoginCredentialsAlertDialog() {

        new MaterialAlertDialogBuilder(LoginActivity.this)
                .setTitle("Invalid Username/Password")
                .setMessage(getString(R.string.invalidcredentials))
                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .show();
    }
}
