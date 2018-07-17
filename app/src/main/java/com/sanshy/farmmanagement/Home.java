package com.sanshy.farmmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanshy.farmmanagement.crops.AllCropsReport;
import com.sanshy.farmmanagement.crops.BuyerCrops;
import com.sanshy.farmmanagement.crops.MyCrops;
import com.sanshy.farmmanagement.crops.PartnerCrop;
import com.sanshy.farmmanagement.crops.ServiceProviders;

public class Home extends AppCompatActivity {

    LinearLayout cropContainer,loanContainer,extraContainer;

    LinearLayout myCrops,serviceProvider,partnerCrops,allCropsReport;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_crop:
                    cropContainer.setVisibility(View.VISIBLE);
                    loanContainer.setVisibility(View.GONE);
                    extraContainer.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_loan:
                    loanContainer.setVisibility(View.VISIBLE);
                    cropContainer.setVisibility(View.GONE);
                    extraContainer.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_extra:
                    extraContainer.setVisibility(View.VISIBLE);
                    cropContainer.setVisibility(View.GONE);
                    loanContainer.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cropContainer = findViewById(R.id.crop_container);
        loanContainer = findViewById(R.id.loan_container);
        extraContainer = findViewById(R.id.extra_container);

        myCrops = findViewById(R.id.my_crops);
        serviceProvider = findViewById(R.id.service_provider);
        partnerCrops = findViewById(R.id.partner_crop);
        allCropsReport = findViewById(R.id.all_crops_report);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void myCropsBt(View view){
        startActivity(new Intent(this,MyCrops.class));
    }
    public void serviceProviderBt(View view){
        startActivity(new Intent(this,ServiceProviders.class));
    }
    public void partnerCropsBt(View view){
        startActivity(new Intent(this, PartnerCrop.class));
    }
    public void buyerCropBt(View view){
        startActivity(new Intent(this, BuyerCrops.class));
    }
    public void allCropsReportBt(View view){
        startActivity(new Intent(this, AllCropsReport.class));
    }


}
