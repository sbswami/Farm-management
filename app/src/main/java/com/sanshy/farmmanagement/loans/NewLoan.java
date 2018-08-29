package com.sanshy.farmmanagement.loans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sanshy.farmmanagement.R;

import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;

public class NewLoan extends AppCompatActivity {

    TextView NewLoanTitle,NewLoanDateShow;
    EditText NewLoanAmount, NewLoanInterestRate;
    AutoCompleteTextView NewLoanRemark, NewLoanPersonName;
    RadioGroup NewLoanInterestType,NewLoanTypeOfInterestRate,NewLoanMode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loan);

        NewLoanTitle = findViewById(R.id.new_loan_title);
        NewLoanDateShow = findViewById(R.id.new_loan_show_date);

        NewLoanAmount = findViewById(R.id.new_loan_amount);
        NewLoanInterestRate = findViewById(R.id.new_loan_interest);

        NewLoanRemark = findViewById(R.id.new_loan_remark);
        NewLoanPersonName = findViewById(R.id.new_loan_loan_person_name);

        NewLoanInterestType = findViewById(R.id.new_loan_interest_type);
        NewLoanTypeOfInterestRate = findViewById(R.id.new_loan_type_of_interest_rate);
        NewLoanMode = findViewById(R.id.new_loan_mode);
    }

    public void makeLoan(View view){

    }
    public void cancelLoan(View view){
        finish();
    }
    boolean isDateChoosen = false;
    public void chooseDate(View view){
        isDateChoosen = ChooseDateDialog(this,NewLoanDateShow);
    }
}
