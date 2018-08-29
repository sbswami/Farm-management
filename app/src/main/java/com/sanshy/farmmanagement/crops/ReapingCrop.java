package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
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
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.PARTNER;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_COUNT;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_1;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_2;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_3;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_4;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_5;
import static com.sanshy.farmmanagement.MyStatic.REAPING;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.addRemainingAmountServiceProvider;
import static com.sanshy.farmmanagement.MyStatic.postAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.postAllReport;
import static com.sanshy.farmmanagement.MyStatic.postSingleReport;
import static com.sanshy.farmmanagement.MyStatic.purchaseServiceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.reapingCropLocation;
import static com.sanshy.farmmanagement.MyStatic.reapingCropRemark;
import static com.sanshy.farmmanagement.MyStatic.serviceProviderList;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;
import static com.sanshy.farmmanagement.MyStatic.staticDate;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER;

public class ReapingCrop extends AppCompatActivity {

    TextView ReapingCropTitle,ReapingCropDateShow;
    EditText ReapingCropAmount,ReapingCropAreaOfLand;
    AutoCompleteTextView ReapingCropRemark,ReapingCropServiceProviderName,ReapingCropPartnerName;
    RadioGroup ReapingCropModeOfPayment;
    CheckBox ReapingCropPaidByPartner;
    LinearLayout ReapingCropPaidPartnerContainer;

    String CropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaping_crop);

        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);

        ReapingCropTitle = findViewById(R.id.reaping_crop_title);
        ReapingCropDateShow = findViewById(R.id.reaping_crop_show_date);

        ReapingCropAmount = findViewById(R.id.reaping_crop_amount);
        ReapingCropAreaOfLand = findViewById(R.id.reaping_crop_land_area_of_service);

        ReapingCropRemark = findViewById(R.id.reaping_crop_remark);
        ReapingCropServiceProviderName = findViewById(R.id.reaping_crop_service_provider_name);
        ReapingCropPartnerName = findViewById(R.id.reaping_crop_partner_name);

        ReapingCropModeOfPayment = findViewById(R.id.reaping_crop_mode_of_payment);

        ReapingCropPaidByPartner = findViewById(R.id.reaping_crop_paid_by_partner);

        ReapingCropPaidPartnerContainer = findViewById(R.id.reaping_crop_paid_partner_container);
        ReapingCropPaidByPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ReapingCropPaidPartnerContainer.setVisibility(View.VISIBLE);
                }
                else{
                    ReapingCropPaidPartnerContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    ArrayList<String> RemarkList = new ArrayList<>();
    ArrayList<String> ServiceProviderList = new ArrayList<>();
    ArrayList<String> PartnerList = new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();

        reapingCropRemark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(ReapingCrop.this,ReapingCropRemark,RemarkList);
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
                    ServiceProviderList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(ReapingCrop.this,ReapingCropServiceProviderName,ServiceProviderList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        singleCropLocation.document(CropId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if ((Boolean) documentSnapshot.get(PARTNER)){
                    ReapingCropPaidByPartner.setVisibility(View.VISIBLE);
                    long pC = (long) documentSnapshot.get(PARTNER_COUNT);
                    int PC = (int) pC;
                    switch (PC){
                        case 5 : PartnerList.add(documentSnapshot.get(PARTNER_NAME_5).toString());
                        case 4 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_4).toString());
                        case 3 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_3).toString());
                        case 2 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_2).toString());
                        case 1 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_1).toString());
                    }
                    setSnipper(ReapingCrop.this,ReapingCropPartnerName,PartnerList);
                }

            }
        });

    }


    String RemarkSt;
    String AmountSt;
    String AreaOfLandSt;
    String ServiceProviderNameSt;
    String PartnerNameSt;
    boolean CashMode = true;
    boolean ByPartner = false;


    public void saveReapingCropBt(View view){

        RemarkSt = ReapingCropRemark.getText().toString();
        AmountSt = ReapingCropAmount.getText().toString();
        AreaOfLandSt = ReapingCropAreaOfLand.getText().toString();
        ServiceProviderNameSt = ReapingCropServiceProviderName.getText().toString();
        PartnerNameSt = ReapingCropPartnerName.getText().toString();

        ByPartner = ReapingCropPaidByPartner.isChecked();

        int CashId = ReapingCropModeOfPayment.getCheckedRadioButtonId();

        CashMode = R.id.sowing_cash_mode == CashId;

        if(RemarkSt.isEmpty()){
            ReapingCropRemark.setError(getString(R.string.fill_it));
            return;
        }
        if (AmountSt.isEmpty()){
            ReapingCropAmount.setError(getString(R.string.fill_it));
            return;
        }
        if (AreaOfLandSt.isEmpty()){

            AreaOfLandSt = "0";

        }
        if (ServiceProviderNameSt.isEmpty()){
            ReapingCropServiceProviderName.setError(getString(R.string.fill_it));
            return;
        }

        if (!(ServiceProviderList.contains(ServiceProviderNameSt))){
            ShowDialog(this,getString(R.string.first_add_service_provider));
            return;
        }
        if (ByPartner&&PartnerNameSt.isEmpty()){
            ReapingCropPartnerName.setError(getString(R.string.fill_it));
            return;
        }
        if (ByPartner&&(!PartnerList.contains(PartnerNameSt))){
            ShowDialog(this, getString(R.string.first_add_partner));
            return;
        }
        if (!isDate){
            ShowDialog(this,getString(R.string.please_choose_any_date));
            return;
        }

        if (!(RemarkList.contains(RemarkSt))){
            RemarkList.add(RemarkSt);
            reapingCropRemark.setValue(RemarkList);
        }


        SaveIt();



    }

    private void SaveIt() {
        ShowProgress(this);
        final String Key = TimeStampKey(new Date());
        SingleSowingCropData item = new SingleSowingCropData(
                Key,
                RemarkSt,
                CropId,
                Double.parseDouble(AmountSt),
                Double.parseDouble(AreaOfLandSt),
                staticDate,
                CashMode,
                ServiceProviderNameSt,
                ServiceProviderNameSt,
                ByPartner
        );
        if (ByPartner){
            item = new SingleSowingCropData(
                    Key,
                    RemarkSt,
                    CropId,
                    Double.parseDouble(AmountSt),
                    Double.parseDouble(AreaOfLandSt),
                    staticDate,
                    CashMode,
                    ServiceProviderNameSt,
                    ServiceProviderNameSt,
                    ByPartner,PartnerNameSt,
                    PartnerNameSt
            );
        }

        final Map<String,Object> SaveItem = new HashMap<>();
        SaveItem.put(ID,item.getSowingId());
        SaveItem.put(REMARK,item.getSowingRemark());
        SaveItem.put(CROP_ID,item.getCropId());
        SaveItem.put(AMOUNT,item.getSowingAmount());
        SaveItem.put(AREA_OF_LAND,item.getSowingLandArea());
        SaveItem.put(DATE,item.getSowingDate());
        SaveItem.put(CASH_MODE,item.isCashService());
        SaveItem.put(SERVICE_PROVIDER_NAME,item.getServiceProviderName());
        SaveItem.put(SERVICE_PROVIDER_ID,item.getServiceProviderId());
        SaveItem.put(BY_PARTNER,item.isCheckPartner());
        if (ByPartner){
            SaveItem.put(PARTNER_NAME,item.getPartnerName());
            SaveItem.put(PARTNER_ID,item.getPartnerId());
        }

        reapingCropLocation(CropId).document(Key).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HideProgress();
                if (task.isSuccessful()){
                    purchaseServiceProviderLocation(ServiceProviderNameSt).document(Key).set(SaveItem);
                    postSingleReport(ALL,CropId,CashMode,AmountSt,REAPING);
                    postAllReport(ALL,CashMode,AmountSt,REAPING);
                    if (ByPartner){
                        postSingleReport(PartnerNameSt,CropId,CashMode,AmountSt,REAPING);
                        postAllReport(PartnerNameSt,CashMode,AmountSt,REAPING);
                    }
                    else{
                        postSingleReport(SELF,CropId,CashMode,AmountSt,REAPING);
                        postAllReport(SELF,CashMode,AmountSt,REAPING);
                    }
                    if (!CashMode){
                        addRemainingAmountServiceProvider(ServiceProviderNameSt,Double.parseDouble(AmountSt));
                        postAllRemainingReport(ALL,AmountSt, SERVICE_PROVIDER);
                        if (ByPartner){
                            postAllRemainingReport(PartnerNameSt,AmountSt, SERVICE_PROVIDER);
                        }
                        else{
                            postAllRemainingReport(SELF,AmountSt, SERVICE_PROVIDER);
                        }
                    }
                    ShowDialog(ReapingCrop.this,getString(R.string.saved));
                }
                else{
                    ShowDialog(ReapingCrop.this,getString(R.string.try_again_later_or_send_feedback));
                }
            }
        });

    }

    public void cancelReapingCropBt(View view){
        finish();
    }
    boolean isDate = false;
    public void dateChooserReapingCropBt(View view){
        isDate = ChooseDateDialog(this,ReapingCropDateShow);
    }
}
