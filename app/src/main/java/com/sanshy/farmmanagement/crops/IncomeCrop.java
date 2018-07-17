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

public class IncomeCrop extends AppCompatActivity {

    TextView IncomeCropTitle,IncomeCropDateShow,IncomeCropMyIncome;
    EditText IncomeCropAmount,IncomeCropAreaOfLand,IncomeCropRate,IncomeCropWeight;
    AutoCompleteTextView IncomeCropRemark,IncomeCropBuyerName;
    RadioGroup IncomeCropModeOfPayment;
    LinearLayout IncomeCropPartnerContainer;

    LinearLayout IncomeCropPartner1,IncomeCropPartner2,IncomeCropPartner3,IncomeCropPartner4,IncomeCropPartner5;
    TextView IncomeCropPartnerName1,IncomeCropPartnerName2,IncomeCropPartnerName3,IncomeCropPartnerName4,IncomeCropPartnerName5;
    EditText IncomeSharePercentagePartner1,IncomeSharePercentagePartner2,IncomeSharePercentagePartner3,IncomeSharePercentagePartner4,IncomeSharePercentagePartner5;
    EditText IncomeShareAmountPartner1,IncomeShareAmountPartner2,IncomeShareAmountPartner3,IncomeShareAmountPartner4,IncomeShareAmountPartner5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_crop);

        IncomeCropTitle = findViewById(R.id.income_crop_title);
        IncomeCropDateShow = findViewById(R.id.income_crop_show_date);
        IncomeCropMyIncome = findViewById(R.id.income_crop_my_share);

        IncomeCropAmount = findViewById(R.id.income_crop_amount);
        IncomeCropAreaOfLand = findViewById(R.id.income_crop_land_area_of_service);
        IncomeCropRate = findViewById(R.id.income_crop_rate_per_quantity);
        IncomeCropWeight = findViewById(R.id.income_crop_weight);

        IncomeCropRemark = findViewById(R.id.income_crop_remark);
        IncomeCropBuyerName = findViewById(R.id.income_crop_buyer_name);

        IncomeCropModeOfPayment = findViewById(R.id.income_crop_mode_of_payment);

        IncomeCropPartnerContainer = findViewById(R.id.income_crop_partner_container);

        IncomeCropPartner1 = findViewById(R.id.income_crop_partner_1);
        IncomeCropPartner2 = findViewById(R.id.income_crop_partner_2);
        IncomeCropPartner3 = findViewById(R.id.income_crop_partner_3);
        IncomeCropPartner4 = findViewById(R.id.income_crop_partner_4);
        IncomeCropPartner5 = findViewById(R.id.income_crop_partner_5);

        IncomeCropPartnerName1 = findViewById(R.id.income_crop_partner_name_1);
        IncomeCropPartnerName2 = findViewById(R.id.income_crop_partner_name_2);
        IncomeCropPartnerName3 = findViewById(R.id.income_crop_partner_name_3);
        IncomeCropPartnerName4 = findViewById(R.id.income_crop_partner_name_4);
        IncomeCropPartnerName5 = findViewById(R.id.income_crop_partner_name_5);

        IncomeSharePercentagePartner1 = findViewById(R.id.income_crop_partner_share_percentage_1);
        IncomeSharePercentagePartner2 = findViewById(R.id.income_crop_partner_share_percentage_2);
        IncomeSharePercentagePartner3 = findViewById(R.id.income_crop_partner_share_percentage_3);
        IncomeSharePercentagePartner4 = findViewById(R.id.income_crop_partner_share_percentage_4);
        IncomeSharePercentagePartner5 = findViewById(R.id.income_crop_partner_share_percentage_5);

        IncomeShareAmountPartner1 = findViewById(R.id.income_crop_partner_share_amount_1);
        IncomeShareAmountPartner2 = findViewById(R.id.income_crop_partner_share_amount_2);
        IncomeShareAmountPartner3 = findViewById(R.id.income_crop_partner_share_amount_3);
        IncomeShareAmountPartner4 = findViewById(R.id.income_crop_partner_share_amount_4);
        IncomeShareAmountPartner5 = findViewById(R.id.income_crop_partner_share_amount_5);

    }

    public void saveIncomeCropBt(View view){

    }
    public void cancelIncomeCropBt(View view){

    }
    public void dateChooserIncomeCropBt(View view){

    }
}
