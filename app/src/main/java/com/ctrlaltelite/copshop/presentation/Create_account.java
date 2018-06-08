package com.ctrlaltelite.copshop.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ctrlaltelite.copshop.R;

public class Create_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button btnPrecinct = findViewById(R.id.btnPrecint);
        btnPrecinct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to Register page for Precinct
            }
        });

        Button btnBidder = findViewById(R.id.btnBidder);
        btnBidder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to Register page for Precinct
            }
        });
    }
}
