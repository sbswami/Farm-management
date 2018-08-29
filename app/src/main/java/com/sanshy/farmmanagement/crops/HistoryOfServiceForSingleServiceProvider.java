package com.sanshy.farmmanagement.crops;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AREA_OF_LAND;
import static com.sanshy.farmmanagement.MyStatic.BY_PARTNER;
import static com.sanshy.farmmanagement.MyStatic.CASH_MODE;
import static com.sanshy.farmmanagement.MyStatic.CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.ShowDetails;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.purchaseServiceProviderLocation;

public class HistoryOfServiceForSingleServiceProvider extends AppCompatActivity {

    ListView HistoryList;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();
    ArrayList<SingleSowingCropData> SowingList = new ArrayList<>();
    SingleFourColumnsListAdapter myAdapter;

    String ServiceProviderName;
    String ServiceProviderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_of_service_for_single_service_provider);

        Intent intent = getIntent();

        ServiceProviderId = intent.getStringExtra(SERVICE_PROVIDER_ID);
        ServiceProviderName = intent.getStringExtra(SERVICE_PROVIDER_NAME);

        HistoryList = findViewById(R.id.sowing_history_list);

        myAdapter = new SingleFourColumnsListAdapter(this,FourList);

        HistoryList.setAdapter(myAdapter);

        HistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String[] dataList = new String[7];

                dataList[0] = SowingList.get(position).getSowingRemark();
                dataList[1] = String.valueOf(SowingList.get(position).getSowingAmount());
                dataList[2] = DateToString(SowingList.get(position).getSowingDate());
                dataList[3] = SowingList.get(position).getServiceProviderName();
                dataList[4] = String.valueOf(SowingList.get(position).getSowingLandArea());
                dataList[5] = SowingList.get(position).isCashService() ?getString(R.string.cash):getString(R.string.borrow);
                dataList[6] = SowingList.get(position).isCheckPartner() ?SowingList.get(position).getPartnerName():getString(R.string.me);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HistoryOfServiceForSingleServiceProvider.this);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.custom_show_detail_crops,null,true);

                TextView Remark = myView.findViewById(R.id.remark_dialog);
                TextView Amount = myView.findViewById(R.id.amount_dialog);
                TextView Date = myView.findViewById(R.id.date_dialog);
                TextView ServiceProvider = myView.findViewById(R.id.service_provider_dialog);
                TextView AreaOfLand = myView.findViewById(R.id.area_of_land_dialog);
                TextView PaymentMode = myView.findViewById(R.id.payment_mode_dialog);
                TextView PaidBy = myView.findViewById(R.id.paid_by_dialog);

                Remark.setText(dataList[0]);
                Amount.setText(dataList[1]);
                Date.setText(dataList[2]);
                ServiceProvider.setText(dataList[3]);
                AreaOfLand.setText(dataList[4]);
                PaymentMode.setText(dataList[5]);
                PaidBy.setText(dataList[6]);

                final ArrayList<Integer> temp = new ArrayList<>();

                builder.setView(myView)
                        .setPositiveButton(getString(R.string.ok), null);
                builder.create().show();

            }
        });

        AddHistoryList();
    }
    private void AddHistoryList() {

        FourList.clear();
        SowingList.clear();
        ShowProgress(this);
        purchaseServiceProviderLocation(ServiceProviderId).orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Map<String, Object> CloudItem = documentSnapshot.getData();

                    SingleSowingCropData item = new SingleSowingCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Date) CloudItem.get(DATE),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(SERVICE_PROVIDER_NAME).toString(),
                            CloudItem.get(SERVICE_PROVIDER_ID).toString(),
                            (Boolean) CloudItem.get(BY_PARTNER)
                    );

                    if ((Boolean) CloudItem.get(BY_PARTNER)){
                        item = new SingleSowingCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                CloudItem.get(CROP_ID).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(AREA_OF_LAND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(CASH_MODE),
                                CloudItem.get(SERVICE_PROVIDER_NAME).toString(),
                                CloudItem.get(SERVICE_PROVIDER_ID).toString(),
                                (Boolean) CloudItem.get(BY_PARTNER),
                                CloudItem.get(PARTNER_NAME).toString(),
                                CloudItem.get(PARTNER_ID).toString()
                        );
                    }

                    SowingList.add(item);
                    int indexOfC = item.getCropId().indexOf("_");

                    SingleFourColumnsList iitem = new SingleFourColumnsList(
                            DateToString(item.getSowingDate()),
                            item.getCropId().substring(0,indexOfC),
                            item.getSowingRemark(),
                            String.valueOf(item.getSowingAmount()));
                    FourList.add(iitem);

                }

                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });
        
    }

    public void FilterServiceHistory(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter,null);

        AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
        AutoCompleteTextView ServiceProviderFilter = customFilterView.findViewById(R.id.service_provider_filter);
        AutoCompleteTextView PartnerFilter = customFilterView.findViewById(R.id.partner_filter);

        ServiceProviderFilter.setText(ServiceProviderName);

        RadioGroup FilterModeOfPayment = customFilterView.findViewById(R.id.filter_mode_of_payment);
        RadioButton FilterCashMode = customFilterView.findViewById(R.id.filter_cash_mode);
        RadioButton FilterBorrowMode = customFilterView.findViewById(R.id.filter_borrow_mode);

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
                singleChoose.add(0,ChooseDateDialogSingle(HistoryOfServiceForSingleServiceProvider.this,SingleShowDateFilter));
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
                fromChoose.add(0,ChooseDateDialogFrom(HistoryOfServiceForSingleServiceProvider.this,FromShowDate));
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        final ArrayList<Boolean> toChoose = new ArrayList<>();
        toChoose.add(false);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChoose.add(0,ChooseDateDialogTo(HistoryOfServiceForSingleServiceProvider.this,ToShowDate));
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
