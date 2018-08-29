package com.sanshy.farmmanagement.crops;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import static com.sanshy.farmmanagement.MyStatic.payBuyerLocation;
import static com.sanshy.farmmanagement.MyStatic.payServiceProviderLocation;

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

        addData();

    }

    private void addData() {
        PayList.clear();
        ShowProgress(this);
        payServiceProviderLocation(ServiceProviderId).orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
        });

        myAdapter.notifyDataSetChanged();
    }

    public void FilterServiceProviderBt(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_pay,null);


        EditText MinAmountFilter = customFilterView.findViewById(R.id.min_amount_filter);
        EditText MaxAmountFilter = customFilterView.findViewById(R.id.max_amount_filter);

        RadioGroup DateFilter = customFilterView.findViewById(R.id.date_filter);
        RadioButton SingleDateFilter = customFilterView.findViewById(R.id.single_date_filter);
        RadioButton BetweenDateFilter = customFilterView.findViewById(R.id.between_date_filter);

        LinearLayout SingleDateContainer = customFilterView.findViewById(R.id.single_date_filter_container);
        Button SingleChooseDateFilter = customFilterView.findViewById(R.id.single_choose_date_filter);

        final TextView SingleShowDateFilter = customFilterView.findViewById(R.id.single_show_date_filter);

        final ArrayList<Boolean> singleChoose = new ArrayList<>();
        singleChoose.add(false);

        SingleChooseDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChoose.add(0,ChooseDateDialogSingle(PayHistoryServiceProvider.this,SingleShowDateFilter));
            }
        });

        LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        final ArrayList<Boolean> fromChoose = new ArrayList<>();
        fromChoose.add(false);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChoose.add(0,ChooseDateDialogFrom(PayHistoryServiceProvider.this,FromShowDate));
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        final ArrayList<Boolean> toChoose = new ArrayList<>();
        toChoose.add(false);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChoose.add(0,ChooseDateDialogTo(PayHistoryServiceProvider.this,ToShowDate));
            }
        });

        Button FilterBt = customFilterView.findViewById(R.id.filter_bt);

        FilterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        builder.setView(customFilterView);

        builder.create().show();
    }
}
