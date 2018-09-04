package com.sanshy.farmmanagement.loans;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.crops.ReportSingleCrop;

import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT_AFTER_LAST_PAYMENT;
import static com.sanshy.farmmanagement.MyStatic.BANK_MODE;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
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
import static com.sanshy.farmmanagement.MyStatic.PAID_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.PAID_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.PAID_LOAN;
import static com.sanshy.farmmanagement.MyStatic.PAYING_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.PER_MONTH_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SIMPLE_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.SIX_MONTHLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.THREE_MONTHLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.YEARLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.lessLoanAllReport;
import static com.sanshy.farmmanagement.MyStatic.paidGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.paidGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.reportGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.reportGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanPersonLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGiveLoanPersonLocation;

public class OptionForSingleLoan extends AppCompatActivity {

    boolean GetMode;
    String LoanId;
    boolean isPaid;
    
    TextView LoanTitle;
    
    LinearLayout payLoan;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_for_single_loan);

        Intent intent = getIntent();
        GetMode = intent.getBooleanExtra(GET_MODE,true);
        LoanId = intent.getStringExtra(LOAN_ID);
        isPaid = intent.getBooleanExtra(LOAN_COMPLETE,false);
        
        payLoan = findViewById(R.id.pay_loan);

        if (isPaid){
            payLoan.setVisibility(View.GONE);
            ShowDialog(this,getString(R.string.this_loan_is_paid));
        }

        LoanTitle = findViewById(R.id.single_loan_title); 
        
        int IndexS = LoanId.indexOf("_");
        LoanTitle.setText(LoanId.subSequence(0,IndexS));

        if (isPaid){
            LoanTitle.setText(LoanId.subSequence(0,IndexS)+" "+getString(R.string.this_loan_is_paid));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isPaid){
            payLoan.setVisibility(View.GONE);
        }
        
    }

    public void calculateBt(View view){
        Intent intent1 = new Intent(OptionForSingleLoan.this,CalculateLoan.class);
        intent1.putExtra(LOAN_ID,LoanId);
        intent1.putExtra(GET_MODE,GetMode);
        intent1.putExtra(LOAN_COMPLETE,isPaid);
        startActivity(intent1);
    }

    public void reportBt(View view){
        Intent intent1 = new Intent(OptionForSingleLoan.this,ReportSingleLoan.class);
        intent1.putExtra(LOAN_ID,LoanId);
        intent1.putExtra(GET_MODE,GetMode);
        intent1.putExtra(LOAN_COMPLETE,isPaid);
        startActivity(intent1);
    }

    public void payLoanBt(View view){
        Intent intent1 = new Intent(OptionForSingleLoan.this,SingleLoan.class);
        intent1.putExtra(LOAN_ID,LoanId);
        intent1.putExtra(GET_MODE,GetMode);
        intent1.putExtra(LOAN_COMPLETE,isPaid);
        startActivity(intent1);
    }

    public void payHistoryBt(View view){
        Intent intent1 = new Intent(OptionForSingleLoan.this,PayHistorySingle.class);
        intent1.putExtra(LOAN_ID,LoanId);
        intent1.putExtra(GET_MODE,GetMode);
        intent1.putExtra(LOAN_COMPLETE,isPaid);
        startActivity(intent1);
    }

    public void loanDetailBt(View view){
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation.document(LoanId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> CloudItem = documentSnapshot.getData();
                    OneLoanProperties item = new OneLoanProperties(
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
                            (Double) CloudItem.get(PAID_AMOUNT),
                            (Boolean) CloudItem.get(BANK_MODE),
                            CloudItem.get(LOAN_PERSON_ID).toString(),
                            CloudItem.get(LOAN_PERSON_NAME).toString()
                    );
                    HideProgress();
                    loadLoanDetail(item);
                }
            });
        }else{
            singleGiveLoanLocation.document(LoanId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> CloudItem = documentSnapshot.getData();
                    OneLoanProperties item = new OneLoanProperties(
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
                            (Double) CloudItem.get(PAID_AMOUNT),
                            (Boolean) CloudItem.get(BANK_MODE),
                            CloudItem.get(LOAN_PERSON_ID).toString(),
                            CloudItem.get(LOAN_PERSON_NAME).toString()
                    );
                    HideProgress();
                    loadLoanDetail(item);
                }
            });
        }
        
        
        
    }

    public void loadLoanDetail(OneLoanProperties myData){
        AlertDialog.Builder builder = new AlertDialog.Builder(OptionForSingleLoan.this);

        final String ThisLoanId = myData.getId();
        final String ThisLoanPerson = myData.getLoanPersonName();
        final Date ThisLoanDate = myData.getStartDate();
        final double ThisLoanAmount = myData.getLoanAmount();
        final boolean ThisisPaid = myData.isLoanComplete();


        LayoutInflater layoutInflater = (LayoutInflater) OptionForSingleLoan.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_show_loan_detail,null);


        TextView Remark = customView.findViewById(R.id.remark_dialog);
        TextView Amount = customView.findViewById(R.id.amount_dialog);
        TextView Date = customView.findViewById(R.id.date_dialog);
        TextView LoanPerson = customView.findViewById(R.id.loan_person_dialog);
        TextView InterestRate = customView.findViewById(R.id.interest_rate_dialog);
        TextView InterestRateType = customView.findViewById(R.id.interest_rate_type_dialog);
        TextView InterestType = customView.findViewById(R.id.interest_type_dialog);
        TextView PaidAmount = customView.findViewById(R.id.paid_amount);
        TextView AfterLastPayment = customView.findViewById(R.id.after_last_payment);
        TextView LastPayDate = customView.findViewById(R.id.last_pay_date);
        TextView InterestCountingWay = customView.findViewById(R.id.interest_counting_way);

        Remark.setText(myData.getRemark());
        Amount.setText(String.valueOf(myData.getLoanAmount()));
        Date.setText(DateToString(myData.getStartDate()));
        LoanPerson.setText(myData.getLoanPersonName());
        InterestRate.setText(String.valueOf(myData.getInterestRate()));
        PaidAmount.setText(String.valueOf(myData.getPaidAmount()));
        AfterLastPayment.setText(String.valueOf(myData.getAmountAfterLastPayment()));
        LastPayDate.setText(DateToString(myData.getLastPayDate()));
        if (myData.isBankMode()){
            InterestCountingWay.setText(getString(R.string.banking));
        }else{
            InterestCountingWay.setText(getString(R.string.traditional));
        }
        String InterestRateTypeString;
        if (myData.isPerMonthInterest()){
            InterestRateTypeString = getString(R.string.per_month);
        }else {
            InterestRateTypeString = getString(R.string.per_year);
        }
        InterestRateType.setText(InterestRateTypeString);
        String InterestTypeString;
        if (myData.isYearlyCompound()){
            InterestTypeString = getString(R.string.yearly_compound);
        }else if (myData.isSixMonthlyCompound()){
            InterestTypeString = getString(R.string.six_monthly_compound);
        }else if (myData.isThreeMonthlyCompound()){
            InterestTypeString = getString(R.string.three_monthly_compound);
        }else{
            InterestTypeString = getString(R.string.simple_interest);
        }
        InterestType.setText(InterestTypeString);

        builder.setView(customView);
        builder.setPositiveButton(getString(R.string.ok),null);
        builder.setNegativeButton(getString(R.string.undo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(OptionForSingleLoan.this);

                builder1.setTitle(getString(R.string.undo));
                builder1.setMessage(R.string.undo_loan_message);
                builder1.setPositiveButton(getString(R.string.undo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowProgress(OptionForSingleLoan.this);
                        if (GetMode){
                            reportGetLoanLocation(ThisLoanId,ALL).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGetLoanLocation(ThisLoanId,ALL).document(DocId).delete();
                                    }
                                }
                            });
                            reportGetLoanLocation(ThisLoanId,ThisLoanPerson).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGetLoanLocation(ThisLoanId,ThisLoanPerson).document(DocId).delete();
                                    }
                                }
                            });


                            lessLoanAllReport(ALL,GOT_LOAN,ThisLoanDate,ThisLoanAmount);
                            paidGetLoanLocation(ThisLoanId).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        double PayingInterest = documentSnapshot.getDouble(PAYING_INTEREST);
                                        Date payingDate = documentSnapshot.getDate(DATE);

                                        lessLoanAllReport(ALL,PAID_INTEREST,payingDate,PayingInterest);
                                        lessLoanAllReport(ALL,PAID_LOAN,payingDate,PayingInterest);

                                        paidGetLoanLocation(ThisLoanId).document(DocId).delete();
                                    }
                                    HideProgress();
                                    ShowDialog(OptionForSingleLoan.this,getString(R.string.deleted));
                                }
                            });
                            singleGetLoanPersonLocation(ThisLoanPerson).document(ThisLoanId).delete();
                            singleGetLoanLocation.document(ThisLoanId).delete();
                        }
                        else{
                            reportGiveLoanLocation(ThisLoanId,ALL).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGiveLoanLocation(ThisLoanId,ALL).document(DocId).delete();
                                    }
                                }
                            });
                            reportGiveLoanLocation(ThisLoanId,ThisLoanPerson).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGiveLoanLocation(ThisLoanId,ThisLoanPerson).document(DocId).delete();
                                    }
                                }
                            });


                            lessLoanAllReport(ALL,GIVEN_LOAN,ThisLoanDate,ThisLoanAmount);
                            paidGiveLoanLocation(ThisLoanId).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        double PayingInterest = documentSnapshot.getDouble(PAYING_INTEREST);
                                        Date payingDate = documentSnapshot.getDate(DATE);

                                        lessLoanAllReport(ALL,GIVEN_LOAN_INTEREST,payingDate,PayingInterest);
                                        lessLoanAllReport(ALL,GIVEN_LOAN_PAID,payingDate,PayingInterest);

                                        paidGiveLoanLocation(ThisLoanId).document(DocId).delete();
                                    }
                                    HideProgress();
                                    
                                    ShowDialog(OptionForSingleLoan.this,getString(R.string.deleted));
                                }
                            });
                            singleGiveLoanPersonLocation(ThisLoanPerson).document(ThisLoanId).delete();
                            singleGiveLoanLocation.document(ThisLoanId).delete();
                        }

                    }
                });

                builder1.create().show();
            }
        });
        builder.setNeutralButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!ThisisPaid){
                    ShowDialog(OptionForSingleLoan.this, getString(R.string.can_not_delete_bacause_payment_still_remaining));
                    return;
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(OptionForSingleLoan.this);

                builder1.setTitle(getString(R.string.delete));
                builder1.setMessage(R.string.delete_paid_loan_message);
                builder1.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowProgress(OptionForSingleLoan.this);
                        if (GetMode){

                            reportGetLoanLocation(ThisLoanId,ALL).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGetLoanLocation(ThisLoanId,ALL).document(DocId).delete();
                                    }
                                }
                            });

                            reportGetLoanLocation(ThisLoanId,ThisLoanPerson).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGetLoanLocation(ThisLoanId,ThisLoanPerson).document(DocId).delete();
                                    }
                                }
                            });


                            paidGetLoanLocation(ThisLoanId).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        paidGetLoanLocation(ThisLoanId).document(DocId).delete();
                                    }
                                    HideProgress();
                                    
                                    ShowDialog(OptionForSingleLoan.this,getString(R.string.deleted));
                                }
                            });
                            singleGetLoanPersonLocation(ThisLoanPerson).document(ThisLoanId).delete();
                            singleGetLoanLocation.document(ThisLoanId).delete();
                        }
                        else{
                            reportGiveLoanLocation(ThisLoanId,ALL).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGiveLoanLocation(ThisLoanId,ALL).document(DocId).delete();
                                    }
                                }
                            });
                            reportGiveLoanLocation(ThisLoanId,ThisLoanPerson).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGiveLoanLocation(ThisLoanId,ThisLoanPerson).document(DocId).delete();
                                    }
                                }
                            });


                            paidGiveLoanLocation(ThisLoanId).get().addOnSuccessListener(OptionForSingleLoan.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        paidGiveLoanLocation(ThisLoanId).document(DocId).delete();
                                    }
                                    HideProgress();
                                    
                                    ShowDialog(OptionForSingleLoan.this,getString(R.string.deleted));
                                }
                            });
                            singleGiveLoanPersonLocation(ThisLoanPerson).document(ThisLoanId).delete();
                            singleGiveLoanLocation.document(ThisLoanId).delete();
                        }

                    }
                });

                builder1.create().show();
            }
        });
        builder.create().show();
    }
    
}
