package com.ctrlaltelite.copshop.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ctrlaltelite.copshop.R;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button btnPrecinct = (Button) findViewById(R.id.btnPrecint);
        btnPrecinct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: go to Register page for Precinct
                //TODO: change visibility for button when PrecinctRegister page exists
            }
        });

        Button btnBidder = (Button) findViewById(R.id.btnBidder);
        btnBidder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, CreateBuyerAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
