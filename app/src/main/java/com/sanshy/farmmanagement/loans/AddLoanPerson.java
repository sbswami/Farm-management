package com.sanshy.farmmanagement.loans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sanshy.farmmanagement.R;

public class AddLoanPerson extends AppCompatActivity {
    
    EditText LoanPersonName, LoanPersonPhoneNumber, LoanPersonAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan_person);
        LoanPersonName = findViewById(R.id.add_partner_name);
        LoanPersonPhoneNumber = findViewById(R.id.add_partner_phone);
        LoanPersonAddress = findViewById(R.id.add_partner_address);

    }

    public void AddLoanPersonBt(View view){

    }
    public void AddLoanPersonCancelBt(View view){
        finish();
    }}
