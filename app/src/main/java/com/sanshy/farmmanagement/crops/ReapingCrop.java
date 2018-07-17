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

public class ReapingCrop extends AppCompatActivity {

    TextView ReapingCropTitle,ReapingCropDateShow;
    EditText ReapingCropAmount,ReapingCropAreaOfLand;
    AutoCompleteTextView ReapingCropRemark,ReapingCropServiceProviderName,ReapingCropPartnerName;
    RadioGroup ReapingCropModeOfPayment;
    CheckBox ReapingCropPaidByPartner;
    LinearLayout ReapingCropPaidPartnerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaping_crop);

        ReapingCropTitle = findViewById(R.id.reaping_crop_title);
        ReapingCropDateShow = findViewById(R.id.reaping_crop_show_date);

        ReapingCropAmount = findViewById(R.id.reaping_crop_amount);
        ReapingCropAreaOfLand = findViewById(R.id.reaping_crop_land_area_of_service);

        ReapingCropRemark = findViewById(R.id.reaping_crop_remark);
        ReapingCropServiceProviderName = findViewById(R.id.reaping_crop_service_provider_name);
        ReapingCropPartnerName = findViewById(R.id.reaping_crop_partner_name);

        ReapingCropModeOfPayment = findViewById(R.id.reaping_crop_mode_of_payment);

        ReapingCropPaidByPartner = findViewById(R.id.reaping_crop_paid_by_partner);

        ReapingCropPaidPartnerContainer = findViewById(R.id.reaping_crop_paid_partner_container);

    }

    public void saveReapingCropBt(View view){

    }
    public void cancelReapingCropBt(View view){

    }
    public void dateChooserReapingCropBt(View view){

    }

}
