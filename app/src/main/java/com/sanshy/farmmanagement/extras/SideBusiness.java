package com.sanshy.farmmanagement.extras;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.extras.side_business.SideExpenditure;
import com.sanshy.farmmanagement.extras.side_business.SideIncome;
import com.sanshy.farmmanagement.extras.side_business.SideReport;

public class SideBusiness extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_business);
    }
    public void SideExpenditureBt(View view){
        startActivity(new Intent(this, SideExpenditure.class));
    }
    public void SideIncomeBt(View view){
        startActivity(new Intent(this, SideIncome.class));
    }
    public void SideReportBt(View view){
        startActivity(new Intent(this, SideReport.class));
    }

}
