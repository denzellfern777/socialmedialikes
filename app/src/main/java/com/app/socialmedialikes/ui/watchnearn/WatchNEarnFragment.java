package com.app.socialmedialikes.ui.watchnearn;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.socialmedialikes.LoginActivity;
import com.app.socialmedialikes.MainActivity;
import com.app.socialmedialikes.R;
import com.app.socialmedialikes.SaveSharedPreference;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.app.socialmedialikes.MainActivity.getInstance;

public class WatchNEarnFragment extends Fragment implements View.OnClickListener {

    private final String url = LoginActivity.url;
    private final String adId_1 = "ca-app-pub-3940256099942544/5224354917";
    private final String adId_2 = "ca-app-pub-3940256099942544/5224354917";
    private final String adId_3 = "ca-app-pub-3940256099942544/5224354917";
    Animation anim;
    private TextView showAd1Text;
    private TextView showAd2Text;
    private TextView showAd3Text;
    private TextView walletBalance;
    private MaterialProgressBar materialProgressBar;
    private RewardedAd rewardedAd1, rewardedAd2, rewardedAd3;
    private CardView showAdBtn1;
    private CardView showAdBtn2;
    private CardView showAdBtn3;
    private String username;

    public WatchNEarnFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_watch_n_earn, container, false);

        showAdBtn1 = root.findViewById(R.id.show_ad_1);
        showAdBtn2 = root.findViewById(R.id.show_ad_2);
        showAdBtn3 = root.findViewById(R.id.show_ad_3);
        showAd2Text = root.findViewById(R.id.show_ad_2_text);
        showAd1Text = root.findViewById(R.id.show_ad_1_text);
        showAd3Text = root.findViewById(R.id.show_ad_3_text);
        walletBalance = root.findViewById(R.id.wallet_balance_watch_n_earn);

        materialProgressBar = root.findViewById(R.id.mp_wallet_balance_watch_n_earn);

        username = SaveSharedPreference.getUserName(MainActivity.getInstance());

        showAdBtn1.setOnClickListener(this);
        showAdBtn2.setOnClickListener(this);
        showAdBtn3.setOnClickListener(this);

        showAdBtn1.setEnabled(false);
        showAd1Text.setBackground(getResources().getDrawable(R.drawable.border_bottom_disabled));

        showAdBtn2.setEnabled(false);
        showAd2Text.setBackground(getResources().getDrawable(R.drawable.border_bottom_disabled));

        showAdBtn3.setEnabled(false);
        showAd3Text.setBackground(getResources().getDrawable(R.drawable.border_bottom_disabled));

        rewardedAd1 = createAndLoadRewardedAd(adId_1, showAdBtn1, showAd1Text);
        rewardedAd2 = createAndLoadRewardedAd(adId_2, showAdBtn2, showAd2Text);
        rewardedAd3 = createAndLoadRewardedAd(adId_3, showAdBtn3, showAd3Text);

        if (SaveSharedPreference.getUserName(getInstance()).length() == 0) {
            userNotFound();
        } else {

            username = SaveSharedPreference.getUserName(getInstance());
            walletBalance.setText(SaveSharedPreference.getPrefWalletBalance(getInstance()));
            String walletbalance = SaveSharedPreference.getPrefWalletBalance(getInstance());
        }

        return root;
    }

    private void userNotFound() {
        materialProgressBar.clearAnimation();
        new MaterialAlertDialogBuilder(getInstance())
                .setTitle("User not found. You will be signed Out.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveSharedPreference.setPrefWalletBalance(MainActivity.getInstance(), "0");
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

    private RewardedAd createAndLoadRewardedAd(String adUnitId, final CardView btn, TextView adBtnText) {
        RewardedAd rewardedAd = new RewardedAd(getInstance(), adUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                btn.setEnabled(true);

                adBtnText.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                switch (errorCode) {
                    case 0:
                        Toast.makeText(getInstance(), "ERROR_CODE_INTERNAL_ERROR", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getInstance(), "ERROR_CODE_INVALID_REQUEST", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getInstance(), "ERROR_CODE_NETWORK_ERROR", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getInstance(), "ERROR_CODE_NO_FILL", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    private void displayAd(RewardedAd rewardedAd, final int adNumber, final CardView adBtn, TextView adBtnText) {

        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    adBtn.setEnabled(false);
                    adBtnText.setBackground(getResources().getDrawable(R.drawable.border_bottom_disabled));
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    switch (adNumber) {
                        case 1:
                            rewardedAd1 = createAndLoadRewardedAd(adId_1, showAdBtn1, showAd1Text);
                            return;
                        case 2:
                            rewardedAd2 = createAndLoadRewardedAd(adId_2, showAdBtn2, showAd2Text);
                            return;
                        case 3:
                            rewardedAd3 = createAndLoadRewardedAd(adId_3, showAdBtn3, showAd3Text);
                    }

                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    switch (adNumber) {
                        case 1:
                            materialProgressBar.setVisibility(View.VISIBLE);
                            post(1);
                            return;
                        case 2:
                            materialProgressBar.setVisibility(View.VISIBLE);
                            post(2);
                            return;
                        case 3:
                            materialProgressBar.setVisibility(View.VISIBLE);
                            post(3);
                    }
                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display.

                }
            };
            rewardedAd.show(getInstance(), adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            Toast.makeText(getInstance(), "The rewarded ad wasn't loaded yet.", Toast.LENGTH_SHORT).show();
        }
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

    private void post(final int adNumber) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        materialProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getInstance(), "10 Points have been added to your wallet", Toast.LENGTH_LONG).show();
                        startCountAnimation(response);
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
                final String adnumber = String.valueOf(adNumber);

                parmas.put("action", "adView");
                parmas.put("uname", uname);
                parmas.put("adnumber", adnumber);
                parmas.put("adreward", "10");
                parmas.put("transaction", "ad_view");

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(getInstance());

        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

        if (v == showAdBtn1) {
            Animation anim = new AlphaAnimation(0.6f, 1.0f);
            anim.setDuration(500);
            v.startAnimation(anim);
            displayAd(rewardedAd1, 1, showAdBtn1, showAd1Text);

        } else if (v == showAdBtn2) {
            Animation anim = new AlphaAnimation(0.6f, 1.0f);
            anim.setDuration(500);
            v.startAnimation(anim);
            displayAd(rewardedAd2, 2, showAdBtn2, showAd2Text);

        } else if (v == showAdBtn3) {
            Animation anim = new AlphaAnimation(0.6f, 1.0f);
            anim.setDuration(500);
            v.startAnimation(anim);
            displayAd(rewardedAd3, 3, showAdBtn3, showAd3Text);
        }
    }
}