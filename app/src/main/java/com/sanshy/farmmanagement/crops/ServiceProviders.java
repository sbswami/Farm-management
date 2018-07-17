package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sanshy.farmmanagement.R;

public class ServiceProviders extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providers);


    }

    public void addServiceProviderBt(View view){
        startActivity(new Intent(this,AddServiceProvider.class));
    }
}
