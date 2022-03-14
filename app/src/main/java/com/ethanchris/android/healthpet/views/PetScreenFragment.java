package com.ethanchris.android.healthpet.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.ethanchris.android.healthpet.R;

public class PetScreenFragment extends Fragment {
    private Button mSettingsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.pet_screen_fragment_fullscreen, container, false);

        mSettingsButton = view.findViewById(R.id.open_settings_button);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                openSettingsActivity(view.getContext());
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void openSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        startActivity(intent);
    }
}
