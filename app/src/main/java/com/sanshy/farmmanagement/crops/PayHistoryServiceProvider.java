package com.sanshy.farmmanagement.crops;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.PayHistoryDataList;
import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.TwoColumnsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.payBuyerLocation;
import static com.sanshy.farmmanagement.MyStatic.payServiceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class PayHistoryServiceProvider extends AppCompatActivity {

    ListView PayListView;
    ArrayList<PayHistoryDataList> PayList = new ArrayList<>();
    TwoColumnsAdapter myAdapter;

    String ServiceProviderId,ServiceProviderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history_service_provider);

        Intent intent = getIntent();
        ServiceProviderName = intent.getStringExtra(SERVICE_PROVIDER_NAME);
        ServiceProviderId = intent.getStringExtra(SERVICE_PROVIDER_ID);

        PayListView = findViewById(R.id.service_p_history_list);

        myAdapter = new TwoColumnsAdapter(this,PayList);

        PayListView.setAdapter(myAdapter);
        

    }
    @Override
    protected void onStart() {
        super.onStart();

        addData();
    }

    OnFailureListener myFailData = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

            HideProgress();
            Log.d("Firestore", "Index : "+e.toString());
            System.out.println(e.toString());
        }
    };
    OnSuccessListener<QuerySnapshot> myListner = new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            PayList.clear();
            for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                Map<String, Object> CloudItem = documentSnapshot.getData();
                PayHistoryDataList item = new PayHistoryDataList(
                        CloudItem.get(ID).toString(),
                        (Date) CloudItem.get(DATE),
                        (double) CloudItem.get(AMOUNT)
                );
                PayList.add(item);
            }
            myAdapter.notifyDataSetChanged();
            HideProgress();
        }
    };


    private void addData() {
        ShowProgress(this);
        payServiceProviderLocation(ServiceProviderId)
                .orderBy(DATE, Query.Direction.DESCENDING)
                .get().addOnSuccessListener(this,myListner)
                .addOnFailureListener(this,myFailData);
    }
    private void addData(double minAmount, double maxAmount) {
        ShowProgress(this);
        payServiceProviderLocation(ServiceProviderId)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING)
                .get().addOnSuccessListener(this,myListner)
                .addOnFailureListener(this,myFailData);
    }
    private void addData(Date date1, Date date2) {
        ShowProgress(this);
        payServiceProviderLocation(ServiceProviderId)
                .whereGreaterThanOrEqualTo(DATE,date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING)
                .get().addOnSuccessListener(this,myListner)
                .addOnFailureListener(this,myFailData);
    }
    private void addData(double minAmount, double maxAmount, final Date date1, final Date date2) {
        ShowProgress(this);
        payServiceProviderLocation(ServiceProviderId)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING)
                .get().addOnSuccessListener(this,new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                PayList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
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
                            (double) CloudItem.get(AMOUNT)
                    );
                    PayList.add(item);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        }).addOnFailureListener(this,myFailData);
    }

    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;

    String minAmountFilterSt;
    String maxAmountFilterSt;

    public void FilterServiceProviderBt(View view){


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
                singleChooseDate = ChooseDateDialogSingle(PayHistoryServiceProvider.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(PayHistoryServiceProvider.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(PayHistoryServiceProvider.this,ToShowDate);
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
