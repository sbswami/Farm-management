package com.sanshy.farmmanagement.loans;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT_AFTER_LAST_PAYMENT;
import static com.sanshy.farmmanagement.MyStatic.BANK_MODE;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.DayInDate;
import static com.sanshy.farmmanagement.MyStatic.GET_MODE;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_PAID;
import static com.sanshy.farmmanagement.MyStatic.GOT_LOAN;
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
import static com.sanshy.farmmanagement.MyStatic.PAID_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.PAID_LOAN;
import static com.sanshy.farmmanagement.MyStatic.PER_MONTH_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SIMPLE_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.SIX_MONTHLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.THREE_MONTHLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.YEARLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.paidGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.paidGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.postGetLoanReport;
import static com.sanshy.farmmanagement.MyStatic.postGiveLoanReport;
import static com.sanshy.farmmanagement.MyStatic.postLoanAllReport;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanPersonLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.staticDate;

public class SingleLoan extends AppCompatActivity {
    TextView UntilDate,Interest,TotalAmount,ShowDate,LoanAmountTV;

    TextView LastPayDateT,AfterLastPaymentT,PaidAmountT;

    EditText PayingAmount;

    boolean GetMode,isPaid,BankMode;
    String LoanId;
    OneLoanProperties item;

    boolean FirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_loan);

        UntilDate = findViewById(R.id.until_date);
        Interest = findViewById(R.id.interest);
        TotalAmount = findViewById(R.id.total_amount);
        ShowDate = findViewById(R.id.until_show_date);
        LastPayDateT = findViewById(R.id.last_pay_date);
        LoanAmountTV = findViewById(R.id.loan_amount);
        AfterLastPaymentT = findViewById(R.id.after_last_payment);
        PaidAmountT = findViewById(R.id.paid_amount);

        PayingAmount = findViewById(R.id.paying_amount);

        Intent intent = getIntent();
        GetMode = intent.getBooleanExtra(GET_MODE,true);
        isPaid = intent.getBooleanExtra(LOAN_COMPLETE,false);
        LoanId = intent.getStringExtra(LOAN_ID);

    }

    boolean isDate = false;
    public void chooseDate(View view){
        isDate = ChooseDateDialog(this,ShowDate);
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

    String PayingAmountSt;

    public void CalculateItBt(View view){
        if (!isDate){
            ShowDialog(this,getString(R.string.please_choose_any_date));
            return;
        }
        FinalDate = staticDate;
        FirstTime = true;
        CalIt();
    }

    public void payBt(View view){
        PayingAmountSt = PayingAmount.getText().toString();

        if (!isDate){
            ShowDialog(this,getString(R.string.please_choose_any_date));
            return;
        }
        if (PayingAmountSt.isEmpty()){
            PayingAmount.setError(getString(R.string.fill_it));
            Toast.makeText(this, getString(R.string.fill_information), Toast.LENGTH_SHORT).show();
            return;
        }

        FinalDate = staticDate;
        CalIt();

        PayIt();

    }
    double PayAmo;
    String PPLoanPerson;
    private void PayIt() {
        String PayId = TimeStampKey(new Date());

        PPLoanPerson = item.getLoanPersonName();

        PayAmo = Double.parseDouble(PayingAmountSt);

        LoanPayingDataList payItem = new LoanPayingDataList(
                PayId,
                LoanId,
                FinalDate,
                Double.parseDouble(PayingAmountSt),
                MainInterest,
                item.getLoanPersonName()
        );

        Map<String, Object> CloudPayItem = new HashMap<>();
        CloudPayItem.put(ID,payItem.getPayId());
        CloudPayItem.put(LOAN_ID,payItem.getLoanId());
        CloudPayItem.put(DATE,payItem.getDate());
        CloudPayItem.put(AMOUNT,payItem.getAmount());
        CloudPayItem.put(MyStatic.PAYING_INTEREST,payItem.getPayingInterest());
        CloudPayItem.put(LOAN_PERSON_NAME,payItem.getLoanPersonName());

        double totalPay = payItem.getAmount() + item.getPaidAmount();
        double lAmountAfterLastPayment = totalAmount - payItem.getAmount();

        final Map<String, Object> LoanSaveCloud = new HashMap<>();
        LoanSaveCloud.put(LAST_PAY_DATE,FinalDate);
        LoanSaveCloud.put(PAID_AMOUNT,totalPay);
        LoanSaveCloud.put(AMOUNT_AFTER_LAST_PAYMENT,lAmountAfterLastPayment);

        if (Math.round(lAmountAfterLastPayment)==0){
            LoanSaveCloud.put(LOAN_COMPLETE,true);
        }


        ShowProgress(this);
        ExitOrNot = false;
        if (GetMode){
            paidGetLoanLocation(LoanId).document(PayId).set(CloudPayItem).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    HideProgress();
                    if (task.isSuccessful()){
                        postLoanAllReport(ALL,PAID_INTEREST,FinalDate,MainInterest,PAID_LOAN,GOT_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postGetLoanReport(LoanId,ALL,PAID_INTEREST,FinalDate,MainInterest,PAID_LOAN,GOT_LOAN);
                        postLoanAllReport(PPLoanPerson,PAID_INTEREST,FinalDate,MainInterest,PAID_LOAN,GOT_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postGetLoanReport(LoanId,PPLoanPerson,PAID_INTEREST,FinalDate,MainInterest,PAID_LOAN,GOT_LOAN);

                        Thread thread = new Thread(runnable);
                        thread.start();

                        LoanData();
                        PayingAmount.setText("");

                        singleGetLoanPersonLocation(item.getLoanPersonName()).document(LoanId).update(LoanSaveCloud);
                        singleGetLoanLocation.document(LoanId).update(LoanSaveCloud);
                    }
                    else{
                        ShowDialog(SingleLoan.this,getString(R.string.try_again_later_or_send_feedback));
                    }
                }
            });
        }else{
            paidGiveLoanLocation(LoanId).document(PayId).set(CloudPayItem).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    HideProgress();
                    if (task.isSuccessful()){
                        postLoanAllReport(ALL,GIVEN_LOAN_INTEREST,FinalDate,MainInterest,PAID_LOAN,PAID_INTEREST,GOT_LOAN,GIVEN_LOAN,GIVEN_LOAN_PAID);
                        postGiveLoanReport(LoanId,ALL,GIVEN_LOAN_INTEREST,FinalDate,MainInterest,GIVEN_LOAN,GIVEN_LOAN_PAID);
                        postLoanAllReport(PPLoanPerson,GIVEN_LOAN_INTEREST,FinalDate,MainInterest,PAID_LOAN,PAID_INTEREST,GOT_LOAN,GIVEN_LOAN,GIVEN_LOAN_PAID);
                        postGiveLoanReport(LoanId,PPLoanPerson,GIVEN_LOAN_INTEREST,FinalDate,MainInterest,GIVEN_LOAN,GIVEN_LOAN_PAID);

                        Thread thread = new Thread(runnable);
                        thread.start();

                        LoanData();
                        PayingAmount.setText("");

                        singleGetLoanPersonLocation(item.getLoanPersonName()).document(LoanId).update(LoanSaveCloud);
                        singleGetLoanLocation.document(LoanId).update(LoanSaveCloud);
                    }
                    else{
                        ShowDialog(SingleLoan.this,getString(R.string.try_again_later_or_send_feedback));
                    }
                }
            });
        }
    }

    boolean ExitOrNot = true;

    @Override
    public void onBackPressed() {
        if (ExitOrNot){
            finish();
        }
        else{
            Toast.makeText(this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            ExitOrNot = false;

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HideProgress();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (GetMode){
                        postLoanAllReport(ALL,PAID_LOAN,FinalDate,PayAmo,PAID_INTEREST,GOT_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postGetLoanReport(LoanId,ALL,PAID_LOAN,FinalDate,PayAmo,PAID_INTEREST,GOT_LOAN);
                        postLoanAllReport(PPLoanPerson,PAID_LOAN,FinalDate,PayAmo,PAID_INTEREST,GOT_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST,GIVEN_LOAN_PAID);
                        postGetLoanReport(LoanId,PPLoanPerson,PAID_LOAN,FinalDate,PayAmo,PAID_INTEREST,GOT_LOAN);
                        HideProgress();
                        ShowDialog(SingleLoan.this,getString(R.string.saved));
                    }else{
                        postLoanAllReport(ALL,GIVEN_LOAN_PAID,FinalDate,PayAmo,PAID_INTEREST,PAID_LOAN,GOT_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST);
                        postGiveLoanReport(LoanId,ALL,GIVEN_LOAN_PAID,FinalDate,PayAmo,GIVEN_LOAN,GIVEN_LOAN_INTEREST);
                        postLoanAllReport(PPLoanPerson,GIVEN_LOAN_PAID,FinalDate,PayAmo,PAID_INTEREST,PAID_LOAN,GOT_LOAN,GIVEN_LOAN,GIVEN_LOAN_INTEREST);
                        postGiveLoanReport(LoanId,PPLoanPerson,GIVEN_LOAN_PAID,FinalDate,PayAmo,GIVEN_LOAN,GIVEN_LOAN_INTEREST);
                        HideProgress();
                        ShowDialog(SingleLoan.this,getString(R.string.saved));
                    }

                    ExitOrNot = true;
                }
            });
        }
    };

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
        if (FirstTime){
            PayingAmount.setText(String.valueOf(totalAmount));
            FirstTime = false;
        }

    }

    public void payHistoryBt(View view){

        if (ExitOrNot){
            Intent intent = new Intent(this,PayHistorySingle.class);

            intent.putExtra(LOAN_ID,LoanId);
            intent.putExtra(GET_MODE,GetMode);
            intent.putExtra(LOAN_COMPLETE,isPaid);

            startActivity(intent);
        }
        else{
            Toast.makeText(this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
        }

    }
}
