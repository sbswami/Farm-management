package com.sanshy.farmmanagement.loans;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_PAID;
import static com.sanshy.farmmanagement.MyStatic.GOT_LOAN;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.PAID_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.PAID_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.PAID_LOAN;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.USED;
import static com.sanshy.farmmanagement.MyStatic.loanPersonList;
import static com.sanshy.farmmanagement.MyStatic.loanPersonLocation;
import static com.sanshy.farmmanagement.MyStatic.loanRemarkList;
import static com.sanshy.farmmanagement.MyStatic.postGetLoanReport;
import static com.sanshy.farmmanagement.MyStatic.postGiveLoanReport;
import static com.sanshy.farmmanagement.MyStatic.postLoanAllReport;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanPersonLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGiveLoanPersonLocation;
import static com.sanshy.farmmanagement.MyStatic.staticDate;

public class NewLoan extends AppCompatActivity {


    TextView NewLoanTitle,NewLoanDateShow;
    EditText NewLoanAmount, NewLoanInterestRate;
    AutoCompleteTextView NewLoanRemark, NewLoanPersonName;
    RadioGroup NewLoanInterestType,NewLoanTypeOfInterestRate,NewLoanMode,LoanForm;

    public void loanDone(){
        NewLoanTitle.requestFocus();
        NewLoanAmount.setText("");
        NewLoanInterestRate.setText("");
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loan);

        NewLoanTitle = findViewById(R.id.new_loan_title);
        NewLoanTitle.setFocusableInTouchMode(true);
        NewLoanDateShow = findViewById(R.id.new_loan_show_date);

        NewLoanAmount = findViewById(R.id.new_loan_amount);
        NewLoanInterestRate = findViewById(R.id.new_loan_interest);

        NewLoanRemark = findViewById(R.id.new_loan_remark);
        NewLoanPersonName = findViewById(R.id.new_loan_loan_person_name);

        NewLoanInterestType = findViewById(R.id.new_loan_interest_type);
        NewLoanTypeOfInterestRate = findViewById(R.id.new_loan_type_of_interest_rate);
        NewLoanMode = findViewById(R.id.new_loan_mode);
        LoanForm = findViewById(R.id.new_loan_from);
    }

    ArrayList<String> RemarkList = new ArrayList<>();
    ArrayList<String> LoanPersonList = new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();
        ShowProgress(NewLoan.this);
        loanRemarkList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(NewLoan.this,NewLoanRemark,RemarkList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        loanPersonList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    LoanPersonList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(NewLoan.this,NewLoanPersonName, LoanPersonList);
                }
                HideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    String Id;
    String Remark;
    double LoanAmount;
    double InterestRate;
    Date StartDate;

    String LoanPersonId;
    String LoanPersonName;

    String RemarkSt;
    String AmountSt;
    String LoanPersonSt;
    String InterestRateSt;

    boolean PerMonthInterest;
    boolean BankMode;

    boolean SimpleInterest = false;
    boolean YearlyCompound = false;
    boolean SixMonthlyCompound = false;
    boolean ThreeMonthlyCompound = false;

    boolean GetMode;

    public void makeLoan(View view){

        RemarkSt = NewLoanRemark.getText().toString();
        InterestRateSt = NewLoanInterestRate.getText().toString();
        AmountSt = NewLoanAmount.getText().toString();
        LoanPersonSt = NewLoanPersonName.getText().toString();

        if (RemarkSt.isEmpty()){
            Toast.makeText(this, getString(R.string.fill_information), Toast.LENGTH_SHORT).show();
            NewLoanRemark.setError(getString(R.string.fill_it));
            return;
        }
        if (InterestRateSt.isEmpty()){
            Toast.makeText(this, getString(R.string.fill_information), Toast.LENGTH_SHORT).show();
            NewLoanInterestRate.setError(getString(R.string.fill_it));
            return;
        }
        if (AmountSt.isEmpty()){
            Toast.makeText(this, getString(R.string.fill_information), Toast.LENGTH_SHORT).show();
            NewLoanAmount.setError(getString(R.string.fill_it));
            return;
        }
        if (LoanPersonSt.isEmpty()){
            Toast.makeText(this, getString(R.string.fill_information), Toast.LENGTH_SHORT).show();
            NewLoanPersonName.setError(getString(R.string.fill_it));
            return;
        }
        if (!isDate){
            ShowDialog(this,getString(R.string.please_choose_any_date));
            return;
        }
        if (!LoanPersonList.contains(LoanPersonSt)){
            ShowDialog(this,getString(R.string.first_add_loan_person));
            return;
        }
        if (!(RemarkList.contains(RemarkSt))){
            RemarkList.add(RemarkSt);
            loanRemarkList.setValue(RemarkList);
        }

        Remark = RemarkSt;
        LoanAmount = Double.parseDouble(AmountSt);
        InterestRate = Double.parseDouble(InterestRateSt);
        StartDate = staticDate;

        LoanPersonId = LoanPersonName = LoanPersonSt;

        int RateTypeId = NewLoanTypeOfInterestRate.getCheckedRadioButtonId();
        int InterestTypeId = NewLoanInterestType.getCheckedRadioButtonId();
        int LoanModeId = NewLoanMode.getCheckedRadioButtonId();
        int BankModeId = LoanForm.getCheckedRadioButtonId();

        switch (BankModeId){
            case R.id.new_loan_bank:
                BankMode = true;
                break;
            case R.id.new_loan_village:
                BankMode = false;
                break;
            default:
                BankMode = true;
                break;
        }
        switch (RateTypeId){
            case R.id.new_loan_per_month:
                PerMonthInterest = true;
                break;
            case R.id.new_loan_per_year:
                PerMonthInterest = false;
                break;
            default:
                PerMonthInterest = false;
                break;
        }

        switch (LoanModeId){
            case R.id.new_loan_get:
                GetMode = true;
                break;
            case R.id.new_loan_give:
                GetMode = false;
                break;
            default:
                GetMode = true;
                break;
        }

        switch (InterestTypeId){
            case R.id.new_loan_simple_interest:
                SimpleInterest = true;
                YearlyCompound = false;
                SixMonthlyCompound = false;
                ThreeMonthlyCompound = false;
                break;
            case R.id.new_loan_yearly_compound:
                SimpleInterest = false;
                YearlyCompound = true;
                SixMonthlyCompound = false;
                ThreeMonthlyCompound = false;
                break;
            case R.id.new_loan_six_month_compound:
                SimpleInterest = false;
                YearlyCompound = false;
                SixMonthlyCompound = true;
                ThreeMonthlyCompound = false;
                break;
            case R.id.new_loan_three_month_compound:
                SimpleInterest = false;
                YearlyCompound = false;
                SixMonthlyCompound = false;
                ThreeMonthlyCompound = true;
                break;
            default:
                SimpleInterest = false;
                YearlyCompound = true;
                SixMonthlyCompound = false;
                ThreeMonthlyCompound = false;
                break;
        }

        SaveToCloud();
    }

    private void SaveToCloud() {
        ShowProgress(this);
        Id = Remark + "_"+ TimeStampKey(new Date());
        double paidAmount = 0.0;
        OneLoanProperties item = new OneLoanProperties(
                Id,
                Remark,
                LoanAmount,
                InterestRate,
                PerMonthInterest,
                SimpleInterest,
                YearlyCompound,
                SixMonthlyCompound,
                ThreeMonthlyCompound,
                StartDate,
                false,
                StartDate,
                LoanAmount,
                paidAmount,
                BankMode,
                LoanPersonId,
                LoanPersonName
        );

        final Map<String, Object> CloudItem = new HashMap<>();
        CloudItem.put(ID,item.getId());
        CloudItem.put(REMARK,item.getRemark());
        CloudItem.put(AMOUNT,item.getLoanAmount());
        CloudItem.put(MyStatic.INTEREST_RATE,item.getInterestRate());
        CloudItem.put(MyStatic.PER_MONTH_INTEREST,item.isPerMonthInterest());
        CloudItem.put(MyStatic.SIMPLE_INTEREST,item.isSimpleInterest());
        CloudItem.put(MyStatic.YEARLY_COMPOUND,item.isYearlyCompound());
        CloudItem.put(MyStatic.SIX_MONTHLY_COMPOUND,item.isSixMonthlyCompound());
        CloudItem.put(MyStatic.THREE_MONTHLY_COMPOUND,item.isThreeMonthlyCompound());
        CloudItem.put(DATE,item.getStartDate());
        CloudItem.put(MyStatic.LOAN_COMPLETE,item.isLoanComplete());
        CloudItem.put(MyStatic.LAST_PAY_DATE,item.getLastPayDate());
        CloudItem.put(MyStatic.AMOUNT_AFTER_LAST_PAYMENT,item.getAmountAfterLastPayment());
        CloudItem.put(PAID_AMOUNT,item.getPaidAmount());
        CloudItem.put(MyStatic.BANK_MODE,item.isBankMode());
        CloudItem.put(MyStatic.LOAN_PERSON_ID,item.getLoanPersonId());
        CloudItem.put(MyStatic.LOAN_PERSON_NAME,item.getLoanPersonName());

        if (GetMode){
            singleGetLoanLocation.document(Id).set(CloudItem).addOnCompleteListener(NewLoan.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    HideProgress();
                    if (task.isSuccessful()){
                        postLoanAllReport(ALL,GOT_LOAN,StartDate,LoanAmount,PAID_INTEREST,PAID_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postLoanAllReport(LoanPersonName,GOT_LOAN,StartDate,LoanAmount,PAID_INTEREST,PAID_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postGetLoanReport(Id,ALL,GOT_LOAN,StartDate,LoanAmount,PAID_INTEREST,PAID_LOAN);
                        postGetLoanReport(Id,LoanPersonName,GOT_LOAN,StartDate,LoanAmount,PAID_INTEREST,PAID_LOAN);
                        Map<String, Object> loanPersonUsed = new HashMap<>();
                        loanPersonUsed.put(USED,true);
                        loanPersonLocation.document(LoanPersonName).update(loanPersonUsed);
                        singleGetLoanPersonLocation(LoanPersonName).document(Id).set(CloudItem);
                        ShowDialog(NewLoan.this,getString(R.string.saved));
                        loanDone();
                    }else {
                        ShowDialog(NewLoan.this,getString(R.string.try_again_later_or_send_feedback));
                    }
                }
            });
        }else{
            singleGiveLoanLocation.document(Id).set(CloudItem).addOnCompleteListener(NewLoan.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    HideProgress();
                    if (task.isSuccessful()){
                        postLoanAllReport(ALL,GIVEN_LOAN,StartDate,LoanAmount,PAID_INTEREST,PAID_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postLoanAllReport(LoanPersonName,GIVEN_LOAN,StartDate,LoanAmount,PAID_INTEREST,PAID_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postGiveLoanReport(Id,ALL,GIVEN_LOAN,StartDate,LoanAmount,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postGiveLoanReport(Id,LoanPersonName,GIVEN_LOAN,StartDate,LoanAmount,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        Map<String, Object> loanPersonUsed = new HashMap<>();
                        loanPersonUsed.put(USED,true);
                        loanPersonLocation.document(LoanPersonName).update(loanPersonUsed);
                        singleGiveLoanPersonLocation(LoanPersonName).document(Id).set(CloudItem);
                        ShowDialog(NewLoan.this,getString(R.string.saved));
                        loanDone();
                    }else {
                        ShowDialog(NewLoan.this,getString(R.string.try_again_later_or_send_feedback));
                    }
                }
            });
        }


    }

    public void cancelLoan(View view){
        finish();
    }
    boolean isDate = false;
    public void chooseDate(View view){
        isDate = ChooseDateDialog(this,NewLoanDateShow);
    }



}
