package com.sanshy.farmmanagement.crops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

public class AddCrop extends AppCompatActivity {

    EditText CropName,YearOfSowing,YearOfReaping,AreaOfLand;
    AutoCompleteTextView CropType;

    CheckBox PartnerExistenceChecker;

    EditText SharingPercentagePartner1,SharingPercentagePartner2,SharingPercentagePartner3,SharingPercentagePartner4,SharingPercentagePartner5;
    AutoCompleteTextView PartnerName1, PartnerName2, PartnerName3, PartnerName4, PartnerName5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crop);

        try{
            CropName = findViewById(R.id.add_crop_name);
            YearOfSowing = findViewById(R.id.add_crop_year_of_sowing);
            YearOfReaping = findViewById(R.id.add_crop_year_of_reaping);
            AreaOfLand = findViewById(R.id.add_crop_land_area);

            PartnerExistenceChecker = findViewById(R.id.add_crop_partner_exits_chooser);

            CropType = findViewById(R.id.add_crop_type);


            SharingPercentagePartner1 = findViewById(R.id.add_crop_partner_percentage1);
            SharingPercentagePartner2 = findViewById(R.id.add_crop_partner_percentage2);
            SharingPercentagePartner3 = findViewById(R.id.add_crop_partner_percentage3);
            SharingPercentagePartner4 = findViewById(R.id.add_crop_partner_percentage4);
            SharingPercentagePartner5 = findViewById(R.id.add_crop_partner_percentage5);

            PartnerName1 = findViewById(R.id.add_crop_partner_name1);
            PartnerName2 = findViewById(R.id.add_crop_partner_name2);
            PartnerName3 = findViewById(R.id.add_crop_partner_name3);
            PartnerName4 = findViewById(R.id.add_crop_partner_name4);
            PartnerName5 = findViewById(R.id.add_crop_partner_name5);
        }catch (Exception ex){
            MyStatic.ShowDialog(this,ex.toString());
        }

    }

    public void AddCropBt(View view){

    }
    public void AddCropCancelBt(View view){

    }

}
