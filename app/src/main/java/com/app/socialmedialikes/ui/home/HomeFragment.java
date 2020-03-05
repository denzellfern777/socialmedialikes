package com.app.socialmedialikes.ui.home;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.socialmedialikes.LoginActivity;
import com.app.socialmedialikes.R;
import com.app.socialmedialikes.SaveSharedPreference;
import com.app.socialmedialikes.Task;
import com.app.socialmedialikes.TaskActivity;
import com.app.socialmedialikes.TaskAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.socialmedialikes.MainActivity.drawer;
import static com.app.socialmedialikes.MainActivity.getInstance;

public class HomeFragment extends Fragment {

    private String username;
    private ListView listView;
    private Animation anim;
    private ProgressBar circularProgressBar;
    private ArrayList<Task> list;
    private TextView walletBalance;

    public HomeFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SaveSharedPreference.getFirstLaunch(getInstance()).equals("firstLaunch")) {
            updateBalance();
            circularProgressBar.setVisibility(View.VISIBLE);
            fetchTasks();
            SaveSharedPreference.setFirstLaunch(getInstance(), "");
        } else {
            parseItems();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        circularProgressBar = root.findViewById(R.id.circular_progress_bar);
        listView = root.findViewById(R.id.tasks_lv);
        listView.setEmptyView(root.findViewById(R.id.empty));

        walletBalance = root.findViewById(R.id.wallet_balance);

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(750); //You can manage the blinking time with getActivity() parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        if (SaveSharedPreference.getUserName(getInstance().getApplicationContext()).length() == 0) {
            userNotFound();
        } else {

            username = SaveSharedPreference.getUserName(getInstance());
            String walletbalance = SaveSharedPreference.getPrefWalletBalance(getInstance());
            if (TextUtils.isEmpty(walletbalance)) {
                walletbalance = "0";
            }
            walletBalance.setText(walletbalance);
        }

        return root;
    }

    private void updateBalance() {

        walletBalance.startAnimation(anim);

        String url = LoginActivity.url;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?action=wbal&usrnm=" + username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("user_not_found")) {
                            incorrectLoginCredentialsAlertDialog();
                        } else {
                            walletBalance.clearAnimation();
                            startCountAnimation(response);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        new MaterialAlertDialogBuilder(getInstance())
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

        RequestQueue queue = Volley.newRequestQueue(getInstance());
        queue.add(stringRequest);

    }

    private void incorrectLoginCredentialsAlertDialog() {

        new MaterialAlertDialogBuilder(getInstance())
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


    private void startCountAnimation(final String balance) {
        int updatedBalance = Integer.parseInt(balance);
        ValueAnimator animator;
        if (SaveSharedPreference.getPrefWalletBalance(getInstance()).length() == 0) {
            animator = ValueAnimator.ofInt(0, updatedBalance);

        } else {
            animator = ValueAnimator.ofInt(Integer.parseInt(SaveSharedPreference.getPrefWalletBalance(getInstance())), updatedBalance);

        }
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                walletBalance.setText(animation.getAnimatedValue().toString());
                SaveSharedPreference.setPrefWalletBalance(getInstance(), balance);
            }
        });
        animator.start();
    }

    private void userNotFound() {
        new MaterialAlertDialogBuilder(getInstance())
                .setTitle("User not found. You will be signed Out.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveSharedPreference.setPrefWalletBalance(getInstance(), "0");
                        SaveSharedPreference.setUserName(getInstance(), "");
                        SaveSharedPreference.setPrefFullName(getInstance(), "");
                        SaveSharedPreference.setPrefEmail(getInstance(), "");
                        SaveSharedPreference.setPrefPassword(getInstance(), "");
                        SaveSharedPreference.setFirstLaunch(getInstance(), "firstLaunch");
                        Intent i = new Intent(getInstance(), LoginActivity.class);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_refresh) {
            updateBalance();
            fetchTasks();
            circularProgressBar.setVisibility(View.VISIBLE);
        } else if (item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        return true;
    }

    private void fetchTasks() {

        String url = LoginActivity.url;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?action=getTasks&usrnm=" + username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("no_rows")) {
                            SaveSharedPreference.setPrefTasksList(getInstance(), "");
                            parseItems();
                        } else {


                            try {
                                JSONObject jobj = new JSONObject(response);
                                JSONArray jarray = jobj.getJSONArray("items");
                                SaveSharedPreference.setPrefTasksList(getInstance(), jarray.toString());
                                parseItems();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

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

        RequestQueue queue = Volley.newRequestQueue(getInstance());
        queue.add(stringRequest);
    }

    private void parseItems() {

        try {
            list = new ArrayList<>();

            JSONArray jarray = new JSONArray(SaveSharedPreference.getPrefTasksLIst(getInstance()));

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String taskid = jo.getString("taskid");
                String taskname = jo.getString("taskname");
                String taskdesc = jo.getString("taskdesc");
                String tasklink = jo.getString("tasklink");
                String taskreward = jo.getString("taskreward");

                list.add(new Task(taskname, taskdesc, taskreward, tasklink, taskid));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            TaskAdapter customAdapter = new TaskAdapter(list, getInstance());
            listView.setAdapter(customAdapter);
        }

        TaskAdapter customAdapter = new TaskAdapter(list, getInstance());
        listView.setAdapter(customAdapter);
        circularProgressBar.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = list.get(position);
                Intent i = new Intent(getInstance(), TaskActivity.class);
                i.putExtra("taskName", task.getTaskName());
                i.putExtra("taskDesc", task.getTaskDesc());
                i.putExtra("taskReward", task.getTaskReward());
                i.putExtra("taskLink", task.getTaskLink());
                i.putExtra("taskId", task.getTaskId());
                startActivity(i);
            }
        });
    }
}