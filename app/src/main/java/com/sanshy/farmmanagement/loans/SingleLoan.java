package com.sanshy.farmmanagement.loans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.LOAN_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_TYPE;

public class SingleLoan extends AppCompatActivity {

    String LoanMode;
    String LoanId;
    TextView Remark,Amount,Date,LoanPerson,InterestRate,InterestRateType,InterestType;

    TextView UntilDate,Interest,TotalAmount,UntilShowDate;

    TextView PayingShowDate,PayingInterest,PayingTotalAmount;
    EditText  PayingAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_loan);

        Intent intent = getIntent();

        LoanMode = intent.getStringExtra(LOAN_TYPE);
        LoanId = intent.getStringExtra(LOAN_ID);

        Remark = findViewById(R.id.remark);
        Amount = findViewById(R.id.amount);
        Date = findViewById(R.id.date);
        LoanPerson = findViewById(R.id.loan_person);
        InterestRate = findViewById(R.id.interest_rate);
        InterestRateType = findViewById(R.id.interest_rate_type);
        InterestType = findViewById(R.id.interest_type);

        UntilDate = findViewById(R.id.until_date);
        Interest = findViewById(R.id.interest);
        TotalAmount = findViewById(R.id.total_amount);

        UntilShowDate = findViewById(R.id.until_show_date);

        PayingShowDate = findViewById(R.id.paying_show_date);
        PayingInterest = findViewById(R.id.paying_interest);
        PayingTotalAmount = findViewById(R.id.paying_total_amount);
        PayingAmount = findViewById(R.id.paying_amount);

    }

    boolean isUntilDate = false;
    public void untilDateChoose(View view){
        isUntilDate = ChooseDateDialog(this,UntilShowDate);
    }
    public void calculateBt(View v){

    }
    boolean isPayDate = false;
    public void payDateChoose(View view){
        isPayDate = ChooseDateDialog(this,PayingShowDate);
    }
    public void payBt(View view){

    }
    public void payHistoryBt(View view){
        Intent intent = new Intent(this,PayHistorySingle.class);

        intent.putExtra(LOAN_ID,LoanId);
        intent.putExtra(LOAN_TYPE,LoanMode);

        startActivity(intent);
    }
}
