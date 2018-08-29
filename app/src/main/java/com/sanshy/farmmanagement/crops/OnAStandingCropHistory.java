package com.sanshy.farmmanagement.crops;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AREA_OF_LAND;
import static com.sanshy.farmmanagement.MyStatic.BY_PARTNER;
import static com.sanshy.farmmanagement.MyStatic.CASH_MODE;
import static com.sanshy.farmmanagement.MyStatic.CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.ON_A_STANDING;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.ShowDetails;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.lessAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.lessAllReport;
import static com.sanshy.farmmanagement.MyStatic.lessSingleReport;
import static com.sanshy.farmmanagement.MyStatic.onAStandingCropLocation;
import static com.sanshy.farmmanagement.MyStatic.purchaseServiceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.removeRemainingAmountServiceProvider;
import static com.sanshy.farmmanagement.MyStatic.singleCropReportLocation;

public class OnAStandingCropHistory extends Activity {

    ListView HistoryList;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();
    ArrayList<SingleSowingCropData> OnAStandingList = new ArrayList<>();
    SingleFourColumnsListAdapter myAdapter;

    String CropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_astanding_crop_history);

        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);

        HistoryList = findViewById(R.id.on_a_standing_history_list);

        myAdapter = new SingleFourColumnsListAdapter(this,FourList);

        HistoryList.setAdapter(myAdapter);

        HistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String Id = OnAStandingList.get(position).getSowingId();

                String[] dataList = new String[7];

                dataList[0] = OnAStandingList.get(position).getSowingRemark();
                dataList[1] = String.valueOf(OnAStandingList.get(position).getSowingAmount());
                dataList[2] = DateToString(OnAStandingList.get(position).getSowingDate());
                dataList[3] = OnAStandingList.get(position).getServiceProviderName();
                dataList[4] = String.valueOf(OnAStandingList.get(position).getSowingLandArea());
                dataList[5] = OnAStandingList.get(position).isCashService() ?getString(R.string.cash):getString(R.string.borrow);
                dataList[6] = OnAStandingList.get(position).isCheckPartner() ?OnAStandingList.get(position).getPartnerName():getString(R.string.me);

                boolean UndoCheck = ShowDetails(OnAStandingCropHistory.this,dataList);

                if (UndoCheck){
                    AlertDialog.Builder builder = new AlertDialog.Builder(OnAStandingCropHistory.this);

                    builder.setTitle(getString(R.string.undo));
                    builder.setMessage(getString(R.string.are_you_sure_you_want_undo));
                    builder.setPositiveButton(getString(R.string.close),null);
                    builder.setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onAStandingCropLocation(CropId).document(Id).delete();
                            purchaseServiceProviderLocation(OnAStandingList.get(position).getServiceProviderId()).document(Id).delete();
                            onAStandingCropLocation(CropId);
                            FourList.remove(position);
                            double AmountR = OnAStandingList.get(position).getSowingAmount();
                            boolean CashMode = OnAStandingList.get(position).isCashService();
                            lessSingleReport(CashMode,CropId,ALL,AmountR,ON_A_STANDING);
                            lessAllReport(CashMode,ALL,AmountR,ON_A_STANDING);
                            if (OnAStandingList.get(position).isCheckPartner()){
                                lessSingleReport(CashMode,CropId,OnAStandingList.get(position).getPartnerName(),AmountR,ON_A_STANDING);
                                lessAllReport(CashMode,OnAStandingList.get(position).getPartnerName(),AmountR,ON_A_STANDING);
                            }
                            else{
                                lessSingleReport(CashMode,CropId,SELF,AmountR,ON_A_STANDING);
                                lessAllReport(CashMode,SELF,AmountR,ON_A_STANDING);
                            }
                            if (!CashMode){
                                removeRemainingAmountServiceProvider(OnAStandingList.get(position).getServiceProviderId(),AmountR);
                                lessAllRemainingReport(ALL,AmountR,SERVICE_PROVIDER);
                                if (OnAStandingList.get(position).isCheckPartner()){
                                    lessAllRemainingReport(OnAStandingList.get(position).getPartnerName(),AmountR,SERVICE_PROVIDER);
                                }else{
                                    lessAllRemainingReport(SELF,AmountR,SERVICE_PROVIDER);
                                }
                            }
                            OnAStandingList.remove(position);
                            myAdapter.notifyDataSetChanged();
                        }
                    });

                    builder.create().show();

                }
            }
        });

        AddHistoryList();

    }

    private void AddHistoryList() {
        ShowProgress(this);
        onAStandingCropLocation(CropId).orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
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

                    OnAStandingList.add(item);

                    SingleFourColumnsList iitem = new SingleFourColumnsList(DateToString(item.getSowingDate()),item.getSowingRemark(),String.valueOf(item.SowingAmount),item.ServiceProviderName);
                    FourList.add(iitem);

                }

                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });
    }

    public void postSingleReport(final boolean CashMode, final String FilterType, final double AmountR){
        singleCropReportLocation(CropId,FilterType).document(ON_A_STANDING).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> myMap = new HashMap<>();
                double cashAmount = 0;
                double borrowAmount = 0;
                Map<String, Object> CloudMap = documentSnapshot.getData();
                cashAmount = (double) CloudMap.get(MyStatic.CASH_AMOUNT);
                borrowAmount = (double) CloudMap.get(MyStatic.BORROW_AMOUNT);

                if (CashMode){
                    cashAmount-= AmountR;
                }else{
                    borrowAmount-= AmountR;
                }

                myMap.put(MyStatic.CASH_AMOUNT,cashAmount);
                myMap.put(MyStatic.BORROW_AMOUNT,borrowAmount);

                singleCropReportLocation(CropId,FilterType).document(ON_A_STANDING).set(myMap);

            }
        });
    }

    public void FilterOnAStandingHistory(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter,null);

        AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
        AutoCompleteTextView ServiceProviderFilter = customFilterView.findViewById(R.id.service_provider_filter);
        AutoCompleteTextView PartnerFilter = customFilterView.findViewById(R.id.partner_filter);

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
                singleChoose.add(0,ChooseDateDialogSingle(OnAStandingCropHistory.this,SingleShowDateFilter));
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
                fromChoose.add(0,ChooseDateDialogFrom(OnAStandingCropHistory.this,FromShowDate));
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        final ArrayList<Boolean> toChoose = new ArrayList<>();
        toChoose.add(false);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChoose.add(0,ChooseDateDialogTo(OnAStandingCropHistory.this,ToShowDate));
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
