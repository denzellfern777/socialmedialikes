package com.app.socialmedialikes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final String url = LoginActivity.url;
    private ProgressDialog loading;
    private EditText editTextFirstName;
    private EditText editTextEmailAddress;
    private EditText editTextUsername;
    private TextInputLayout nameInputLayout;
    private TextInputLayout emailInputLayout;
    private TextInputLayout usernameInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText editTextPassword;
    private MaterialButton btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        Toolbar toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextFirstName = findViewById(R.id.fname_et);
        editTextEmailAddress = findViewById(R.id.email_et);
        editTextUsername = findViewById(R.id.uname_et);
        editTextPassword = findViewById(R.id.password_et);

        nameInputLayout = findViewById(R.id.name_wrapper_register);
        emailInputLayout = findViewById(R.id.email_wrapper_register);
        usernameInputLayout = findViewById(R.id.username_wrapper_register);
        usernameInputLayout.setHelperText("* Minimum 5 characters required\n* No Spaces Allowed");
        passwordInputLayout = findViewById(R.id.password_wrapper_register);

        btnAddItem = findViewById(R.id.btn_add_item);
        btnAddItem.setOnClickListener(this);
    }


    private void checkFields() {

        int goodToGo = 1;
        if (TextUtils.isEmpty(editTextFirstName.getText().toString())) {
            nameInputLayout.setError("Required Field");
            goodToGo = 0;
        } else {
            nameInputLayout.setError(null);
        }
        if (TextUtils.isEmpty(editTextEmailAddress.getText().toString())) {
            emailInputLayout.setError("Required Field");
            goodToGo = 0;
        } else {
            emailInputLayout.setError(null);
        }
        if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            passwordInputLayout.setError("Required Field");
            goodToGo = 0;
        } else if (editTextPassword.getText().toString().length() < 6) {
            passwordInputLayout.setError("Minimum 6 characters required");
            goodToGo = 0;
        } else {
            passwordInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(editTextUsername.getText().toString())) {
            usernameInputLayout.setError("Required Field");
            goodToGo = 0;
        } else if (editTextUsername.getText().toString().trim().length() < 5) {
            usernameInputLayout.setError("Minimum 5 characters required");
            goodToGo = 0;
        } else if (editTextUsername.getText().toString().contains(" ")) {
            usernameInputLayout.setError("No Spaces Allowed");
            goodToGo = 0;
        } else {
            usernameInputLayout.setError(null);
        }
        if (goodToGo == 1) {
            getItems();
        }

    }

    private void post() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(RegisterActivity.this, "Account created successfully.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                final String fname = editTextFirstName.getText().toString().trim();
                final String email = editTextEmailAddress.getText().toString().trim();
                final String uname = editTextUsername.getText().toString().trim();
                final String pswd = editTextPassword.getText().toString().trim();

                parmas.put("action", "addItem");
                parmas.put("uname", uname);
                parmas.put("fname", fname);
                parmas.put("email", email);
                parmas.put("pswd", pswd);

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    private void getItems() {
        loading = ProgressDialog.show(this, "Signing you up...", "Please wait");
        final String uname = editTextUsername.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?action=searchItems&search=" + uname,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void parseItems(String jsonResponse) {
        int result = Integer.parseInt(jsonResponse);
        if (result == 1) {
            loading.dismiss();
            usernameInputLayout.setError("That username is taken. Try another");
        } else {
            usernameInputLayout.setError(null);
            post();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnAddItem) {
            checkFields();

        }
    }
}
