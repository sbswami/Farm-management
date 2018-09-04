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
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AREA_OF_LAND;
import static com.sanshy.farmmanagement.MyStatic.BUYER;
import static com.sanshy.farmmanagement.MyStatic.BUYER_ID;
import static com.sanshy.farmmanagement.MyStatic.BUYER_NAME;
import static com.sanshy.farmmanagement.MyStatic.CASH_MODE;
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
import static com.sanshy.farmmanagement.MyStatic.RATE;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SELF_INCOME;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.WEIGHT;
import static com.sanshy.farmmanagement.MyStatic.incomeCropLocation;
import static com.sanshy.farmmanagement.MyStatic.lessAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.lessAllReport;
import static com.sanshy.farmmanagement.MyStatic.lessSingleReport;
import static com.sanshy.farmmanagement.MyStatic.purchaseBuyerLocation;
import static com.sanshy.farmmanagement.MyStatic.removeRemainingAmountBuyer;

public class HistoryOfSingleBuyerPurchase extends AppCompatActivity {


    ListView HistoryList;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();
    ArrayList<SingleIncomeCropData> IncomeList = new ArrayList<>();
    SingleFourColumnsListAdapter myAdapter;

    String BuyerName;
    String BuyerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisotory_of_single_buyer_purchase);

        Intent intent = getIntent();

        BuyerName = intent.getStringExtra(BUYER_NAME);
        BuyerId = intent.getStringExtra(BUYER_ID);

        HistoryList = findViewById(R.id.buyer_history_list);

        myAdapter = new SingleFourColumnsListAdapter(this,FourList);

        HistoryList.setAdapter(myAdapter);

        HistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                SingleIncomeCropData List = IncomeList.get(position);
                final String Id = List.getIncomeId();
                final String CropId = List.getCropId();

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HistoryOfSingleBuyerPurchase.this);

                LayoutInflater inflater = (LayoutInflater) HistoryOfSingleBuyerPurchase.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                PaymentMode.setText(List.isCashBuyer() ?HistoryOfSingleBuyerPurchase.this.getString(R.string.cash):HistoryOfSingleBuyerPurchase.this.getString(R.string.borrow));
                RatePerWeight.setText(String.valueOf(List.getIncomeCropRate()));
                Weight.setText(String.valueOf(List.getIncomeCropWeight()));
                MyIncome.setText(String.valueOf(List.getSelfShareAmount()));


                builder.setView(myView)
                        .setPositiveButton(HistoryOfSingleBuyerPurchase.this.getString(R.string.ok), null);

                builder.setNegativeButton(HistoryOfSingleBuyerPurchase.this.getString(R.string.undo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(HistoryOfSingleBuyerPurchase.this);

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

        AddHistoryList();
    }

    private void AddHistoryList() {

        ShowProgress(this);
        purchaseBuyerLocation(BuyerId).orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Map<String, Object> CloudItem = documentSnapshot.getData();

                    SingleIncomeCropData item = new SingleIncomeCropData(
                            CloudItem.get(ID).toString(),
                            CloudItem.get(REMARK).toString(),
                            CloudItem.get(MyStatic.CROP_ID).toString(),
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

                    int indexOfC = item.getCropId().indexOf("_");

                    SingleFourColumnsList iitem = new SingleFourColumnsList(
                            DateToString(item.getIncomeDate()),
                            item.getCropId().substring(0,indexOfC),
                            item.getIncomeRemark(),
                            String.valueOf(item.getIncomeAmount()));

                    long pC = (Long) CloudItem.get(PARTNER_COUNT);
                    int PC = (int) pC;

                    switch (PC){
                        case 1 : item = new SingleIncomeCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                            CloudItem.get(MyStatic.CROP_ID).toString(),
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
                            CloudItem.get(MyStatic.CROP_ID).toString(),
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
                            CloudItem.get(MyStatic.CROP_ID).toString(),
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
                            CloudItem.get(MyStatic.CROP_ID).toString(),
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
                            CloudItem.get(MyStatic.CROP_ID).toString(),
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

    public void FilterSingeBuyerHistory(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_for_income,null);

        AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
        AutoCompleteTextView BuyerFilter = customFilterView.findViewById(R.id.buyer_filter);

        BuyerFilter.setText(BuyerName);

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
                singleChoose.add(0,ChooseDateDialogSingle(HistoryOfSingleBuyerPurchase.this,SingleShowDateFilter));
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
                fromChoose.add(0,ChooseDateDialogFrom(HistoryOfSingleBuyerPurchase.this,FromShowDate));
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        final ArrayList<Boolean> toChoose = new ArrayList<>();
        toChoose.add(false);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChoose.add(0,ChooseDateDialogTo(HistoryOfSingleBuyerPurchase.this,ToShowDate));
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
