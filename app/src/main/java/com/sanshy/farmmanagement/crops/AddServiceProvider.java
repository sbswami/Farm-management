package com.sanshy.farmmanagement.crops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.sanshy.farmmanagement.R;

public class AddServiceProvider extends AppCompatActivity {

    EditText ServiceProviderName,ServiceProviderPhoneNumber, ServiceProviderAddress;
    AutoCompleteTextView ServiceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_provider);

        ServiceProviderName = findViewById(R.id.add_service_provider_name);
        ServiceProviderPhoneNumber = findViewById(R.id.add_service_provider_phone);
        ServiceProviderAddress = findViewById(R.id.add_service_provider_address);

        ServiceType = findViewById(R.id.add_service_provider_type);

    }

    public void AddServiceProviderBt(View view){

    }
    public void AddServiceProviderCancleBt(View view){

    }
}
