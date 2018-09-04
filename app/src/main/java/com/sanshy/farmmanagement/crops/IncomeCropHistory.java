package com.sanshy.farmmanagement.crops;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AREA_OF_LAND;
import static com.sanshy.farmmanagement.MyStatic.BUYER_ID;
import static com.sanshy.farmmanagement.MyStatic.BUYER_NAME;
import static com.sanshy.farmmanagement.MyStatic.CASH_MODE;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.INCOME;
import static com.sanshy.farmmanagement.MyStatic.PARTNER;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_AMOUNT_1;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_AMOUNT_2;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_AMOUNT_3;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_AMOUNT_4;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_AMOUNT_5;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_COUNT;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID_1;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID_2;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID_3;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID_4;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID_5;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_1;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_2;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_3;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_4;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_5;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_SHARE_1;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_SHARE_2;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_SHARE_3;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_SHARE_4;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_SHARE_5;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SELF_INCOME;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.buyerList;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.incomeCropLocation;
import static com.sanshy.farmmanagement.MyStatic.RATE;
import static com.sanshy.farmmanagement.MyStatic.WEIGHT;
import static com.sanshy.farmmanagement.MyStatic.incomeCropRemark;
import static com.sanshy.farmmanagement.MyStatic.lessAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.lessAllReport;
import static com.sanshy.farmmanagement.MyStatic.lessSingleReport;
import static com.sanshy.farmmanagement.MyStatic.BUYER;
import static com.sanshy.farmmanagement.MyStatic.purchaseBuyerLocation;
import static com.sanshy.farmmanagement.MyStatic.removeRemainingAmountBuyer;
import static com.sanshy.farmmanagement.MyStatic.CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class IncomeCropHistory extends AppCompatActivity {

    ListView HistoryList;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();
    ArrayList<SingleIncomeCropData> IncomeList = new ArrayList<>();
    SingleFourColumnsListAdapter myAdapter;
    
    String CropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_crop_history);
        
        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);
        
        HistoryList = findViewById(R.id.income_history_list);

        myAdapter = new SingleFourColumnsListAdapter(this,FourList);

        HistoryList.setAdapter(myAdapter);

        HistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SingleIncomeCropData List = IncomeList.get(position);
                final String Id = List.getIncomeId();

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(IncomeCropHistory.this);

                LayoutInflater inflater = (LayoutInflater) IncomeCropHistory.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.custom_show_details_for_income,null,true);

                TextView Remark = myView.findViewById(R.id.remark_dialog);
                TextView RatePerWeight = myView.findViewById(R.id.rate_dialog);
                TextView Weight = myView.findViewById(R.id.weight_dialog);
                TextView Amount = myView.findViewById(R.id.amount_dialog);
                TextView Date = myView.findViewById(R.id.date_dialog);
                TextView BuyerName = myView.findViewById(R.id.buyer_dialog);
                TextView AreaOfLand = myView.findViewById(R.id.area_of_land_dialog);
                TextView PaymentMode = myView.findViewById(R.id.payment_mode_dialog);
                TextView MyIncome = myView.findViewById(R.id.my_income_dialog);

                TableRow Partner1 = myView.findViewById(R.id.partner_1_container);
                TableRow Partner2 = myView.findViewById(R.id.partner_2_container);
                TableRow Partner3 = myView.findViewById(R.id.partner_3_container);
                TableRow Partner4 = myView.findViewById(R.id.partner_4_container);
                TableRow Partner5 = myView.findViewById(R.id.partner_5_container);

                TextView PName1 = myView.findViewById(R.id.partner_name1);
                TextView PName2 = myView.findViewById(R.id.partner_name2);
                TextView PName3 = myView.findViewById(R.id.partner_name3);
                TextView PName4 = myView.findViewById(R.id.partner_name4);
                TextView PName5 = myView.findViewById(R.id.partner_name5);

                TextView PShare1 = myView.findViewById(R.id.partner_share1);
                TextView PShare2 = myView.findViewById(R.id.partner_share2);
                TextView PShare3 = myView.findViewById(R.id.partner_share3);
                TextView PShare4 = myView.findViewById(R.id.partner_share4);
                TextView PShare5 = myView.findViewById(R.id.partner_share5);

                long PartnerCount = List.getPartnerCount();
                int pC = (int) PartnerCount;

                switch (pC){
                    case 5 :
                        Partner5.setVisibility(View.VISIBLE);
                        PName5.setText(List.getPartnerName5());
                        PShare5.setText(String.valueOf(List.getPartnerShareAmount5()));
                    case 4 :
                        Partner4.setVisibility(View.VISIBLE);
                        PName4.setText(List.getPartnerName4());
                        PShare4.setText(String.valueOf(List.getPartnerShareAmount4()));
                    case 3 :
                        Partner3.setVisibility(View.VISIBLE);
                        PName3.setText(List.getPartnerName3());
                        PShare3.setText(String.valueOf(List.getPartnerShareAmount3()));
                    case 2 :
                        Partner2.setVisibility(View.VISIBLE);
                        PName2.setText(List.getPartnerName2());
                        PShare2.setText(String.valueOf(List.getPartnerShareAmount2()));
                    case 1 :
                        Partner1.setVisibility(View.VISIBLE);
                        PName1.setText(List.getPartnerName1());
                        PShare1.setText(String.valueOf(List.getPartnerShareAmount1()));
                }

                Remark.setText(List.getIncomeRemark());
                Amount.setText(String.valueOf(List.getIncomeAmount()));
                Date.setText(DateToString(List.getIncomeDate()));
                BuyerName.setText(List.getBuyerCropsName());
                AreaOfLand.setText(String.valueOf(List.getIncomeLandArea()));
                PaymentMode.setText(List.isCashBuyer() ?IncomeCropHistory.this.getString(R.string.cash):IncomeCropHistory.this.getString(R.string.borrow));
                RatePerWeight.setText(String.valueOf(List.getIncomeCropRate()));
                Weight.setText(String.valueOf(List.getIncomeCropWeight()));
                MyIncome.setText(String.valueOf(List.getSelfShareAmount()));


                builder.setView(myView)
                        .setPositiveButton(IncomeCropHistory.this.getString(R.string.ok), null);

                builder.setNegativeButton(IncomeCropHistory.this.getString(R.string.undo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(IncomeCropHistory.this);

                        builder1.setTitle(getString(R.string.undo));
                        builder1.setMessage(getString(R.string.are_you_sure_you_want_undo));
                        builder1.setPositiveButton(getString(R.string.close),null);
                        builder1.setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                incomeCropLocation(CropId).document(Id).delete();
                                purchaseBuyerLocation(IncomeList.get(position).getBuyerCropsId()).document(Id).delete();
                                incomeCropLocation(CropId);
                                FourList.remove(position);
                                double AmountR = IncomeList.get(position).getIncomeAmount();
                                boolean CashMode = IncomeList.get(position).isCashBuyer();
                                lessSingleReport(CashMode,CropId,ALL,AmountR,INCOME);
                                lessAllReport(CashMode,ALL,AmountR,INCOME);
                                if (!CashMode){
                                    removeRemainingAmountBuyer(IncomeList.get(position).getBuyerCropsId(),AmountR);
                                    lessAllRemainingReport(ALL,AmountR,BUYER);
                                    if (IncomeList.get(position).isCheckPartner()){
                                        long pC = IncomeList.get(position).getPartnerCount();
                                        int PC = (int) pC;
                                        switch (PC){
                                            case 5:
                                                lessAllRemainingReport(IncomeList.get(position).getPartnerName5(),IncomeList.get(position).getPartnerShareAmount5(),BUYER);
                                            case 4:
                                                lessAllRemainingReport(IncomeList.get(position).getPartnerName4(),IncomeList.get(position).getPartnerShareAmount4(),BUYER);
                                            case 3:
                                                lessAllRemainingReport(IncomeList.get(position).getPartnerName3(),IncomeList.get(position).getPartnerShareAmount3(),BUYER);
                                            case 2:
                                                lessAllRemainingReport(IncomeList.get(position).getPartnerName2(),IncomeList.get(position).getPartnerShareAmount2(),BUYER);
                                            case 1:
                                                lessAllRemainingReport(IncomeList.get(position).getPartnerName1(),IncomeList.get(position).getPartnerShareAmount1(),BUYER);
                                        }
                                    }
                                    lessAllRemainingReport(SELF,IncomeList.get(position).getSelfShareAmount(),BUYER);

                                }
                                if (IncomeList.get(position).isCheckPartner()){
                                    long pC = IncomeList.get(position).getPartnerCount();
                                    int PC = (int) pC;
                                    switch (PC){
                                        case 5:
                                            lessSingleReport(CashMode,CropId,IncomeList.get(position).getPartnerName5(),IncomeList.get(position).getPartnerShareAmount5(),INCOME);
                                            lessAllReport(CashMode,IncomeList.get(position).getPartnerName5(),IncomeList.get(position).getPartnerShareAmount5(),INCOME);
                                        case 4:
                                            lessSingleReport(CashMode,CropId,IncomeList.get(position).getPartnerName4(),IncomeList.get(position).getPartnerShareAmount4(),INCOME);
                                            lessAllReport(CashMode,IncomeList.get(position).getPartnerName4(),IncomeList.get(position).getPartnerShareAmount4(),INCOME);
                                        case 3:
                                            lessSingleReport(CashMode,CropId,IncomeList.get(position).getPartnerName3(),IncomeList.get(position).getPartnerShareAmount3(),INCOME);
                                            lessAllReport(CashMode,IncomeList.get(position).getPartnerName3(),IncomeList.get(position).getPartnerShareAmount3(),INCOME);
                                        case 2:
                                            lessSingleReport(CashMode,CropId,IncomeList.get(position).getPartnerName2(),IncomeList.get(position).getPartnerShareAmount2(),INCOME);
                                            lessAllReport(CashMode,IncomeList.get(position).getPartnerName2(),IncomeList.get(position).getPartnerShareAmount2(),INCOME);
                                        case 1:
                                            lessSingleReport(CashMode,CropId,IncomeList.get(position).getPartnerName1(),IncomeList.get(position).getPartnerShareAmount1(),INCOME);
                                            lessAllReport(CashMode,IncomeList.get(position).getPartnerName1(),IncomeList.get(position).getPartnerShareAmount1(),INCOME);
                                    }
                                }
                                lessSingleReport(CashMode,CropId,SELF,IncomeList.get(position).getSelfShareAmount(),INCOME);
                                lessAllReport(CashMode,SELF,AmountR,INCOME);

                                IncomeList.remove(position);
                                myAdapter.notifyDataSetChanged();
                            }
                        });

                        builder1.create().show();
                    }
                });
                builder.create().show();

            }
        });

    }

    private void AddHistoryList() {
        
        ShowProgress(this);
        incomeCropLocation(CropId).orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Map<String, Object> CloudItem = documentSnapshot.getData();
                    
                    SingleIncomeCropData item = new SingleIncomeCropData(
                           CloudItem.get(ID).toString(),
                           CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT), 
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER)
                    );
                    
                    SingleFourColumnsList iitem = new SingleFourColumnsList(
                            DateToString(item.getIncomeDate()),
                            item.getIncomeRemark(),
                            String.valueOf(item.getIncomeAmount()),
                            item.getBuyerCropsName());
                    
                    long pC = (Long) CloudItem.get(PARTNER_COUNT);
                    int PC = (int) pC;
                    
                    switch (PC){
                        case 1 : item = new SingleIncomeCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
  CloudItem.get(CROP_ID).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(RATE),
                                (Double) CloudItem.get(WEIGHT),
                                (Double) CloudItem.get(SELF_INCOME),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AREA_OF_LAND),
                                (Boolean) CloudItem.get(CASH_MODE),
                                CloudItem.get(BUYER_NAME).toString(),
                                CloudItem.get(BUYER_ID).toString(),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                (Boolean) CloudItem.get(PARTNER),
                                (Double) CloudItem.get(PARTNER_AMOUNT_1),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                CloudItem.get(PARTNER_ID_1).toString()
                        );
                        break;
                        
                        case 2 : item = new SingleIncomeCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
  CloudItem.get(CROP_ID).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(RATE),
                                (Double) CloudItem.get(WEIGHT),
                                (Double) CloudItem.get(SELF_INCOME),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AREA_OF_LAND),
                                (Boolean) CloudItem.get(CASH_MODE),
                                CloudItem.get(BUYER_NAME).toString(),
                                CloudItem.get(BUYER_ID).toString(),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                (Boolean) CloudItem.get(PARTNER),
                                (Double) CloudItem.get(PARTNER_AMOUNT_1),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_2),
                                (Double) CloudItem.get(PARTNER_SHARE_2),
                                CloudItem.get(PARTNER_NAME_2).toString(),
                                CloudItem.get(PARTNER_ID_2).toString()
                        );
                            break;
                            
                        case 3 : item = new SingleIncomeCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
  CloudItem.get(CROP_ID).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(RATE),
                                (Double) CloudItem.get(WEIGHT),
                                (Double) CloudItem.get(SELF_INCOME),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AREA_OF_LAND),
                                (Boolean) CloudItem.get(CASH_MODE),
                                CloudItem.get(BUYER_NAME).toString(),
                                CloudItem.get(BUYER_ID).toString(),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                (Boolean) CloudItem.get(PARTNER),
                                (Double) CloudItem.get(PARTNER_AMOUNT_1),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_2),
                                (Double) CloudItem.get(PARTNER_SHARE_2),
                                CloudItem.get(PARTNER_NAME_2).toString(),
                                CloudItem.get(PARTNER_ID_2).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_3),
                                (Double) CloudItem.get(PARTNER_SHARE_3),
                                CloudItem.get(PARTNER_NAME_3).toString(),
                                CloudItem.get(PARTNER_ID_3).toString()
                        );
                            break;
                            
                        case 4 : item = new SingleIncomeCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
  CloudItem.get(CROP_ID).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(RATE),
                                (Double) CloudItem.get(WEIGHT),
                                (Double) CloudItem.get(SELF_INCOME),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AREA_OF_LAND),
                                (Boolean) CloudItem.get(CASH_MODE),
                                CloudItem.get(BUYER_NAME).toString(),
                                CloudItem.get(BUYER_ID).toString(),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                (Boolean) CloudItem.get(PARTNER),
                                (Double) CloudItem.get(PARTNER_AMOUNT_1),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_2),
                                (Double) CloudItem.get(PARTNER_SHARE_2),
                                CloudItem.get(PARTNER_NAME_2).toString(),
                                CloudItem.get(PARTNER_ID_2).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_3),
                                (Double) CloudItem.get(PARTNER_SHARE_3),
                                CloudItem.get(PARTNER_NAME_3).toString(),
                                CloudItem.get(PARTNER_ID_3).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_4),
                                (Double) CloudItem.get(PARTNER_SHARE_4),
                                CloudItem.get(PARTNER_NAME_4).toString(),
                                CloudItem.get(PARTNER_ID_4).toString()
                        );
                            break;
                            
                        case 5 :  item = new SingleIncomeCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
  CloudItem.get(CROP_ID).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(RATE),
                                (Double) CloudItem.get(WEIGHT),
                                (Double) CloudItem.get(SELF_INCOME),
                                (Date) CloudItem.get(DATE),
                                (Double) CloudItem.get(AREA_OF_LAND),
                                (Boolean) CloudItem.get(CASH_MODE),
                                CloudItem.get(BUYER_NAME).toString(),
                                CloudItem.get(BUYER_ID).toString(),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                (Boolean) CloudItem.get(PARTNER),
                                (Double) CloudItem.get(PARTNER_AMOUNT_1),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_2),
                                (Double) CloudItem.get(PARTNER_SHARE_2),
                                CloudItem.get(PARTNER_NAME_2).toString(),
                                CloudItem.get(PARTNER_ID_2).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_3),
                                (Double) CloudItem.get(PARTNER_SHARE_3),
                                CloudItem.get(PARTNER_NAME_3).toString(),
                                CloudItem.get(PARTNER_ID_3).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_4),
                                (Double) CloudItem.get(PARTNER_SHARE_4),
                                CloudItem.get(PARTNER_NAME_4).toString(),
                                CloudItem.get(PARTNER_ID_4).toString(),
                                (Double) CloudItem.get(PARTNER_AMOUNT_5),
                                (Double) CloudItem.get(PARTNER_SHARE_5),
                                CloudItem.get(PARTNER_NAME_5).toString(),
                                CloudItem.get(PARTNER_ID_5).toString()
                        );
                            break;

                    }

                    FourList.add(iitem);
                    IncomeList.add(item);
                }


                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }
//    public void FilterIncomeHistory(View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_for_income,null);
//
//        AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
//        AutoCompleteTextView BuyerFilter = customFilterView.findViewById(R.id.buyer_filter);
//
//        AutoCompleteTextView PartnerFilter = customFilterView.findViewById(R.id.partner_filter);
//        EditText PartnerPercentage = customFilterView.findViewById(R.id.sharing_percentage_filter);
//
//        RadioGroup FilterModeOfPayment = customFilterView.findViewById(R.id.filter_mode_of_payment);
//        RadioButton FilterCashMode = customFilterView.findViewById(R.id.filter_cash_mode);
//        RadioButton FilterBorrowMode = customFilterView.findViewById(R.id.filter_borrow_mode);
//
//        EditText MinAmountFilter = customFilterView.findViewById(R.id.min_amount_filter);
//        EditText MaxAmountFilter = customFilterView.findViewById(R.id.max_amount_filter);
//
//        EditText MinRateFilter = customFilterView.findViewById(R.id.min_rate_filter);
//        EditText MaxRateFilter = customFilterView.findViewById(R.id.max_rate_filter);
//
//        RadioGroup DateFilter = customFilterView.findViewById(R.id.date_filter);
//        RadioButton SingleDateFilter = customFilterView.findViewById(R.id.single_date_filter);
//        RadioButton BetweenDateFilter = customFilterView.findViewById(R.id.between_date_filter);
//
//        LinearLayout SingleDateContainer = customFilterView.findViewById(R.id.single_date_filter_container);
//        Button SingleChooseDateFilter = customFilterView.findViewById(R.id.single_choose_date_filter);
//        final TextView SingleShowDateFilter = customFilterView.findViewById(R.id.single_show_date_filter);
//
//        final ArrayList<Boolean> singleChoose = new ArrayList<>();
//        singleChoose.add(false);
//
//        SingleChooseDateFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                singleChoose.add(0,ChooseDateDialogSingle(IncomeCropHistory.this,SingleShowDateFilter));
//            }
//        });
//
//        LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
//        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
//        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);
//
//        final ArrayList<Boolean> fromChoose = new ArrayList<>();
//        fromChoose.add(false);
//
//        FromChooseDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fromChoose.add(0,ChooseDateDialogFrom(IncomeCropHistory.this,FromShowDate));
//            }
//        });
//
//        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
//        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);
//
//        final ArrayList<Boolean> toChoose = new ArrayList<>();
//        toChoose.add(false);
//
//        ToChooseDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toChoose.add(0,ChooseDateDialogTo(IncomeCropHistory.this,ToShowDate));
//            }
//        });
//
//
//        Button FilterBt = customFilterView.findViewById(R.id.filter_bt);
//
//        FilterBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        builder.setView(customFilterView);
//
//        builder.create().show();
//    }


    private void addData() {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataCash(boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataPartner(String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataPartnerCash(String partner, boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataBuyer(String buyer) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(BUYER_NAME, buyer)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataBuyerCash(String buyer, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(BUYER_NAME, buyer)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataBuyerPartner(String buyer, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataBuyerPartnerCash(String buyer, String partner,boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataRemark(String remark) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkCash(String remark, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataRemarkPartner(String remark, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataRemarkPartnerCash(String remark, String partner, boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataRemarkBuyer(String remark, String buyer) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(BUYER_NAME, buyer)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkBuyerCash(String remark, String buyer, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(BUYER_NAME, buyer)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataRemarkBuyerPartner(String remark, String buyer, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataRemarkBuyerPartnerCash(String remark, String buyer, String partner,boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataAmount(double minAmount, double maxAmount) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountCash(double minAmount, double maxAmount,boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataAmountPartner(double minAmount, double maxAmount,String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataAmountPartnerCash(double minAmount, double maxAmount,String partner, boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataAmountBuyer(double minAmount, double maxAmount, String buyer) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountBuyerCash(double minAmount, double maxAmount, String buyer, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataAmountBuyerPartner(double minAmount, double maxAmount, String buyer, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataAmountBuyerPartnerCash(double minAmount, double maxAmount, String buyer, String partner,boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataAmountRemark(double minAmount, double maxAmount, String remark) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkCash(double minAmount, double maxAmount, String remark, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataAmountRemarkPartner(double minAmount, double maxAmount, String remark, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataAmountRemarkPartnerCash(double minAmount, double maxAmount, String remark, String partner, boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataAmountRemarkBuyer(double minAmount, double maxAmount, String remark, String buyer) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkBuyerCash(double minAmount, double maxAmount, String remark, String buyer, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataAmountRemarkBuyerPartner(double minAmount, double maxAmount, String remark, String buyer, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataAmountRemarkBuyerPartnerCash(double minAmount, double maxAmount, String remark, String buyer, String partner,boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataDate(Date date1, Date date2) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateCash(Date date1, Date date2,boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataDatePartner(Date date1, Date date2,String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereGreaterThanOrEqualTo(DATE, date1)
//                .whereLessThanOrEqualTo(DATE, date2)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataDatePartnerCash(Date date1, Date date2,String partner, boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereGreaterThanOrEqualTo(DATE, date1)
//                .whereLessThanOrEqualTo(DATE, date2)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataDateBuyer(Date date1, Date date2, String buyer) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateBuyerCash(Date date1, Date date2, String buyer, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataDateBuyerPartner(Date date1, Date date2, String buyer, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(DATE, date1)
//                .whereLessThanOrEqualTo(DATE, date2)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataDateBuyerPartnerCash(Date date1, Date date2, String buyer, String partner,boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(DATE, date1)
//                .whereLessThanOrEqualTo(DATE, date2)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataDateRemark(Date date1, Date date2, String remark) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkCash(Date date1, Date date2, String remark, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataDateRemarkPartner(Date date1, Date date2, String remark, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereGreaterThanOrEqualTo(DATE, date1)
//                .whereLessThanOrEqualTo(DATE, date2)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataDateRemarkPartnerCash(Date date1, Date date2, String remark, String partner, boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereGreaterThanOrEqualTo(DATE, date1)
//                .whereLessThanOrEqualTo(DATE, date2)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataDateRemarkBuyer(Date date1, Date date2, String remark, String buyer) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkBuyerCash(Date date1, Date date2, String remark, String buyer, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataDateRemarkBuyerPartner(Date date1, Date date2, String remark, String buyer, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(DATE, date1)
//                .whereLessThanOrEqualTo(DATE, date2)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataDateRemarkBuyerPartnerCash(Date date1, Date date2, String remark, String buyer, String partner,boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(DATE, date1)
//                .whereLessThanOrEqualTo(DATE, date2)
//                .orderBy(DATE, Query.Direction.DESCENDING).get()
//                .addOnSuccessListener(this, myListener)
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataDateAmount(final Date date1, final Date date2, double minAmount, double maxAmount) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dateWithAPro(queryDocumentSnapshots,date1,date2);
                    }
                })
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateAmountCash(final Date date1, final Date date2 , double minAmount, double maxAmount, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dateWithAPro(queryDocumentSnapshots,date1,date2);
                    }
                })
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataDateAmountPartner(final Date date1, final Date date2 , double minAmount, double maxAmount,String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        dateWithAPro(queryDocumentSnapshots,date1,date2);
//                    }
//                })
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataDateAmountPartnerCash(final Date date1, final Date date2 , double minAmount, double maxAmount,String partner, boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        dateWithAPro(queryDocumentSnapshots,date1,date2);
//                    }
//                })
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataDateAmountBuyer(final Date date1, final Date date2 , double minAmount, double maxAmount, String buyer) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dateWithAPro(queryDocumentSnapshots,date1,date2);
                    }
                })
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateAmountBuyerCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String buyer, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dateWithAPro(queryDocumentSnapshots,date1,date2);
                    }
                })
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataDateAmountBuyerPartner(final Date date1, final Date date2 , double minAmount, double maxAmount, String buyer, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        dateWithAPro(queryDocumentSnapshots,date1,date2);
//                    }
//                })
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataDateAmountBuyerPartnerCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String buyer, String partner,boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        dateWithAPro(queryDocumentSnapshots,date1,date2);
//                    }
//                })
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataDateAmountRemark(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dateWithAPro(queryDocumentSnapshots,date1,date2);
                    }
                })
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateAmountRemarkCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dateWithAPro(queryDocumentSnapshots,date1,date2);
                    }
                })
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataDateAmountRemarkPartner(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        dateWithAPro(queryDocumentSnapshots,date1,date2);
//                    }
//                })
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataDateAmountRemarkPartnerCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String partner, boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        dateWithAPro(queryDocumentSnapshots,date1,date2);
//                    }
//                })
//                .addOnFailureListener(this, myFailData);
//    }
    private void addDataDateAmountRemarkBuyer(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String buyer) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dateWithAPro(queryDocumentSnapshots,date1,date2);
                    }
                })
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateAmountRemarkBuyerCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String buyer, boolean cashMode) {
        ShowProgress(this);
        incomeCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(BUYER_NAME, buyer)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dateWithAPro(queryDocumentSnapshots,date1,date2);
                    }
                })
                .addOnFailureListener(this, myFailData);
    }
//    private void addDataDateAmountRemarkBuyerPartner(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String buyer, String partner) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        dateWithAPro(queryDocumentSnapshots,date1,date2);
//                    }
//                })
//                .addOnFailureListener(this, myFailData);
//    }
//    private void addDataDateAmountRemarkBuyerPartnerCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String buyer, String partner,boolean cashMode) {
//        ShowProgress(this);
//        incomeCropLocation(CropId)
//                .whereEqualTo(REMARK,remark)
//                .whereEqualTo(CASH_MODE,cashMode)
//                .whereEqualTo(PARTNER_NAME,partner)
//                .whereEqualTo(BUYER_NAME, buyer)
//                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
//                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
//                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
//                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        dateWithAPro(queryDocumentSnapshots,date1,date2);
//                    }
//                })
//                .addOnFailureListener(this, myFailData);
//    }






    @Override
    protected void onStart() {
        super.onStart();

        addData();

        RemarkList.clear();

        incomeCropRemark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buyerList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    BuyerList = (ArrayList<String>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        

    }

    ArrayList<String> RemarkList = new ArrayList<>();
    ArrayList<String> BuyerList = new ArrayList<>();

    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;

    String minAmountFilterSt;
    String maxAmountFilterSt;

    String remarkFilterSt;
    String buyerFilterSt;

    boolean cashModeFilter;



    public void FilterIncomeHistory(View v){

        singleDate = false;
        betweenDate =false;
        singleChooseDate = false;
        fromChooseDate = false;
        toChooseDate = false;


        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_for_income,null);

        final AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
        setSnipper(this,RemarkFilter,RemarkList);
        final AutoCompleteTextView BuyerFilter = customFilterView.findViewById(R.id.buyer_filter);
        setSnipper(this,BuyerFilter, BuyerList);

        final RadioGroup FilterModeOfPayment = customFilterView.findViewById(R.id.filter_mode_of_payment);
        RadioButton FilterCashMode = customFilterView.findViewById(R.id.filter_cash_mode);
        RadioButton FilterBorrowMode = customFilterView.findViewById(R.id.filter_borrow_mode);

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
                singleChooseDate = ChooseDateDialogSingle(IncomeCropHistory.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(IncomeCropHistory.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(IncomeCropHistory.this,ToShowDate);
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

                remarkFilterSt = RemarkFilter.getText().toString();
                buyerFilterSt = BuyerFilter.getText().toString();

                boolean dF, aF, rF, bF, cF;

                int CheckId = FilterModeOfPayment.getCheckedRadioButtonId();
                switch (CheckId){
                    case R.id.filter_cash_mode:
                        cashModeFilter = true;
                        cF = false;
                        break;
                    case R.id.filter_borrow_mode:
                        cashModeFilter = false;
                        cF = false;
                        break;
                    default:
                        cF = true;
                }

                if (minAmountFilterSt.isEmpty()&&!maxAmountFilterSt.isEmpty()){
                    MinAmountFilter.setText(R.string.default_min_amount);
                    minAmountFilterSt = getString(R.string.default_min_amount);
                }
                if (maxAmountFilterSt.isEmpty()&&!minAmountFilterSt.isEmpty()){
                    MaxAmountFilter.setText(R.string.default_max_amount);
                    maxAmountFilterSt = getString(R.string.default_max_amount);
                }

                dF = !((singleDate&&singleChooseDate)||(betweenDate&&fromChooseDate&&toChooseDate));
                aF = (minAmountFilterSt.isEmpty()||maxAmountFilterSt.isEmpty());
                rF = remarkFilterSt.isEmpty();
                bF = buyerFilterSt.isEmpty();

                String fr,fb;
                double fmina,fmaxa;
                boolean fc;
                // Change Name To short Form
                fr = remarkFilterSt;
                fb = buyerFilterSt;

                fc = cashModeFilter;

                if(dF){
                    if (aF){
                        if (rF){
                            /*
                            CashMode
                             */
                            if (bF&&!cF){
                                addDataCash(fc);
                            }
                            
                            /*
                            Buyer
                             */
                            else if (!bF&&cF){
                                addDataBuyer(fb);
                            }
                            
                            /*
                            Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataBuyerCash(fb,fc);
                            }
                        }
                        else{
                            /*
                            Remark
                             */
                            if(bF&&cF){
                                addDataRemark(fr);
                            }
                            
                            /*
                            Remark CashMode
                             */
                            else if (bF&&!cF){
                                addDataRemarkCash(fr,fc);
                            }
                            
                            /*
                            Remark Buyer
                             */
                            else if (!bF&&cF){
                                addDataRemarkBuyer(fr,fb);
                            }
                            
                            /*
                            Remark Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataRemarkBuyerCash(fr,fb,fc);
                            }
                        }
                    }
                    else{
                        fmina = Double.parseDouble(minAmountFilterSt);
                        fmaxa = Double.parseDouble(maxAmountFilterSt);
                        
                        if (rF){
                            /*
                            Amount
                             */
                            if (bF&&cF){
                                addDataAmount(fmina, fmaxa);
                            }
                            
                            /*
                            Amount and CashMode
                             */
                            if (bF&&!cF){
                                addDataAmountCash(fmina,fmaxa,fc);
                            }
                            
                            /*
                            Amount and Buyer
                             */
                            else if (!bF&&cF){
                                addDataAmountBuyer(fmina,fmaxa,fb);
                            }
                            
                            /*
                            Amount and Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataAmountBuyerCash(fmina,fmaxa,fb,fc);
                            }
                        }
                        else{
                            /*
                            Amount and Remark
                             */
                            if(bF&&cF){
                                addDataAmountRemark(fmina,fmaxa, fr);
                            }
                            
                            /*
                            Amount and Remark CashMode
                             */
                            else if (bF&&!cF){
                                addDataAmountRemarkCash(fmina,fmaxa, fr,fc);
                            }
                            
                            /*
                            Amount and Remark Buyer
                             */
                            else if (!bF&&cF){
                                addDataAmountRemarkBuyer(fmina,fmaxa, fr,fb);
                            }
                            
                            /*
                            Amount and Remark Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataAmountRemarkBuyerCash(fmina,fmaxa, fr,fb,fc);
                            }
                        }
                    }
                }
                else if (singleDate&&singleChooseDate){
                    if (aF){
                        if (rF){
                            
                            /*
                            Single Date
                             */
                            if (bF&&cF){
                                addDataDate(singleDateStatic,singleDateStaticTill);
                            }
                            
                            /*
                            Single Date and CashMode
                             */
                            else if (bF&&!cF){
                                addDataDateCash(singleDateStatic,singleDateStaticTill,fc);
                            }
                            
                            /*
                            Single Date and Buyer
                             */
                            else if (!bF&&cF){
                                addDataDateBuyer(singleDateStatic,singleDateStaticTill,fb);
                            }
                            
                            /*
                            Single Date and Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataDateBuyerCash(singleDateStatic,singleDateStaticTill,fb,fc);
                            }
                        }
                        else{
                            /*
                            Single Date and Remark
                             */
                            if(bF&&cF){
                                addDataDateRemark(singleDateStatic, singleDateStaticTill, fr);
                            }
                            
                            /*
                            Single Date and Remark CashMode
                             */
                            else if (bF&&!cF){
                                addDataDateRemarkCash(singleDateStatic, singleDateStaticTill, fr,fc);
                            }
                            
                            /*
                            Single Date and Remark Buyer
                             */
                            else if (!bF&&cF){
                                addDataDateRemarkBuyer(singleDateStatic, singleDateStaticTill, fr,fb);
                            }
                            
                            /*
                            Single Date and Remark Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataDateRemarkBuyerCash(singleDateStatic, singleDateStaticTill, fr,fb,fc);
                            }
                        }
                    }
                    else{
                        fmina = Double.parseDouble(minAmountFilterSt);
                        fmaxa = Double.parseDouble(maxAmountFilterSt);

                        if (rF){
                            /*
                            Single Date and Amount
                             */
                            if (bF&&cF){
                                addDataDateAmount(singleDateStatic,singleDateStaticTill,fmina, fmaxa);
                            }
                            
                            /*
                            Single Date and Amount and CashMode
                             */
                            if (bF&&!cF){
                                addDataDateAmountCash(singleDateStatic,singleDateStaticTill, fmina,fmaxa,fc);
                            }
                            
                            /*
                            Single Date and Amount and Buyer
                             */
                            else if (!bF&&cF){
                                addDataDateAmountBuyer(singleDateStatic,singleDateStaticTill, fmina,fmaxa,fb);
                            }
                            
                            /*
                            Single Date and Amount and Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataDateAmountBuyerCash(singleDateStatic,singleDateStaticTill, fmina,fmaxa,fb,fc);
                            }
                        }
                        else{
                            /*
                            Single Date and Amount and Remark
                             */
                            if(bF&&cF){
                                addDataDateAmountRemark(singleDateStatic,singleDateStaticTill, fmina,fmaxa, fr);
                            }
                            
                            /*
                            Single Date and Amount and Remark CashMode
                             */
                            else if (bF&&!cF){
                                addDataDateAmountRemarkCash(singleDateStatic,singleDateStaticTill, fmina,fmaxa, fr,fc);
                            }
                            
                            /*
                            Single Date and Amount and Remark Buyer
                             */
                            else if (!bF&&cF){
                                addDataDateAmountRemarkBuyer(singleDateStatic,singleDateStaticTill, fmina,fmaxa, fr,fb);
                            }
                            
                            /*
                            Single Date and Amount and Remark Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataDateAmountRemarkBuyerCash(singleDateStatic,singleDateStaticTill, fmina,fmaxa, fr,fb,fc);
                            }
                        }
                    }
                }
                else if((betweenDate&&fromChooseDate&&toChooseDate)){
                    if (aF){
                        if (rF){
                            
                            /*
                            Between Date
                             */
                            if (bF&&cF){
                                addDataDate(fromDateStatic, toDateStatic);
                            }
                            
                            /*
                            Between Date and CashMode
                             */
                            else if (bF&&!cF){
                                addDataDateCash(fromDateStatic, toDateStatic,fc);
                            }
                            
                            /*
                            Between Date and Buyer
                             */
                            else if (!bF&&cF){
                                addDataDateBuyer(fromDateStatic, toDateStatic,fb);
                            }
                            
                            /*
                            Between Date and Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataDateBuyerCash(fromDateStatic, toDateStatic,fb,fc);
                            }
                        }
                        else{
                            /*
                            Between Date and Remark
                             */
                            if(bF&&cF){
                                addDataDateRemark(fromDateStatic, toDateStatic, fr);
                            }
                            
                            /*
                            Between Date and Remark CashMode
                             */
                            else if (bF&&!cF){
                                addDataDateRemarkCash(fromDateStatic, toDateStatic, fr,fc);
                            }
                            
                            /*
                            Between Date and Remark Buyer
                             */
                            else if (!bF&&cF){
                                addDataDateRemarkBuyer(fromDateStatic, toDateStatic, fr,fb);
                            }
                            
                            /*
                            Between Date and Remark Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataDateRemarkBuyerCash(fromDateStatic, toDateStatic, fr,fb,fc);
                            }
                        }
                    }
                    else{
                        fmina = Double.parseDouble(minAmountFilterSt);
                        fmaxa = Double.parseDouble(maxAmountFilterSt);

                        if (rF){
                            /*
                            Between Date and Amount
                             */
                            if (bF&&cF){
                                addDataDateAmount(fromDateStatic, toDateStatic,fmina, fmaxa);
                            }
                            
                            /*
                            Between Date and Amount and CashMode
                             */
                            if (bF&&!cF){
                                addDataDateAmountCash(fromDateStatic, toDateStatic, fmina,fmaxa,fc);
                            }
                            
                            /*
                            Between Date and Amount and Buyer
                             */
                            else if (!bF&&cF){
                                addDataDateAmountBuyer(fromDateStatic, toDateStatic, fmina,fmaxa,fb);
                            }
                            
                            /*
                            Between Date and Amount and Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataDateAmountBuyerCash(fromDateStatic, toDateStatic, fmina,fmaxa,fb,fc);
                            }
                        }
                        else{
                            /*
                            Between Date and Amount and Remark
                             */
                            if(bF&&cF){
                                addDataDateAmountRemark(fromDateStatic, toDateStatic, fmina,fmaxa, fr);
                            }
                            
                            /*
                            Between Date and Amount and Remark CashMode
                             */
                            else if (bF&&!cF){
                                addDataDateAmountRemarkCash(fromDateStatic, toDateStatic, fmina,fmaxa, fr,fc);
                            }
                            
                            /*
                            Between Date and Amount and Remark Buyer
                             */
                            else if (!bF&&cF){
                                addDataDateAmountRemarkBuyer(fromDateStatic, toDateStatic, fmina,fmaxa, fr,fb);
                            }
                            
                            /*
                            Between Date and Amount and Remark Buyer and CashMode
                             */
                            if (!bF&&!cF){
                                addDataDateAmountRemarkBuyerCash(fromDateStatic, toDateStatic, fmina,fmaxa, fr,fb,fc);
                            }
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
        ShowDialog(IncomeCropHistory.this,getString(R.string.please_fill_correct_value));
    }

    public void dateWithAPro(QuerySnapshot queryDocumentSnapshots, Date date1, Date date2){
        
        
            IncomeList.clear();
            FourList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                if (!((documentSnapshot.getDate(DATE).after(date1)
                        ||documentSnapshot.getDate(DATE).equals(date1))
                        &&(documentSnapshot.getDate(DATE).before(date2)
                        ||documentSnapshot.getDate(DATE).equals(date2)))) {
                    continue;
                }
                
                Map<String, Object> CloudItem = documentSnapshot.getData();

                SingleIncomeCropData item = new SingleIncomeCropData(
                        CloudItem.get(ID).toString(),
                        CloudItem.get(REMARK).toString(),
                        CloudItem.get(CROP_ID).toString(),
                        (Double) CloudItem.get(AMOUNT),
                        (Double) CloudItem.get(RATE),
                        (Double) CloudItem.get(WEIGHT),
                        (Double) CloudItem.get(SELF_INCOME),
                        (Date) CloudItem.get(DATE),
                        (Double) CloudItem.get(AREA_OF_LAND),
                        (Boolean) CloudItem.get(CASH_MODE),
                        CloudItem.get(BUYER_NAME).toString(),
                        CloudItem.get(BUYER_ID).toString(),
                        (Long) CloudItem.get(PARTNER_COUNT),
                        (Boolean) CloudItem.get(PARTNER)
                );

                SingleFourColumnsList iitem = new SingleFourColumnsList(
                        DateToString(item.getIncomeDate()),
                        item.getIncomeRemark(),
                        String.valueOf(item.getIncomeAmount()),
                        item.getBuyerCropsName());

                long pC = (Long) CloudItem.get(PARTNER_COUNT);
                int PC = (int) pC;

                switch (PC){
                    case 1 : item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString()
                    );
                        break;

                    case 2 : item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_2),
                            (Double) CloudItem.get(PARTNER_SHARE_2),
                            CloudItem.get(PARTNER_NAME_2).toString(),
                            CloudItem.get(PARTNER_ID_2).toString()
                    );
                        break;

                    case 3 : item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_2),
                            (Double) CloudItem.get(PARTNER_SHARE_2),
                            CloudItem.get(PARTNER_NAME_2).toString(),
                            CloudItem.get(PARTNER_ID_2).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_3),
                            (Double) CloudItem.get(PARTNER_SHARE_3),
                            CloudItem.get(PARTNER_NAME_3).toString(),
                            CloudItem.get(PARTNER_ID_3).toString()
                    );
                        break;

                    case 4 : item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_2),
                            (Double) CloudItem.get(PARTNER_SHARE_2),
                            CloudItem.get(PARTNER_NAME_2).toString(),
                            CloudItem.get(PARTNER_ID_2).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_3),
                            (Double) CloudItem.get(PARTNER_SHARE_3),
                            CloudItem.get(PARTNER_NAME_3).toString(),
                            CloudItem.get(PARTNER_ID_3).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_4),
                            (Double) CloudItem.get(PARTNER_SHARE_4),
                            CloudItem.get(PARTNER_NAME_4).toString(),
                            CloudItem.get(PARTNER_ID_4).toString()
                    );
                        break;

                    case 5 :  item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_2),
                            (Double) CloudItem.get(PARTNER_SHARE_2),
                            CloudItem.get(PARTNER_NAME_2).toString(),
                            CloudItem.get(PARTNER_ID_2).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_3),
                            (Double) CloudItem.get(PARTNER_SHARE_3),
                            CloudItem.get(PARTNER_NAME_3).toString(),
                            CloudItem.get(PARTNER_ID_3).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_4),
                            (Double) CloudItem.get(PARTNER_SHARE_4),
                            CloudItem.get(PARTNER_NAME_4).toString(),
                            CloudItem.get(PARTNER_ID_4).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_5),
                            (Double) CloudItem.get(PARTNER_SHARE_5),
                            CloudItem.get(PARTNER_NAME_5).toString(),
                            CloudItem.get(PARTNER_ID_5).toString()
                    );
                        break;

                }

                FourList.add(iitem);
                IncomeList.add(item);
            }


            myAdapter.notifyDataSetChanged();
            HideProgress();
    }

    OnSuccessListener<QuerySnapshot> myListener = new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            IncomeList.clear();
            FourList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                Map<String, Object> CloudItem = documentSnapshot.getData();

                SingleIncomeCropData item = new SingleIncomeCropData(
                        CloudItem.get(ID).toString(),
                        CloudItem.get(REMARK).toString(),
                        CloudItem.get(CROP_ID).toString(),
                        (Double) CloudItem.get(AMOUNT),
                        (Double) CloudItem.get(RATE),
                        (Double) CloudItem.get(WEIGHT),
                        (Double) CloudItem.get(SELF_INCOME),
                        (Date) CloudItem.get(DATE),
                        (Double) CloudItem.get(AREA_OF_LAND),
                        (Boolean) CloudItem.get(CASH_MODE),
                        CloudItem.get(BUYER_NAME).toString(),
                        CloudItem.get(BUYER_ID).toString(),
                        (Long) CloudItem.get(PARTNER_COUNT),
                        (Boolean) CloudItem.get(PARTNER)
                );

                SingleFourColumnsList iitem = new SingleFourColumnsList(
                        DateToString(item.getIncomeDate()),
                        item.getIncomeRemark(),
                        String.valueOf(item.getIncomeAmount()),
                        item.getBuyerCropsName());

                long pC = (Long) CloudItem.get(PARTNER_COUNT);
                int PC = (int) pC;

                switch (PC){
                    case 1 : item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString()
                    );
                        break;

                    case 2 : item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_2),
                            (Double) CloudItem.get(PARTNER_SHARE_2),
                            CloudItem.get(PARTNER_NAME_2).toString(),
                            CloudItem.get(PARTNER_ID_2).toString()
                    );
                        break;

                    case 3 : item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_2),
                            (Double) CloudItem.get(PARTNER_SHARE_2),
                            CloudItem.get(PARTNER_NAME_2).toString(),
                            CloudItem.get(PARTNER_ID_2).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_3),
                            (Double) CloudItem.get(PARTNER_SHARE_3),
                            CloudItem.get(PARTNER_NAME_3).toString(),
                            CloudItem.get(PARTNER_ID_3).toString()
                    );
                        break;

                    case 4 : item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_2),
                            (Double) CloudItem.get(PARTNER_SHARE_2),
                            CloudItem.get(PARTNER_NAME_2).toString(),
                            CloudItem.get(PARTNER_ID_2).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_3),
                            (Double) CloudItem.get(PARTNER_SHARE_3),
                            CloudItem.get(PARTNER_NAME_3).toString(),
                            CloudItem.get(PARTNER_ID_3).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_4),
                            (Double) CloudItem.get(PARTNER_SHARE_4),
                            CloudItem.get(PARTNER_NAME_4).toString(),
                            CloudItem.get(PARTNER_ID_4).toString()
                    );
                        break;

                    case 5 :  item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(CROP_ID).toString(),
                            (Double) CloudItem.get(AMOUNT),
                            (Double) CloudItem.get(RATE),
                            (Double) CloudItem.get(WEIGHT),
                            (Double) CloudItem.get(SELF_INCOME),
                            (Date) CloudItem.get(DATE),
                            (Double) CloudItem.get(AREA_OF_LAND),
                            (Boolean) CloudItem.get(CASH_MODE),
                            CloudItem.get(BUYER_NAME).toString(),
                            CloudItem.get(BUYER_ID).toString(),
                            (Long) CloudItem.get(PARTNER_COUNT),
                            (Boolean) CloudItem.get(PARTNER),
                            (Double) CloudItem.get(PARTNER_AMOUNT_1),
                            (Double) CloudItem.get(PARTNER_SHARE_1),
                            CloudItem.get(PARTNER_NAME_1).toString(),
                            CloudItem.get(PARTNER_ID_1).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_2),
                            (Double) CloudItem.get(PARTNER_SHARE_2),
                            CloudItem.get(PARTNER_NAME_2).toString(),
                            CloudItem.get(PARTNER_ID_2).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_3),
                            (Double) CloudItem.get(PARTNER_SHARE_3),
                            CloudItem.get(PARTNER_NAME_3).toString(),
                            CloudItem.get(PARTNER_ID_3).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_4),
                            (Double) CloudItem.get(PARTNER_SHARE_4),
                            CloudItem.get(PARTNER_NAME_4).toString(),
                            CloudItem.get(PARTNER_ID_4).toString(),
                            (Double) CloudItem.get(PARTNER_AMOUNT_5),
                            (Double) CloudItem.get(PARTNER_SHARE_5),
                            CloudItem.get(PARTNER_NAME_5).toString(),
                            CloudItem.get(PARTNER_ID_5).toString()
                    );
                        break;

                }

                FourList.add(iitem);
                IncomeList.add(item);
            }


            myAdapter.notifyDataSetChanged();
            HideProgress();
        }
    };


    OnFailureListener myFailData = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

            HideProgress();
            Log.d("Firestore", "Index : "+e.toString());
            System.out.println(e.toString());
        }
    };

}
