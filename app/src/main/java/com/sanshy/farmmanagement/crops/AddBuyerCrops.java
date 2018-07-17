package com.sanshy.farmmanagement.crops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sanshy.farmmanagement.R;

public class AddBuyerCrops extends AppCompatActivity {

    EditText BuyerCropsName,BuyerCropsPhoneNumber, BuyerCropsAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buyer_crops);

        BuyerCropsName = findViewById(R.id.add_buyer_crops_name);
        BuyerCropsPhoneNumber = findViewById(R.id.add_buyer_crops_phone);
        BuyerCropsAddress = findViewById(R.id.add_buyer_crops_address);

    }

    public void AddBuyerCropsBt(View view){

    }
    public void AddBuyerCropsCancleBt(View view){

    }
}
