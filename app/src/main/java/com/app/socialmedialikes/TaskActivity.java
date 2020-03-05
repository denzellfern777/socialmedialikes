package com.app.socialmedialikes;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;
import java.util.Map;

import static com.app.socialmedialikes.MainActivity.getInstance;


public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private final String url = LoginActivity.url;
    private String username;
    private String taskId;
    private String taskReward;
    private String taskLink;
    private MaterialButton taskLinkBtn;
    private MaterialButton taskCompletedBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (SaveSharedPreference.getUserName(getInstance()).length() == 0) {
            userNotFound();
        } else {

            username = SaveSharedPreference.getUserName(getInstance());
        }


        TextView taskNameTv = findViewById(R.id.task_name_tv_task_activity);
        TextView taskDescTv = findViewById(R.id.task_desc_tv_task_activity);
        TextView taskRewardTv = findViewById(R.id.task_reward_tv_task_activity);
        taskLinkBtn = findViewById(R.id.task_link_btn_task_activity);
        taskCompletedBtn = findViewById(R.id.task_completed_btn_task_activity);

        String taskName = getIntent().getStringExtra("taskName");
        String taskDesc = getIntent().getStringExtra("taskDesc");
        taskReward = getIntent().getStringExtra("taskReward");
        taskLink = getIntent().getStringExtra("taskLink");
        taskId = getIntent().getStringExtra("taskId");

        taskNameTv.setText(taskName);
        taskDescTv.setText(taskDesc);
        taskRewardTv.setText(taskReward);

        taskLinkBtn.setOnClickListener(this);
        taskCompletedBtn.setOnClickListener(this);

    }

    private void userNotFound() {
        new MaterialAlertDialogBuilder(getInstance())
                .setTitle("User not found. You will be signed Out.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveSharedPreference.setPrefWalletBalance(TaskActivity.this, "0");
                        SaveSharedPreference.setUserName(getInstance(), "");
                        SaveSharedPreference.setPrefFullName(TaskActivity.this, "");
                        SaveSharedPreference.setPrefEmail(TaskActivity.this, "");
                        SaveSharedPreference.setPrefPassword(TaskActivity.this, "");
                        SaveSharedPreference.setFirstLaunch(TaskActivity.this, "firstLaunch");
                        Intent i = new Intent(TaskActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        getInstance().finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == taskLinkBtn) {
            openTaskLink();
        } else if (v == taskCompletedBtn) {
            setTaskCompletedBtn();
        }
    }

    private void setTaskCompletedBtn() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure you have completed the task?")
                .setMessage("Click here only after you have followed all the steps and completed the task. This action cannot be undone. Once you click submit this task will be sent for verification. If the credentials enetered in social profile doesn't match with the account you used to complete the task you won't be rewarded.")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        post();
                    }
                })
                .setNegativeButton(getString(R.string.sign_out_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .show();
    }

    private void openTaskLink() {
        String url = taskLink;
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    taskCompletedBtn.setEnabled(true);
                }
            }, 2000);

        }

    }

    private void post() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SaveSharedPreference.setFirstLaunch(TaskActivity.this, "firstLaunch");
                        onBackPressed();
                        Toast.makeText(TaskActivity.this, taskReward + " points have been added to your wallet but it will reflect in your total balance only after it has been approved.", Toast.LENGTH_LONG).show();
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

                final String uname = username;

                parmas.put("action", "adView");
                parmas.put("uname", uname);
                parmas.put("adnumber", taskId);
                parmas.put("adreward", taskReward);
                parmas.put("transaction", "task");

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(getInstance());

        queue.add(stringRequest);
    }
}
