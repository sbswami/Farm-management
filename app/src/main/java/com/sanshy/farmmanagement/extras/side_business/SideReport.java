package com.sanshy.farmmanagement.extras.side_business;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DATE_SIDE_BUSINESS_KEY;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.EXPENDITURE_FIRESTORE_FIELD_KEY;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.INCOME_FIRESTORE_FIELD_KEY;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.dateInterval;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.sideReportLocation;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class SideReport extends AppCompatActivity {

    TextView Expenditure,Income,Profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_report);

        Expenditure = findViewById(R.id.side_expenditure);
        Income = findViewById(R.id.side_income);
        Profit = findViewById(R.id.side_profit);

    }

    @Override
    protected void onResume() {
        super.onResume();
        addSideReport();
    }
    double IncomeValue = 0;
    double ExpenditureValue = 0;
    double ProfitValue = 0;
    public void addSideReport(){
        IncomeValue = 0;
        ExpenditureValue = 0;
        ProfitValue = 0;
        ShowProgress(this);
        sideReportLocation.orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                            Map<String, Object> DataMap = queryDocumentSnapshot.getData();

                            ExpenditureValue += (double) DataMap.get(EXPENDITURE_FIRESTORE_FIELD_KEY);
                            IncomeValue += (double) DataMap.get(INCOME_FIRESTORE_FIELD_KEY);
                        }

                        ProfitValue = IncomeValue - ExpenditureValue;

                        Expenditure.setText(String.valueOf(ExpenditureValue));
                        Income.setText(String.valueOf(IncomeValue));
                        Profit.setText(String.valueOf(ProfitValue));
                        HideProgress();
                    }
                });
    }
    public void addSideReport(Date date1,Date date2){
        IncomeValue = 0;
        ExpenditureValue = 0;
        ProfitValue = 0;
        ShowProgress(this);
        sideReportLocation.whereGreaterThanOrEqualTo(DATE,date1)
                .whereLessThanOrEqualTo(DATE,date2).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                            Map<String, Object> DataMap = queryDocumentSnapshot.getData();

                            ExpenditureValue += (double) DataMap.get(EXPENDITURE_FIRESTORE_FIELD_KEY);
                            IncomeValue += (double) DataMap.get(INCOME_FIRESTORE_FIELD_KEY);
                        }

                        ProfitValue = IncomeValue - ExpenditureValue;

                        Expenditure.setText(String.valueOf(ExpenditureValue));
                        Income.setText(String.valueOf(IncomeValue));
                        Profit.setText(String.valueOf(ProfitValue));
                        HideProgress();
                    }
                });

    }


    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;

    public void FilterSideReportHistory(final View view){


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
                singleChooseDate = ChooseDateDialogSingle(SideReport.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(SideReport.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(SideReport.this,ToShowDate);
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
                        addSideReport(singleDateStatic,singleDateStaticTill);
                    }
                }
                else if(betweenDate&&fromChooseDate&&toChooseDate){
                    if (fromDateStatic!=null&&toDateStatic!=null){
                        addSideReport(fromDateStatic, toDateStatic);
                    }
                }
                builder.dismiss();
            }
        });

        builder.setView(customFilterView);

        builder.show();
    }
}
