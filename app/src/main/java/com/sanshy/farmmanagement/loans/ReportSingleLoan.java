package com.sanshy.farmmanagement.loans;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.GET_MODE;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_PAID;
import static com.sanshy.farmmanagement.MyStatic.GOT_LOAN;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.LOAN_ID;
import static com.sanshy.farmmanagement.MyStatic.PAID_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.PAID_LOAN;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.reportGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.reportGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class ReportSingleLoan extends AppCompatActivity {

    TextView LoanAmount,PaidInterest,PaidLoan;

    String LoanId;
    boolean GetMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_single_loan);

        Intent intent = getIntent();
        LoanId = intent.getStringExtra(LOAN_ID);
        GetMode = intent.getBooleanExtra(GET_MODE,true);

        LoanAmount = findViewById(R.id.loan_amount);
        PaidInterest = findViewById(R.id.paid_interest);
        PaidLoan = findViewById(R.id.paid_loan);

    }

    @Override
    protected void onStart() {
        super.onStart();

        addData(ALL);
    }

    private void addData(String FilterType) {

        ShowProgress(this);
        if (GetMode){
            reportGetLoanLocation(LoanId, FilterType).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    double loanA = 0;
                    double paidI = 0;
                    double paidL = 0;

                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        loanA += documentSnapshot.getDouble(GOT_LOAN);
                        paidI += documentSnapshot.getDouble(PAID_INTEREST);
                        paidL += documentSnapshot.getDouble(PAID_LOAN);
                    }

                    LoanAmount.setText(String.valueOf(loanA));
                    PaidInterest.setText(String.valueOf(paidI));
                    PaidLoan.setText(String.valueOf(paidL));
                    HideProgress();
                }
            });
        }else{
            reportGiveLoanLocation(LoanId, FilterType).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    double loanA = 0;
                    double paidI = 0;
                    double paidL = 0;

                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        loanA += documentSnapshot.getDouble(GIVEN_LOAN);
                        paidI += documentSnapshot.getDouble(GIVEN_LOAN_INTEREST);
                        paidL += documentSnapshot.getDouble(GIVEN_LOAN_PAID);
                    }

                    LoanAmount.setText(String.valueOf(loanA));
                    PaidInterest.setText(String.valueOf(paidI));
                    PaidLoan.setText(String.valueOf(paidL));
                    HideProgress();
                }
            });
        }
    }
    private void addData(Date date1, Date date2) {

        ShowProgress(this);
        if (GetMode){
            reportGetLoanLocation(LoanId, ALL).whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    double loanA = 0;
                    double paidI = 0;
                    double paidL = 0;

                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        loanA += documentSnapshot.getDouble(GOT_LOAN);
                        paidI += documentSnapshot.getDouble(PAID_INTEREST);
                        paidL += documentSnapshot.getDouble(PAID_LOAN);
                    }

                    LoanAmount.setText(String.valueOf(loanA));
                    PaidInterest.setText(String.valueOf(paidI));
                    PaidLoan.setText(String.valueOf(paidL));
                    HideProgress();
                }
            });
        }else{
            reportGiveLoanLocation(LoanId, ALL).whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    double loanA = 0;
                    double paidI = 0;
                    double paidL = 0;

                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        loanA += documentSnapshot.getDouble(GIVEN_LOAN);
                        paidI += documentSnapshot.getDouble(GIVEN_LOAN_INTEREST);
                        paidL += documentSnapshot.getDouble(GIVEN_LOAN_PAID);
                    }

                    LoanAmount.setText(String.valueOf(loanA));
                    PaidInterest.setText(String.valueOf(paidI));
                    PaidLoan.setText(String.valueOf(paidL));
                    HideProgress();
                }
            });
        }
    }


    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;

    public void FilterReportLoan(final View view){


        singleDate = false;
        betweenDate =false;
        singleChooseDate = false;
        fromChooseDate = false;
        toChooseDate = false;

        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_for_side_report,null);

        RadioGroup DateFilter = customFilterView.findViewById(R.id.date_filter);
        RadioButton SingleDateFilter = customFilterView.findViewById(R.id.single_date_filter);
        RadioButton BetweenDateFilter = customFilterView.findViewById(R.id.between_date_filter);

        final LinearLayout SingleDateContainer = customFilterView.findViewById(R.id.single_date_filter_container);
        Button SingleChooseDateFilter = customFilterView.findViewById(R.id.single_choose_date_filter);
        final TextView SingleShowDateFilter = customFilterView.findViewById(R.id.single_show_date_filter);

        SingleChooseDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseDate = ChooseDateDialogSingle(ReportSingleLoan.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(ReportSingleLoan.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(ReportSingleLoan.this,ToShowDate);
            }
        });


        DateFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.single_date_filter){
                    singleDate = true;
                    betweenDate = false;
                    SingleDateContainer.setVisibility(View.VISIBLE);
                    BetweenDateContainer.setVisibility(View.GONE);
                }else{
                    singleDate = false;
                    betweenDate = true;
                    SingleDateContainer.setVisibility(View.GONE);
                    BetweenDateContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        Button FilterBt = customFilterView.findViewById(R.id.filter_bt);

        FilterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singleDate&&singleChooseDate){
                    if (singleDateStatic!=null){
                        addData(singleDateStatic,singleDateStaticTill);
                    }
                }
                else if(betweenDate&&fromChooseDate&&toChooseDate){
                    if (fromDateStatic!=null&&toDateStatic!=null){
                        addData(fromDateStatic, toDateStatic);
                    }
                }
                builder.dismiss();
            }
        });

        builder.setView(customFilterView);

        builder.show();
    }
}
