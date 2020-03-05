package com.app.socialmedialikes.ui.smprofiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.socialmedialikes.AddSMProfileDetails;
import com.app.socialmedialikes.MainActivity;
import com.app.socialmedialikes.R;

public class SMProfilesFragment extends Fragment implements View.OnClickListener {


    private ImageView fab;

    public SMProfilesFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SMProfilesViewModel smProfilesViewModel = ViewModelProviders.of(this).get(SMProfilesViewModel.class);

        View root = inflater.inflate(R.layout.fragment_sm_profiles, container, false);
        smProfilesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            startActivity(new Intent(MainActivity.getInstance(), AddSMProfileDetails.class));
        }
    }
}