package com.sanshy.farmmanagement.loans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT_AFTER_LAST_PAYMENT;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.DayInDate;
import static com.sanshy.farmmanagement.MyStatic.GET_MODE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.INTEREST_RATE;
import static com.sanshy.farmmanagement.MyStatic.LAST_PAY_DATE;
import static com.sanshy.farmmanagement.MyStatic.LOAN_COMPLETE;
import static com.sanshy.farmmanagement.MyStatic.LOAN_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_PERSON_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_PERSON_NAME;
import static com.sanshy.farmmanagement.MyStatic.MonthsBetweenDates;
import static com.sanshy.farmmanagement.MyStatic.PAID_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.PER_MONTH_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SIMPLE_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.SIX_MONTHLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.THREE_MONTHLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.YEARLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.staticDate;
import static com.sanshy.farmmanagement.MyStatic.BANK_MODE;

public class CalculateLoan extends AppCompatActivity {

    TextView UntilDate,Interest,TotalAmount,ShowDate,LoanAmountTV;

    boolean GetMode,isPaid;
    String LoanId;
    OneLoanProperties item;
    TextView LastPayDateT,AfterLastPaymentT,PaidAmountT;

    boolean BankMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_loan);

        UntilDate = findViewById(R.id.until_date);
        Interest = findViewById(R.id.interest);
        TotalAmount = findViewById(R.id.total_amount);
        ShowDate = findViewById(R.id.until_show_date);
        LastPayDateT = findViewById(R.id.last_pay_date);
        LoanAmountTV = findViewById(R.id.loan_amount);
        AfterLastPaymentT = findViewById(R.id.after_last_payment);
        PaidAmountT = findViewById(R.id.paid_amount);

        Intent intent = getIntent();
        GetMode = intent.getBooleanExtra(GET_MODE,true);
        isPaid = intent.getBooleanExtra(LOAN_COMPLETE,false);
        LoanId = intent.getStringExtra(LOAN_ID);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isPaid){
            Toast.makeText(this, getString(R.string.this_loan_is_paid), Toast.LENGTH_SHORT).show();
        }

        LoanData();

    }

    private void LoanData() {
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation.document(LoanId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> CloudItemGetLoan = documentSnapshot.getData();
                    item = new OneLoanProperties(
                            CloudItemGetLoan.get(ID).toString(),
                            CloudItemGetLoan.get(REMARK).toString(),
                            (Double) CloudItemGetLoan.get(AMOUNT),
                            (Double) CloudItemGetLoan.get(INTEREST_RATE),
                            (Boolean) CloudItemGetLoan.get(PER_MONTH_INTEREST),
                            (Boolean) CloudItemGetLoan.get(SIMPLE_INTEREST),
                            (Boolean) CloudItemGetLoan.get(YEARLY_COMPOUND),
                            (Boolean) CloudItemGetLoan.get(SIX_MONTHLY_COMPOUND),
                            (Boolean) CloudItemGetLoan.get(THREE_MONTHLY_COMPOUND),
                            (Date) CloudItemGetLoan.get(DATE),
                            (Boolean) CloudItemGetLoan.get(LOAN_COMPLETE),
                            (Date) CloudItemGetLoan.get(LAST_PAY_DATE),
                            (Double) CloudItemGetLoan.get(AMOUNT_AFTER_LAST_PAYMENT),
                            (Double) CloudItemGetLoan.get(PAID_AMOUNT),
                            (Boolean) CloudItemGetLoan.get(BANK_MODE),
                            CloudItemGetLoan.get(LOAN_PERSON_ID).toString(),
                            CloudItemGetLoan.get(LOAN_PERSON_NAME).toString()
                    );
                    BankMode = item.isBankMode();
                    LastPayDateT.setText(DateToString(item.getLastPayDate()));
                    LoanAmountTV.setText(String.valueOf(item.getLoanAmount()));
                    AfterLastPaymentT.setText(String.valueOf(item.getAmountAfterLastPayment()));
                    PaidAmountT.setText(String.valueOf(item.getPaidAmount()));
                    HideProgress();
                    CalculatingLoanAmount = item.getAmountAfterLastPayment();
                    CalIt();

                }
            });



        }else{
            singleGiveLoanLocation.document(LoanId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> CloudItem = documentSnapshot.getData();
                    item = new OneLoanProperties(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(INTEREST_RATE),
                            (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                            (Boolean) CloudItem.get(SIMPLE_INTEREST),
                            (Boolean) CloudItem.get(YEARLY_COMPOUND),
                            (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                            (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                            (Date) CloudItem.get(DATE),
                            (Boolean) CloudItem.get(LOAN_COMPLETE),
                            (Date) CloudItem.get(LAST_PAY_DATE),
                            (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                            (Double) CloudItem.get(MyStatic.PAID_AMOUNT),
                            (Boolean) CloudItem.get(BANK_MODE),
                            CloudItem.get(LOAN_PERSON_ID).toString(),
                            CloudItem.get(LOAN_PERSON_NAME).toString()
                    );
                    BankMode = item.isBankMode();
                    LastPayDateT.setText(DateToString(item.getLastPayDate()));
                    LoanAmountTV.setText(String.valueOf(item.getLoanAmount()));
                    AfterLastPaymentT.setText(String.valueOf(item.getAmountAfterLastPayment()));
                    PaidAmountT.setText(String.valueOf(item.getPaidAmount()));
                    HideProgress();
                    CalculatingLoanAmount = item.getAmountAfterLastPayment();
                    CalIt();
                }
            });
        }
    }



    public void CalculateBt(View view){
        if (!isDate){
            ShowDialog(this,getString(R.string.please_choose_any_date));
            return;
        }
        FinalDate = staticDate;
        CalIt();
    }

    boolean lessMonth = false;

    Date InitialDate;
    Date FinalDate;
    double DiffMonth;
    double DiffDay;
    double interestRate;
    double CompoundInPeriod;
    final double daysInMonth = 30.4375;

    double CalculatingLoanAmount;

    double MainInterest;
    double totalAmount;

    private void CalIt() {

        if (!isDate){
            FinalDate = new Date();

        }else{
            FinalDate = staticDate;
        }

        InitialDate = item.getLastPayDate();
        DiffMonth = MonthsBetweenDates(InitialDate, FinalDate);
        DiffDay = DayInDate(FinalDate)-DayInDate(InitialDate);

        Double temp = Double.valueOf(DiffDay);
        Double temp2 = Double.valueOf(daysInMonth);


        DiffMonth += temp/temp2;

        if (DayInDate(FinalDate)<DayInDate(InitialDate)){
            lessMonth = true;
        }
        interestRate = item.getInterestRate();
        if (!item.isPerMonthInterest()){
            interestRate = interestRate/12;
        }

        if (item.isYearlyCompound()){
            CompoundInPeriod = 12;
        }
        if (item.isSixMonthlyCompound()){
            CompoundInPeriod = 6;
        }
        if (item.isThreeMonthlyCompound()){
            CompoundInPeriod = 3;
        }

        if (item.isSimpleInterest()){
            MainInterest = (interestRate*CalculatingLoanAmount*DiffMonth)/100;
            totalAmount = MainInterest+CalculatingLoanAmount;
        }else{
            if (BankMode){
                totalAmount = CalculatingLoanAmount*Math.pow((1+((interestRate*CompoundInPeriod)/100)),(DiffMonth/CompoundInPeriod));
                MainInterest = totalAmount - CalculatingLoanAmount;
            }else{
                if (DiffMonth<=CompoundInPeriod){
                    MainInterest = (interestRate*CalculatingLoanAmount*DiffMonth)/100;
                    totalAmount = MainInterest+CalculatingLoanAmount;
                }
                else {
                    for(int i = 200; i >= 1; i--){
                        if (DiffMonth>i*CompoundInPeriod){
                            double ExtraMonths = DiffMonth - i*CompoundInPeriod;

                            DiffMonth = i*CompoundInPeriod;
                            double tMainInterest = (interestRate*CalculatingLoanAmount*DiffMonth)/100;
                            double tTotalAmount = tMainInterest+CalculatingLoanAmount;

                            MainInterest = (interestRate*tTotalAmount*ExtraMonths)/100;
                            totalAmount = MainInterest+tTotalAmount;

                            MainInterest = MainInterest +tMainInterest;

                            break;
                        }
                    }
                }
            }

        }


        UntilDate.setText(DateToString(FinalDate));
        Interest.setText(String.valueOf(MainInterest));
        TotalAmount.setText(String.valueOf(totalAmount));

    }

    boolean isDate = false;
    public void chooseDate(View view){
        isDate = ChooseDateDialog(this,ShowDate);
    }

}
