package com.sanshy.farmmanagement.loans;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.PayHistoryDataList;
import com.sanshy.farmmanagement.TwoColumnsAdapter;
import com.sanshy.farmmanagement.crops.PayHistoryBuyer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.GET_MODE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_TYPE;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.paidGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.paidGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class PayHistorySingle extends AppCompatActivity {

    String LoanId;
    boolean GetMode;

    ListView PayListView;
    ArrayList<PayHistoryDataList> PayList = new ArrayList<>();
    TwoColumnsAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history_single);

        Intent intent = getIntent();

        LoanId = intent.getStringExtra(LOAN_ID);
        GetMode = intent.getBooleanExtra(GET_MODE,true);

        PayListView = findViewById(R.id.pay_history_single_loan_list);
        myAdapter = new TwoColumnsAdapter(this,PayList);
        PayListView.setAdapter(myAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        addData();
    }

    private void addData() {

        PayList.clear();
        ShowProgress(this);
        if (GetMode){
            paidGetLoanLocation(LoanId).orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        PayHistoryDataList item = new PayHistoryDataList(
                                CloudItem.get(ID).toString(),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AMOUNT)
                        );
                        PayList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();

                }
            });
        }else{
            paidGiveLoanLocation(LoanId).orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        PayHistoryDataList item = new PayHistoryDataList(
                                CloudItem.get(ID).toString(),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AMOUNT)
                        );
                        PayList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();

                }
            });
        }
    }
    private void addData(double minAmount, double maxAmount) {
        PayList.clear();

        ShowProgress(this);
        if (GetMode){
            paidGetLoanLocation(LoanId)
                    .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        PayHistoryDataList item = new PayHistoryDataList(
                                CloudItem.get(ID).toString(),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AMOUNT)
                        );
                        PayList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();

                }
            });
        }else{
            paidGiveLoanLocation(LoanId)
                    .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        PayHistoryDataList item = new PayHistoryDataList(
                                CloudItem.get(ID).toString(),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AMOUNT)
                        );
                        PayList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();

                }
            });
        }
    }
    private void addData(Date date1, Date date2) {
        PayList.clear();

        ShowProgress(this);
        if (GetMode){
            paidGetLoanLocation(LoanId)
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE, date2)
                    .orderBy(DATE, Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        PayHistoryDataList item = new PayHistoryDataList(
                                CloudItem.get(ID).toString(),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AMOUNT)
                        );
                        PayList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();

                }
            });
        }else{
            paidGiveLoanLocation(LoanId)
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE, date2)
                    .orderBy(DATE, Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        PayHistoryDataList item = new PayHistoryDataList(
                                CloudItem.get(ID).toString(),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AMOUNT)
                        );
                        PayList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();

                }
            });
        }
    }
    private void addData(double minAmount, double maxAmount, final Date date1, final Date date2) {
        PayList.clear();

        ShowProgress(this);
        if (GetMode){
            paidGetLoanLocation(LoanId)// Time To Do Something
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    Log.d("FireStore","Chutiya: "+task.getException()+" or "+task.toString());
                }
            })

                    .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }

                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        PayHistoryDataList item = new PayHistoryDataList(
                                CloudItem.get(ID).toString(),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AMOUNT)
                        );
                        PayList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();

                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                    Log.d("Chutiya", "Index : "+e.toString());
                }
            });
        }else{
            paidGiveLoanLocation(LoanId)
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }

                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        PayHistoryDataList item = new PayHistoryDataList(
                                CloudItem.get(ID).toString(),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AMOUNT)
                        );
                        PayList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                    Log.d("Firestore", "Index : "+e.toString());
                }
            });
        }
    }

    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;

    String minAmountFilterSt;
    String maxAmountFilterSt;
    
    public void FilterPayHistorySingleLoanBt(View view){


        singleDate = false;
        betweenDate =false;
        singleChooseDate = false;
        fromChooseDate = false;
        toChooseDate = false;


        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_pay,null);


        final EditText MinAmountFilter = customFilterView.findViewById(R.id.min_amount_filter);
        final EditText MaxAmountFilter = customFilterView.findViewById(R.id.max_amount_filter);

        RadioGroup DateFilter = customFilterView.findViewById(R.id.date_filter);
        RadioButton SingleDateFilter = customFilterView.findViewById(R.id.single_date_filter);
        RadioButton BetweenDateFilter = customFilterView.findViewById(R.id.between_date_filter);


        final LinearLayout SingleDateContainer = customFilterView.findViewById(R.id.single_date_filter_container);
        Button SingleChooseDateFilter = customFilterView.findViewById(R.id.single_choose_date_filter);
        final TextView SingleShowDateFilter = customFilterView.findViewById(R.id.single_show_date_filter);

        SingleChooseDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseDate = ChooseDateDialogSingle(PayHistorySingle.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(PayHistorySingle.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(PayHistorySingle.this,ToShowDate);
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

                minAmountFilterSt = MinAmountFilter.getText().toString();
                maxAmountFilterSt = MaxAmountFilter.getText().toString();

                if (minAmountFilterSt.isEmpty()&&!maxAmountFilterSt.isEmpty()){
                    MinAmountFilter.setText(R.string.default_min_amount);
                    minAmountFilterSt = getString(R.string.default_min_amount);
                }
                if (maxAmountFilterSt.isEmpty()&&!minAmountFilterSt.isEmpty()){
                    MaxAmountFilter.setText(R.string.default_max_amount);
                    maxAmountFilterSt = getString(R.string.default_max_amount);
                }

                if ((!singleDate)&&(!betweenDate)&&(!minAmountFilterSt.isEmpty())&&(!maxAmountFilterSt.isEmpty())){
                    addData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt));
                }

                if (singleDate&&singleChooseDate){
                    if ((minAmountFilterSt.isEmpty())&&(maxAmountFilterSt.isEmpty())){
                        addData(singleDateStatic,singleDateStaticTill);
                    }else{
                        addData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                singleDateStatic,singleDateStaticTill);
                    }
                }

                if (betweenDate&&toChooseDate&&fromChooseDate){
                    if ((minAmountFilterSt.isEmpty())&&(maxAmountFilterSt.isEmpty())){
                        addData(fromDateStatic,toDateStatic);
                    }else{
                        addData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                fromDateStatic,toDateStatic);
                    }
                }

                builder.dismiss();

            }
        });

        builder.setView(customFilterView);

        builder.show();
    }
}
