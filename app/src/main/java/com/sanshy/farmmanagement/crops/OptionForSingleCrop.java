package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_NAME;
import static com.sanshy.farmmanagement.MyStatic.YEAR_OF_REAPING;
import static com.sanshy.farmmanagement.MyStatic.YEAR_OF_SOWING;

public class OptionForSingleCrop extends AppCompatActivity {

    TextView CropTitle;
    String CropName;
    String CropYearOfSowing;
    String CropYearOfReaping;
    String CropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_for_single_crop);

        CropTitle = findViewById(R.id.single_crop_title);

        try{
            Intent intent = getIntent();
            CropName = intent.getStringExtra(CURRENT_CROP_NAME);
            CropId = intent.getStringExtra(CURRENT_CROP_ID);
            CropYearOfSowing = intent.getStringExtra(YEAR_OF_SOWING);
            CropYearOfReaping = intent.getStringExtra(YEAR_OF_REAPING);
            String Season;
            if (CropYearOfSowing.equals(CropYearOfReaping)){
                Season = "("+CropYearOfReaping+")";
            }else{
                Season = "("+CropYearOfSowing+"-"+CropYearOfReaping+")";
            }
            CropTitle.setText(CropName+Season);
        }catch (Exception ex){}

    }

    public void SowingCropBt(View view){
        Intent intent = new Intent(this, SowingCrop.class);
        intent.putExtra(CURRENT_CROP_ID,CropId);
        startActivity(intent);
    }
    public void OnStandingACropBt(View view){
        Intent intent = new Intent(this, OnAStandingCrop.class);
        intent.putExtra(CURRENT_CROP_ID,CropId);
        startActivity(intent);
    }
    public void ReapingCropBt(View view){
        Intent intent = new Intent(this,ReapingCrop.class);
        intent.putExtra(CURRENT_CROP_ID,CropId);
        startActivity(intent);
    }
    public void IncomeCropBt(View view){
        Intent intent = new Intent(this,IncomeCrop.class);
        intent.putExtra(CURRENT_CROP_ID,CropId);
        startActivity(intent);
    }
    public void ReportCropBt(View view){
        Intent intent = new Intent(this,ReportSingleCrop.class);
        intent.putExtra(CURRENT_CROP_ID,CropId);
        startActivity(intent);
    }

}
