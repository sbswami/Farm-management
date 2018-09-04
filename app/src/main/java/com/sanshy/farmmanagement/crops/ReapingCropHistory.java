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
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME;
import static com.sanshy.farmmanagement.MyStatic.REAPING;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.ShowDetails;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.lessAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.lessAllReport;
import static com.sanshy.farmmanagement.MyStatic.lessSingleReport;
import static com.sanshy.farmmanagement.MyStatic.partnerList;
import static com.sanshy.farmmanagement.MyStatic.purchaseServiceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.reapingCropLocation;
import static com.sanshy.farmmanagement.MyStatic.reapingCropRemark;
import static com.sanshy.farmmanagement.MyStatic.removeRemainingAmountServiceProvider;
import static com.sanshy.farmmanagement.MyStatic.serviceProviderList;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleCropReportLocation;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class ReapingCropHistory extends AppCompatActivity {

    ListView HistoryList;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();
    ArrayList<SingleSowingCropData> ReapingList = new ArrayList<>();
    SingleFourColumnsListAdapter myAdapter;

    String CropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaping_crop_history);

        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);

        HistoryList = findViewById(R.id.reaping_history_list);

        myAdapter = new SingleFourColumnsListAdapter(this,FourList);

        HistoryList.setAdapter(myAdapter);
        HistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String Id = ReapingList.get(position).getSowingId();

                String[] dataList = new String[7];

                dataList[0] = ReapingList.get(position).getSowingRemark();
                dataList[1] = String.valueOf(ReapingList.get(position).getSowingAmount());
                dataList[2] = DateToString(ReapingList.get(position).getSowingDate());
                dataList[3] = ReapingList.get(position).getServiceProviderName();
                dataList[4] = String.valueOf(ReapingList.get(position).getSowingLandArea());
                dataList[5] = ReapingList.get(position).isCashService() ?getString(R.string.cash):getString(R.string.borrow);
                dataList[6] = ReapingList.get(position).isCheckPartner() ?ReapingList.get(position).getPartnerName():getString(R.string.me);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReapingCropHistory.this);

                LayoutInflater inflater = (LayoutInflater) ReapingCropHistory.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                        .setPositiveButton(ReapingCropHistory.this.getString(R.string.ok), null);

                builder.setNegativeButton(ReapingCropHistory.this.getString(R.string.undo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReapingCropHistory.this);

                        builder.setTitle(getString(R.string.undo));
                        builder.setMessage(getString(R.string.are_you_sure_you_want_undo));
                        builder.setPositiveButton(getString(R.string.close),null);
                        builder.setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reapingCropLocation(CropId).document(Id).delete();
                                purchaseServiceProviderLocation(ReapingList.get(position).getServiceProviderId()).document(Id).delete();
                                reapingCropLocation(CropId);
                                FourList.remove(position);
                                double AmountR = ReapingList.get(position).getSowingAmount();
                                boolean CashMode = ReapingList.get(position).isCashService();
                                lessSingleReport(CashMode,CropId,ALL,AmountR,REAPING);
                                lessAllReport(CashMode,ALL,AmountR,REAPING);
                                if (ReapingList.get(position).isCheckPartner()){
                                    lessSingleReport(CashMode,CropId,ReapingList.get(position).getPartnerName(),AmountR,REAPING);
                                    lessAllReport(CashMode,ReapingList.get(position).getPartnerName(),AmountR,REAPING);
                                }
                                else{
                                    lessSingleReport(CashMode,CropId,SELF,AmountR,REAPING);
                                    lessAllReport(CashMode,SELF,AmountR,REAPING);
                                }
                                if (!CashMode){
                                    removeRemainingAmountServiceProvider(ReapingList.get(position).getServiceProviderId(),AmountR);
                                    lessAllRemainingReport(ALL,AmountR,SERVICE_PROVIDER);
                                    if (ReapingList.get(position).isCheckPartner()){
                                        lessAllRemainingReport(ReapingList.get(position).getPartnerName(),AmountR,SERVICE_PROVIDER);
                                    }else{
                                        lessAllRemainingReport(SELF,AmountR,SERVICE_PROVIDER);
                                    }
                                }
                                ReapingList.remove(position);
                                myAdapter.notifyDataSetChanged();
                            }
                        });

                        builder.create().show();
                    }
                });
                builder.create().show();
            }
        });

    }

    private void addData() {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataCash(boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataPartner(String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataPartnerCash(String partner, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(CASH_MODE,cashMode)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataService(String service) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataServiceCash(String service, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataServicePartner(String service, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataServicePartnerCash(String service, String partner,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemark(String remark) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkCash(String remark, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkPartner(String remark, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkPartnerCash(String remark, String partner, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(CASH_MODE,cashMode)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkService(String remark, String service) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkServiceCash(String remark, String service, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkServicePartner(String remark, String service, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataRemarkServicePartnerCash(String remark, String service, String partner,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmount(double minAmount, double maxAmount) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountCash(double minAmount, double maxAmount,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountPartner(double minAmount, double maxAmount,String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountPartnerCash(double minAmount, double maxAmount,String partner, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountService(double minAmount, double maxAmount, String service) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountServiceCash(double minAmount, double maxAmount, String service, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountServicePartner(double minAmount, double maxAmount, String service, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountServicePartnerCash(double minAmount, double maxAmount, String service, String partner,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemark(double minAmount, double maxAmount, String remark) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkCash(double minAmount, double maxAmount, String remark, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkPartner(double minAmount, double maxAmount, String remark, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkPartnerCash(double minAmount, double maxAmount, String remark, String partner, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkService(double minAmount, double maxAmount, String remark, String service) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkServiceCash(double minAmount, double maxAmount, String remark, String service, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkServicePartner(double minAmount, double maxAmount, String remark, String service, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataAmountRemarkServicePartnerCash(double minAmount, double maxAmount, String remark, String service, String partner,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(AMOUNT,minAmount)
                .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                .orderBy(AMOUNT, Query.Direction.ASCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDate(Date date1, Date date2) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateCash(Date date1, Date date2,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDatePartner(Date date1, Date date2,String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDatePartnerCash(Date date1, Date date2,String partner, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateService(Date date1, Date date2, String service) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateServiceCash(Date date1, Date date2, String service, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateServicePartner(Date date1, Date date2, String service, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateServicePartnerCash(Date date1, Date date2, String service, String partner,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemark(Date date1, Date date2, String remark) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkCash(Date date1, Date date2, String remark, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkPartner(Date date1, Date date2, String remark, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkPartnerCash(Date date1, Date date2, String remark, String partner, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkService(Date date1, Date date2, String remark, String service) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkServiceCash(Date date1, Date date2, String remark, String service, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkServicePartner(Date date1, Date date2, String remark, String service, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateRemarkServicePartnerCash(Date date1, Date date2, String remark, String service, String partner,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
                .whereGreaterThanOrEqualTo(DATE, date1)
                .whereLessThanOrEqualTo(DATE, date2)
                .orderBy(DATE, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(this, myListener)
                .addOnFailureListener(this, myFailData);
    }
    private void addDataDateAmount(final Date date1, final Date date2, double minAmount, double maxAmount) {
        ShowProgress(this);
        reapingCropLocation(CropId)
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
        reapingCropLocation(CropId)
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
    private void addDataDateAmountPartner(final Date date1, final Date date2 , double minAmount, double maxAmount,String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
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
    private void addDataDateAmountPartnerCash(final Date date1, final Date date2 , double minAmount, double maxAmount,String partner, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
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
    private void addDataDateAmountService(final Date date1, final Date date2 , double minAmount, double maxAmount, String service) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
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
    private void addDataDateAmountServiceCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String service, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
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
    private void addDataDateAmountServicePartner(final Date date1, final Date date2 , double minAmount, double maxAmount, String service, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
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
    private void addDataDateAmountServicePartnerCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String service, String partner,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
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
    private void addDataDateAmountRemark(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark) {
        ShowProgress(this);
        reapingCropLocation(CropId)
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
        reapingCropLocation(CropId)
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
    private void addDataDateAmountRemarkPartner(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
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
    private void addDataDateAmountRemarkPartnerCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String partner, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
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
    private void addDataDateAmountRemarkService(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String service) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
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
    private void addDataDateAmountRemarkServiceCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String service, boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
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
    private void addDataDateAmountRemarkServicePartner(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String service, String partner) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
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
    private void addDataDateAmountRemarkServicePartnerCash(final Date date1, final Date date2 , double minAmount, double maxAmount, String remark, String service, String partner,boolean cashMode) {
        ShowProgress(this);
        reapingCropLocation(CropId)
                .whereEqualTo(REMARK,remark)
                .whereEqualTo(CASH_MODE,cashMode)
                .whereEqualTo(PARTNER_NAME,partner)
                .whereEqualTo(SERVICE_PROVIDER_NAME, service)
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






    @Override
    protected void onStart() {
        super.onStart();

        addData();

        RemarkList.clear();

        reapingCropRemark.addListenerForSingleValueEvent(new ValueEventListener() {
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

        serviceProviderList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ServiceList = (ArrayList<String>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        partnerList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    PartnerList = (ArrayList<String>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    ArrayList<String> RemarkList = new ArrayList<>();
    ArrayList<String> ServiceList = new ArrayList<>();
    ArrayList<String> PartnerList = new ArrayList<>();

    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;

    String minAmountFilterSt;
    String maxAmountFilterSt;

    String remarkFilterSt;
    String serviceFilterSt;
    String partnerFilterSt;

    boolean cashModeFilter;



    public void FilterReapingHistory(View v){

        singleDate = false;
        betweenDate =false;
        singleChooseDate = false;
        fromChooseDate = false;
        toChooseDate = false;


        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter,null);

        final AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
        setSnipper(this,RemarkFilter,RemarkList);
        final AutoCompleteTextView ServiceProviderFilter = customFilterView.findViewById(R.id.service_provider_filter);
        setSnipper(this,ServiceProviderFilter,ServiceList);
        final AutoCompleteTextView PartnerFilter = customFilterView.findViewById(R.id.partner_filter);
        setSnipper(this,PartnerFilter,PartnerList);

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
                singleChooseDate = ChooseDateDialogSingle(ReapingCropHistory.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(ReapingCropHistory.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(ReapingCropHistory.this,ToShowDate);
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
                serviceFilterSt = ServiceProviderFilter.getText().toString();
                partnerFilterSt = PartnerFilter.getText().toString();

                boolean dF, aF, rF, spF, pF, cF;

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
                spF = serviceFilterSt.isEmpty();
                pF = partnerFilterSt.isEmpty();

                String fr,fsp,fp;
                double fmina,fmaxa;
                boolean fc;
                // Change Name To short Form
                fr = remarkFilterSt;
                fsp = serviceFilterSt;
                fp = partnerFilterSt;

                fc = cashModeFilter;

                if (dF){
                    if (aF){
                        if (rF){
                            /*
                            CashMode
                             */
                            if (spF&&pF&&!cF){
                                addDataCash(cashModeFilter);
                            }

                            /*
                            Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataPartner(partnerFilterSt);
                            }

                            /*
                            Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataPartnerCash(partnerFilterSt,cashModeFilter);
                            }

                            /*
                            ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataService(fsp);

                            }

                            /*
                            Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataServiceCash(fsp,fc);

                            }

                            /*
                            Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataServicePartner(fsp,fp);

                            }

                            /*
                            Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataServicePartnerCash(fsp,fp,fc);

                            }
                        }
                        else{

                            /*
                            Remark
                             */
                            if (spF&&pF&&cF){
                                addDataRemark(fr);
                            }

                            /*
                            Remark and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataRemarkCash(fr,fc);
                            }

                            /*
                            Remark and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataRemarkPartner(fr,fp);
                            }

                            /*
                            Remark and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataRemarkPartnerCash(fr,fp,fc);
                            }

                            /*
                            Remark and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataRemarkService(fr,fsp);
                            }

                            /*
                            Remark and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataRemarkServiceCash(fr,fsp,fc);
                            }

                            /*
                            Remark and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataRemarkServicePartner(fr,fsp,fp);
                            }

                            /*
                            Remark and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataRemarkServicePartnerCash(fr,fsp,fp,fc);
                            }
                        }
                    }
                    else{
                        fmina = Double.parseDouble(minAmountFilterSt);
                        fmaxa = Double.parseDouble(maxAmountFilterSt);

                        if (rF){

                            /*Amount
                             */
                            if (spF&&pF&&cF){
                                addDataAmount(fmina,fmaxa);
                            }
                            
                            /*
                            Amount and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataAmountCash(fmina,fmaxa,fc);
                            }

                            /*
                            Amount and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataAmountPartner(fmina,fmaxa,fp);
                            }

                            /*
                            Amount and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataAmountPartnerCash(fmina,fmaxa,fp,fc);
                            }

                            /*
                            Amount and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataAmountService(fmina,fmaxa,fsp);

                            }

                            /*
                            Amount and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataAmountServiceCash(fmina,fmaxa,fsp,fc);

                            }

                            /*
                            Amount and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataAmountServicePartner(fmina,fmaxa,fsp,fp);

                            }

                            /*
                            Amount and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataAmountServicePartnerCash(fmina,fmaxa,fsp,fp,fc);

                            }
                        }
                        else{

                            /*
                            Amount and Remark
                             */
                            if (spF&&pF&&cF){
                                addDataAmountRemark(fmina,fmaxa,fr);
                            }

                            /*
                            Amount and Remark and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataAmountRemarkCash(fmina,fmaxa,fr,fc);
                            }

                            /*
                            Amount and Remark and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataAmountRemarkPartner(fmina,fmaxa,fr,fp);
                            }

                            /*
                            Amount and Remark and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataAmountRemarkPartnerCash(fmina,fmaxa,fr,fp,fc);
                            }

                            /*
                            Amount and Remark and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataAmountRemarkService(fmina,fmaxa,fr,fsp);
                            }

                            /*
                            Amount and Remark and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataAmountRemarkServiceCash(fmina,fmaxa,fr,fsp,fc);
                            }

                            /*
                            Amount and Remark and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataAmountRemarkServicePartner(fmina,fmaxa,fr,fsp,fp);
                            }

                            /*
                            Amount and Remark and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataAmountRemarkServicePartnerCash(fmina,fmaxa,fr,fsp,fp,fc);
                            }
                        }
                    }
                }
                else if (singleChooseDate&&singleDate){
                    if (aF){
                        if (rF){
                            
                            /*
                            Single Date
                             */
                            if (spF&&pF&&cF){
                                addDataDate(singleDateStatic, singleDateStaticTill);
                            }
                            
                            /*
                            Single Date and CashMode
                             */
                            if (spF&&pF&&!cF){
                                addDataDateCash(singleDateStatic, singleDateStaticTill,cashModeFilter);
                            }

                            /*
                            Single Date and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataDatePartner(singleDateStatic, singleDateStaticTill,partnerFilterSt);
                            }

                            /*
                            Single Date and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataDatePartnerCash(singleDateStatic, singleDateStaticTill,partnerFilterSt,cashModeFilter);
                            }

                            /*
                            Single Date and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataDateService(singleDateStatic, singleDateStaticTill, fsp );

                            }

                            /*
                            Single Date and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataDateServiceCash(singleDateStatic, singleDateStaticTill, fsp ,fc);

                            }

                            /*
                            Single Date and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataDateServicePartner(singleDateStatic, singleDateStaticTill, fsp ,fp);

                            }

                            /*
                            Single Date and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataDateServicePartnerCash(singleDateStatic, singleDateStaticTill, fsp ,fp,fc);

                            }
                        }
                        else{

                            /*
                            Single Date and Remark
                             */
                            if (spF&&pF&&cF){
                                addDataDateRemark(singleDateStatic, singleDateStaticTill, fr );
                            }

                            /*
                            Single Date and Remark and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataDateRemarkCash(singleDateStatic, singleDateStaticTill, fr ,fc);
                            }

                            /*
                            Single Date and Remark and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataDateRemarkPartner(singleDateStatic, singleDateStaticTill, fr ,fp);
                            }

                            /*
                            Single Date and Remark and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataDateRemarkPartnerCash(singleDateStatic, singleDateStaticTill, fr ,fp,fc);
                            }

                            /*
                            Single Date and Remark and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataDateRemarkService(singleDateStatic, singleDateStaticTill, fr ,fsp  );
                            }

                            /*
                            Single Date and Remark and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataDateRemarkServiceCash(singleDateStatic, singleDateStaticTill, fr ,fsp  ,fc);
                            }

                            /*
                            Single Date and Remark and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataDateRemarkServicePartner(singleDateStatic, singleDateStaticTill, fr ,fsp  ,fp);
                            }

                            /*
                            Single Date and Remark and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataDateRemarkServicePartnerCash(singleDateStatic, singleDateStaticTill, fr ,fsp  ,fp,fc);
                            }
                        }
                    }
                    else{
                        fmina = Double.parseDouble(minAmountFilterSt);
                        fmaxa = Double.parseDouble(maxAmountFilterSt);

                        if (rF){

                            /*Amount
                             */
                            if (spF&&pF&&cF){
                                addDataDateAmount(singleDateStatic, singleDateStaticTill, fmina,fmaxa);
                            }
                            
                            /*
                            Single Date and Amount and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataDateAmountCash(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fc);
                            }

                            /*
                            Single Date and Amount and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataDateAmountPartner(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fp);
                            }

                            /*
                            Single Date and Amount and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataDateAmountPartnerCash(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fp,fc);
                            }

                            /*
                            Single Date and Amount and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataDateAmountService(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fsp);

                            }

                            /*
                            Single Date and Amount and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataDateAmountServiceCash(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fsp,fc);

                            }

                            /*
                            Single Date and Amount and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataDateAmountServicePartner(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fsp,fp);

                            }

                            /*
                            Single Date and Amount and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataDateAmountServicePartnerCash(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fsp,fp,fc);

                            }
                        }
                        else{

                            /*
                            Single Date and Amount and Remark
                             */
                            if (spF&&pF&&cF){
                                addDataDateAmountRemark(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fr);
                            }

                            /*
                            Single Date and Amount and Remark and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataDateAmountRemarkCash(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fr,fc);
                            }

                            /*
                            Single Date and Amount and Remark and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataDateAmountRemarkPartner(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fr,fp);
                            }

                            /*
                            Single Date and Amount and Remark and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataDateAmountRemarkPartnerCash(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fr,fp,fc);
                            }

                            /*
                            Single Date and Amount and Remark and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataDateAmountRemarkService(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fr,fsp);
                            }

                            /*
                            Single Date and Amount and Remark and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataDateAmountRemarkServiceCash(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fr,fsp,fc);
                            }

                            /*
                            Single Date and Amount and Remark and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataDateAmountRemarkServicePartner(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fr,fsp,fp);
                            }

                            /*
                            Single Date and Amount and Remark and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataDateAmountRemarkServicePartnerCash(singleDateStatic, singleDateStaticTill, fmina,fmaxa,fr,fsp,fp,fc);
                            }
                        }
                    }
                }
                else if (betweenDate&&fromChooseDate&&toChooseDate){
                    if (aF){
                        if (rF){
                            
                            /*
                            Between Date
                             */
                            if (spF&&pF&&cF){
                                addDataDate(fromDateStatic, toDateStatic);
                            }
                            
                            /*
                            Between Date and CashMode
                             */
                            if (spF&&pF&&!cF){
                                addDataDateCash(fromDateStatic, toDateStatic,cashModeFilter);
                            }

                            /*
                            Between Date and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataDatePartner(fromDateStatic, toDateStatic,partnerFilterSt);
                            }

                            /*
                            Between Date and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataDatePartnerCash(fromDateStatic, toDateStatic,partnerFilterSt,cashModeFilter);
                            }

                            /*
                            Between Date and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataDateService(fromDateStatic, toDateStatic, fsp );

                            }

                            /*
                            Between Date and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataDateServiceCash(fromDateStatic, toDateStatic, fsp ,fc);

                            }

                            /*
                            Between Date and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataDateServicePartner(fromDateStatic, toDateStatic, fsp ,fp);

                            }

                            /*
                            Between Date and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataDateServicePartnerCash(fromDateStatic, toDateStatic, fsp ,fp,fc);

                            }
                        }
                        else{

                            /*
                            Between Date and Remark
                             */
                            if (spF&&pF&&cF){
                                addDataDateRemark(fromDateStatic, toDateStatic, fr );
                            }

                            /*
                            Between Date and Remark and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataDateRemarkCash(fromDateStatic, toDateStatic, fr ,fc);
                            }

                            /*
                            Between Date and Remark and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataDateRemarkPartner(fromDateStatic, toDateStatic, fr ,fp);
                            }

                            /*
                            Between Date and Remark and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataDateRemarkPartnerCash(fromDateStatic, toDateStatic, fr ,fp,fc);
                            }

                            /*
                            Between Date and Remark and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataDateRemarkService(fromDateStatic, toDateStatic, fr ,fsp  );
                            }

                            /*
                            Between Date and Remark and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataDateRemarkServiceCash(fromDateStatic, toDateStatic, fr ,fsp  ,fc);
                            }

                            /*
                            Between Date and Remark and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataDateRemarkServicePartner(fromDateStatic, toDateStatic, fr ,fsp  ,fp);
                            }

                            /*
                            Between Date and Remark and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataDateRemarkServicePartnerCash(fromDateStatic, toDateStatic, fr ,fsp  ,fp,fc);
                            }
                        }
                    }
                    else{
                        fmina = Double.parseDouble(minAmountFilterSt);
                        fmaxa = Double.parseDouble(maxAmountFilterSt);

                        if (rF){

                            /*Amount
                             */
                            if (spF&&pF&&cF){
                                addDataDateAmount(fromDateStatic, toDateStatic, fmina,fmaxa);
                            }
                            
                            /*
                            Between Date and Amount and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataDateAmountCash(fromDateStatic, toDateStatic, fmina,fmaxa,fc);
                            }

                            /*
                            Between Date and Amount and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataDateAmountPartner(fromDateStatic, toDateStatic, fmina,fmaxa,fp);
                            }

                            /*
                            Between Date and Amount and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataDateAmountPartnerCash(fromDateStatic, toDateStatic, fmina,fmaxa,fp,fc);
                            }

                            /*
                            Between Date and Amount and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataDateAmountService(fromDateStatic, toDateStatic, fmina,fmaxa,fsp);

                            }

                            /*
                            Between Date and Amount and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataDateAmountServiceCash(fromDateStatic, toDateStatic, fmina,fmaxa,fsp,fc);

                            }

                            /*
                            Between Date and Amount and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataDateAmountServicePartner(fromDateStatic, toDateStatic, fmina,fmaxa,fsp,fp);

                            }

                            /*
                            Between Date and Amount and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataDateAmountServicePartnerCash(fromDateStatic, toDateStatic, fmina,fmaxa,fsp,fp,fc);

                            }
                        }
                        else{

                            /*
                            Between Date and Amount and Remark
                             */
                            if (spF&&pF&&cF){
                                addDataDateAmountRemark(fromDateStatic, toDateStatic, fmina,fmaxa,fr);
                            }

                            /*
                            Between Date and Amount and Remark and CashMode
                             */
                            else if (spF&&pF&&!cF){
                                addDataDateAmountRemarkCash(fromDateStatic, toDateStatic, fmina,fmaxa,fr,fc);
                            }

                            /*
                            Between Date and Amount and Remark and Partner
                             */
                            else if (spF&&!pF&&cF){
                                addDataDateAmountRemarkPartner(fromDateStatic, toDateStatic, fmina,fmaxa,fr,fp);
                            }

                            /*
                            Between Date and Amount and Remark and Partner and CashMode
                             */
                            else if (spF&&!pF&&!cF){
                                addDataDateAmountRemarkPartnerCash(fromDateStatic, toDateStatic, fmina,fmaxa,fr,fp,fc);
                            }

                            /*
                            Between Date and Amount and Remark and ServiceProvider
                             */
                            else if (!spF&&pF&&cF){
                                addDataDateAmountRemarkService(fromDateStatic, toDateStatic, fmina,fmaxa,fr,fsp);
                            }

                            /*
                            Between Date and Amount and Remark and Service and CashMode
                             */
                            else if (!spF&&pF&&!cF){
                                addDataDateAmountRemarkServiceCash(fromDateStatic, toDateStatic, fmina,fmaxa,fr,fsp,fc);
                            }

                            /*
                            Between Date and Amount and Remark and Service and Partner
                             */
                            else if (!spF&&!pF&&cF){
                                addDataDateAmountRemarkServicePartner(fromDateStatic, toDateStatic, fmina,fmaxa,fr,fsp,fp);
                            }

                            /*
                            Between Date and Amount and Remark and Service and Partner and CashMode
                             */
                            else if (!spF&&!pF&&!cF){
                                addDataDateAmountRemarkServicePartnerCash(fromDateStatic, toDateStatic, fmina,fmaxa,fr,fsp,fp,fc);
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
        ShowDialog(ReapingCropHistory.this,getString(R.string.please_fill_correct_value));
    }

    public void dateWithAPro(QuerySnapshot queryDocumentSnapshots, Date date1, Date date2){
        ReapingList.clear();
        FourList.clear();

        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){

            if (!((documentSnapshot.getDate(DATE).after(date1)
                    ||documentSnapshot.getDate(DATE).equals(date1))
                    &&(documentSnapshot.getDate(DATE).before(date2)
                    ||documentSnapshot.getDate(DATE).equals(date2)))) {
                continue;
            }

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

            ReapingList.add(item);

            SingleFourColumnsList iitem = new SingleFourColumnsList(DateToString(item.getSowingDate()),item.getSowingRemark(),String.valueOf(item.SowingAmount),item.ServiceProviderName);
            FourList.add(iitem);

        }

        myAdapter.notifyDataSetChanged();
        HideProgress();
    }

    OnSuccessListener<QuerySnapshot> myListener = new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            ReapingList.clear();
            FourList.clear();
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

                ReapingList.add(item);

                SingleFourColumnsList iitem = new SingleFourColumnsList(DateToString(item.getSowingDate()),item.getSowingRemark(),String.valueOf(item.SowingAmount),item.ServiceProviderName);
                FourList.add(iitem);

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
