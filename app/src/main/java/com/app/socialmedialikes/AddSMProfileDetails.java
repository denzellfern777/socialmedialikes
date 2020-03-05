package com.app.socialmedialikes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import static com.app.socialmedialikes.LoginActivity.url;

public class AddSMProfileDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String[] spinnerItems = {"Instagram", "Facebook", "YouTube", "Zomato", "Google Play Account", "Just Dial", "Discord", "IMDB", "Twitter"};
    private TextInputEditText fullNameAddSm;
    private TextInputEditText usernameAddSm;
    private TextInputEditText emailIdAddSm;
    private TextInputEditText profileLinkAddSm;
    private String selectedSocialProfile;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_smprofile_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fullNameAddSm = findViewById(R.id.full_name_et_add_sm);
        usernameAddSm = findViewById(R.id.username_et_add_sm);
        emailIdAddSm = findViewById(R.id.email_et_add_sm);
        profileLinkAddSm = findViewById(R.id.profile_link_et_add_sm);

        spinner = findViewById(R.id.spinner);
        SpinnerAdapter adapter = new SpinnerAdapter(AddSMProfileDetails.this, android.R.layout.simple_list_item_1);
        adapter.addAll(spinnerItems);
        adapter.add("Please select one");
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        spinner.setOnItemSelectedListener(this);
    }

    private void submitData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    Toast.makeText(AddSMProfileDetails.this, "Social Media Profile added successfully.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                final String fname = fullNameAddSm.getText().toString().trim();
                final String uname = usernameAddSm.getText().toString().trim();
                final String email = emailIdAddSm.getText().toString().trim();
                final String plink = profileLinkAddSm.getText().toString().trim();

                params.put("action", "addSmProfile");
                params.put("uname", uname);
                params.put("fname", fname);
                params.put("email", email);
                params.put("plink", plink);
                params.put("profile", selectedSocialProfile);

                return params;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_sm_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_sm_profile:
                if (!TextUtils.isEmpty(selectedSocialProfile)) {
                    submitData();
                    Toast.makeText(this, "Details Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please select a s ocial media profile from the drop down list", Toast.LENGTH_SHORT).show();
                }


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (spinner.getSelectedItem() != "Please select one") {
            selectedSocialProfile = spinner.getSelectedItem().toString();
        } else {
            selectedSocialProfile = null;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}