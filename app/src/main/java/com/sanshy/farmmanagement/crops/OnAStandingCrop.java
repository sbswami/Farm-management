package com.sanshy.farmmanagement.crops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

public class OnAStandingCrop extends AppCompatActivity {

    TextView OnAStandingCropTitle,OnAStandingCropDateShow;
    EditText OnAStandingCropAmount,OnAStandingCropAreaOfLand;
    AutoCompleteTextView OnAStandingCropRemark,OnAStandingCropServiceProviderName,OnAStandingCropPartnerName;
    RadioGroup OnAStandingCropModeOfPayment;
    CheckBox OnAStandingCropPaidByPartner;
    LinearLayout OnAStandingCropPaidPartnerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_astanding_crop);

        OnAStandingCropTitle = findViewById(R.id.on_a_standing_crop_title);
        OnAStandingCropDateShow = findViewById(R.id.on_a_standing_crop_show_date);

        OnAStandingCropAmount = findViewById(R.id.on_a_standing_crop_amount);
        OnAStandingCropAreaOfLand = findViewById(R.id.on_a_standing_crop_land_area_of_service);

        OnAStandingCropRemark = findViewById(R.id.on_a_standing_crop_remark);
        OnAStandingCropServiceProviderName = findViewById(R.id.on_a_standing_crop_service_provider_name);
        OnAStandingCropPartnerName = findViewById(R.id.on_a_standing_crop_partner_name);

        OnAStandingCropModeOfPayment = findViewById(R.id.on_a_standing_crop_mode_of_payment);

        OnAStandingCropPaidByPartner = findViewById(R.id.on_a_standing_crop_paid_by_partner);

        OnAStandingCropPaidPartnerContainer = findViewById(R.id.on_a_standing_crop_paid_partner_container);

    }

    public void saveOnAStandingCropBt(View view){

    }
    public void cancelOnAStandingCropBt(View view){

    }
    public void dateChooserOnAStandingCropBt(View view){

    }

}
