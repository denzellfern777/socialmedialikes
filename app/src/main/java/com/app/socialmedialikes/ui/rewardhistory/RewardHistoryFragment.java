package com.app.socialmedialikes.ui.rewardhistory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.socialmedialikes.Data;
import com.app.socialmedialikes.DataAdapter;
import com.app.socialmedialikes.LoginActivity;
import com.app.socialmedialikes.R;
import com.app.socialmedialikes.SaveSharedPreference;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.app.socialmedialikes.MainActivity.getInstance;

public class RewardHistoryFragment extends Fragment {

    private ListView listView;
    private String username;
    private MaterialProgressBar materialProgressBar;

    public RewardHistoryFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reward_history, container, false);


        if (SaveSharedPreference.getUserName(getInstance()).length() == 0) {
            userNotFound();
        } else {

            username = SaveSharedPreference.getUserName(getInstance());
        }

        listView = root.findViewById(R.id.items_lv);
        materialProgressBar = root.findViewById(R.id.material_progressbar_horizontal);

        getItems();

        return root;
    }

    private void userNotFound() {
        materialProgressBar.clearAnimation();
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

    private void getItems() {

        materialProgressBar.setVisibility(View.VISIBLE);

        String url = LoginActivity.url;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?action=adLogs&usrnm=" + username,
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

        RequestQueue queue = Volley.newRequestQueue(getInstance());
        queue.add(stringRequest);
    }

    private void parseItems(String jsonResposnce) {

        ArrayList<Data> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");

            int earned = 0;
            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String timestamp = jo.getString("timestamp");
                String uname = jo.getString("uname");
                String points = jo.getString("points");
                String transaction = jo.getString("transaction");
                String approval = jo.getString("approval");

                list.add(new Data(timestamp, uname, points, transaction, approval));


                if (transaction.equals("ad_view")) {
                    earned += Integer.parseInt(points);
                } else if (transaction.equals("task")) {
                    if (approval.equals("approved")) {
                        earned += Integer.parseInt(points);
                    }
                }
            }
            Collections.reverse(list);
            DataAdapter customAdapter = new DataAdapter(getInstance(), list);


            listView.setAdapter(customAdapter);

            String size = String.valueOf(jarray.length());

            View header = getInstance().getLayoutInflater().inflate(R.layout.header_view, listView, false);
            TextView tasksSummary = header.findViewById(R.id.total_tasks_completed_tv);
            TextView totalEarned = header.findViewById(R.id.total_earned_tv);
            totalEarned.setText(String.valueOf(earned));
            tasksSummary.setText(size);
            listView.addHeaderView(header);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        materialProgressBar.setVisibility(View.GONE);
    }


}