package com.ctrlaltelite.copshop.presentation.activities;

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

        Button btnSeller = (Button) findViewById(R.id.register_seller_btn);
        btnSeller.setVisibility(View.INVISIBLE); // Change visibility for button when SellerRegister page exists
        btnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to Register page for Seller when SellerRegister page exists
            }
        });

        Button btnBuyer = (Button) findViewById(R.id.register_buyer_btn);
        btnBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, CreateBuyerAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
