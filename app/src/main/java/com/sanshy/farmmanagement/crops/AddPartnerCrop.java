package com.sanshy.farmmanagement.crops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sanshy.farmmanagement.R;

public class AddPartnerCrop extends AppCompatActivity {

    EditText PartnerName,PartnerPhoneNumber,PartnerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partner_crop);

        PartnerName = findViewById(R.id.add_partner_name);
        PartnerPhoneNumber = findViewById(R.id.add_partner_phone);
        PartnerAddress = findViewById(R.id.add_partner_address);

    }

    public void AddPartnerBt(View view){

    }
    public void AddPartnerCancelBt(View view){

    }
}
