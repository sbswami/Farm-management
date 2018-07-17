package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sanshy.farmmanagement.R;

public class MyCrops extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crops);


    }

    public void addCropBt(View view){
        try{
            startActivity(new Intent(this,AddCrop.class));
        }catch(Exception ex){}
    }
}
