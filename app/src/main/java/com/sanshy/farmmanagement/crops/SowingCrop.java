package com.sanshy.farmmanagement.crops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

public class SowingCrop extends AppCompatActivity {

    TextView SowingTitle,SowingDate;
    EditText SowingCropAmount,SowingCropAreaOfLand;
    AutoCompleteTextView SowingCropRemark,SowingCropServiceProviderName,SowingCropPartnerName;
    RadioGroup SowingCropModeOfPayment;
    CheckBox SowingCropPaidByPartner;
    LinearLayout SowingCropPaidPartnerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sowing_crop);

        SowingTitle = findViewById(R.id.sowing_crop_title);
        SowingDate = findViewById(R.id.sowing_show_date);

        SowingCropAmount = findViewById(R.id.sowing_crop_amount);
        SowingCropAreaOfLand = findViewById(R.id.sowing_land_area_of_service);

        SowingCropRemark = findViewById(R.id.sowing_crop_remark);
        SowingCropServiceProviderName = findViewById(R.id.sowing_service_provider_name);
        SowingCropPartnerName = findViewById(R.id.sowing_partner_name);

        SowingCropModeOfPayment = findViewById(R.id.sowing_mode_of_payment);

        SowingCropPaidByPartner = findViewById(R.id.sowing_paid_by_partner);

        SowingCropPaidPartnerContainer = findViewById(R.id.sowing_paid_partner_container);

    }

    public void saveSowingCrop(View view){

    }
    public void cancelSowingCrop(View view){

    }
    public void dateChooserSowing(View view){

    }
}
