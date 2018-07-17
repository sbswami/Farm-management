package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sanshy.farmmanagement.R;

public class PartnerCrop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_crop);
    }

    public void addPartnerBt(View view){
        startActivity(new Intent(this,AddPartnerCrop.class));
    }
}
