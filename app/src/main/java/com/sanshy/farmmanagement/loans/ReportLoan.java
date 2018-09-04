package com.sanshy.farmmanagement.loans;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_PAID;
import static com.sanshy.farmmanagement.MyStatic.GOT_LOAN;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.PAID_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.PAID_LOAN;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.loanPersonList;
import static com.sanshy.farmmanagement.MyStatic.reportLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class ReportLoan extends AppCompatActivity {

    TextView GetLoan,PaidInterest,PaidLoan;
    TextView GiveLoan,GivenPaidInterest,GivenPaidLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_loan);

        GetLoan = findViewById(R.id.get_loan);
        PaidInterest = findViewById(R.id.paid_interest);
        PaidLoan = findViewById(R.id.paid_loan);

        GiveLoan = findViewById(R.id.give_loan);
        GivenPaidInterest = findViewById(R.id.give_loan_paid_interest);
        GivenPaidLoan = findViewById(R.id.paid_give_loan);

    }


    @Override
    protected void onStart() {
        super.onStart();
        addData(ALL);

        loanPersonList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Hint = (ArrayList<String>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    double gotLoan = 0;
    double paidInterest = 0;
    double paidLoan = 0;
    double giveLoan = 0;
    double givePaidInterest = 0;
    double givePaidLoan = 0;

    private void addData(String FilterType) {

        gotLoan = 0;
        paidInterest = 0;
        paidLoan = 0;
        giveLoan = 0;
        givePaidInterest = 0;
        givePaidLoan = 0;

        ShowProgress(this);
        reportLoanLocation(FilterType).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Map<String, Object> CloudItem = documentSnapshot.getData();

                    gotLoan +=(Double) CloudItem.get(GOT_LOAN);
                    paidInterest += (Double) CloudItem.get(PAID_INTEREST);
                    paidLoan += (Double) CloudItem.get(PAID_LOAN);
                    giveLoan += (Double) CloudItem.get(GIVEN_LOAN);
                    givePaidInterest += (Double) CloudItem.get(GIVEN_LOAN_INTEREST);
                    givePaidLoan += (Double) CloudItem.get(GIVEN_LOAN_PAID);

                }

                GetLoan.setText(String.valueOf(Math.round(gotLoan)));
                PaidInterest.setText(String.valueOf(Math.round(paidInterest)));
                PaidLoan.setText(String.valueOf(Math.round(paidLoan)));
                GiveLoan.setText(String.valueOf(Math.round(giveLoan)));
                GivenPaidInterest.setText(String.valueOf(Math.round(givePaidInterest)));
                GivenPaidLoan.setText(String.valueOf(Math.round(givePaidLoan)));
                HideProgress();
            }
        });
    }

    private void addData(String FilterType, Date date1, Date date2) {

        gotLoan = 0;
        paidInterest = 0;
        paidLoan = 0;
        giveLoan = 0;
        givePaidInterest = 0;
        givePaidLoan = 0;

        ShowProgress(this);
        reportLoanLocation(FilterType).whereGreaterThanOrEqualTo(DATE,date1)
                .whereLessThanOrEqualTo(DATE,date2).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Map<String, Object> CloudItem = documentSnapshot.getData();

                    gotLoan +=(Double) CloudItem.get(GOT_LOAN);
                    paidInterest += (Double) CloudItem.get(PAID_INTEREST);
                    paidLoan += (Double) CloudItem.get(PAID_LOAN);
                    giveLoan += (Double) CloudItem.get(GIVEN_LOAN);
                    givePaidInterest += (Double) CloudItem.get(GIVEN_LOAN_INTEREST);
                    givePaidLoan += (Double) CloudItem.get(GIVEN_LOAN_PAID);

                }

                GetLoan.setText(String.valueOf(Math.round(gotLoan)));
                PaidInterest.setText(String.valueOf(Math.round(paidInterest)));
                PaidLoan.setText(String.valueOf(Math.round(paidLoan)));
                GiveLoan.setText(String.valueOf(Math.round(giveLoan)));
                GivenPaidInterest.setText(String.valueOf(Math.round(givePaidInterest)));
                GivenPaidLoan.setText(String.valueOf(Math.round(givePaidLoan)));
                HideProgress();
            }
        });
    }



    String loanPersonString;
    ArrayList<String> Hint = new ArrayList<>();

    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;
    
    public void FilterReportLoan(View view){

        singleDate = false;
        betweenDate =false;
        singleChooseDate = false;
        fromChooseDate = false;
        toChooseDate = false;

        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_loan_report,null);

        final AutoCompleteTextView LoanPersonFilter = customFilterView.findViewById(R.id.loan_person_filter);
        setSnipper(this,LoanPersonFilter,Hint);

        RadioGroup DateFilter = customFilterView.findViewById(R.id.date_filter);
        RadioButton SingleDateFilter = customFilterView.findViewById(R.id.single_date_filter);
        RadioButton BetweenDateFilter = customFilterView.findViewById(R.id.between_date_filter);


        final LinearLayout SingleDateContainer = customFilterView.findViewById(R.id.single_date_filter_container);
        Button SingleChooseDateFilter = customFilterView.findViewById(R.id.single_choose_date_filter);
        final TextView SingleShowDateFilter = customFilterView.findViewById(R.id.single_show_date_filter);

        SingleChooseDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseDate = ChooseDateDialogSingle(ReportLoan.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(ReportLoan.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(ReportLoan.this,ToShowDate);
            }
        });


        Button FilterBt = customFilterView.findViewById(R.id.filter_bt);


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

        FilterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loanPersonString = LoanPersonFilter.getText().toString();

                if ((!singleDate)&&(!betweenDate)&&(!loanPersonString.isEmpty())){
                    if (Hint.contains(loanPersonString)){
                        addData(loanPersonString);
                    }else{
                        onWrongValue();
                    }
                }

                if (singleDate&&singleChooseDate){
                    if (loanPersonString.isEmpty()){
                        addData(ALL,singleDateStatic,singleDateStaticTill);
                    }else{
                        if (Hint.contains(loanPersonString)){
                            addData(loanPersonString,singleDateStatic,singleDateStaticTill);
                        }else{
                            onWrongValue();
                        }
                    }
                }

                if (betweenDate&&toChooseDate&&fromChooseDate){
                    if (loanPersonString.isEmpty()){
                        addData(ALL,fromDateStatic,toDateStatic);
                    }else{
                        if (Hint.contains(loanPersonString)){
                            addData(loanPersonString,fromDateStatic,toDateStatic);
                        }else{
                            onWrongValue();
                        }
                    }
                }

                builder.dismiss();

            }
        });
        builder.setView(customFilterView);

        builder.show();
    }

    public void onWrongValue(){
        ShowDialog(ReportLoan.this,getString(R.string.please_fill_correct_value));
    }
}
