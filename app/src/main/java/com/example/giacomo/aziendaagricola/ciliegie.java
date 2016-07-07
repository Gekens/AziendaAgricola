package com.example.giacomo.aziendaagricola;

import android.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ciliegie extends Fragment {
    ImageView primegiant, bigglory, regina, duroni;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ciliegie, container, false);
        primegiant = (ImageView) view.findViewById(R.id.imageViewPrimegiant);

        primegiant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(getActivity(), primegiant.class);
                startActivity(vIntent);
            }
        });

        return view;
    }
}
