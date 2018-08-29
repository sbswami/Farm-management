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
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
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
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.SOWING;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.addRemainingAmountServiceProvider;
import static com.sanshy.farmmanagement.MyStatic.postAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.postSingleReport;
import static com.sanshy.farmmanagement.MyStatic.purchaseServiceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.serviceProviderList;
import static com.sanshy.farmmanagement.MyStatic.serviceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;
import static com.sanshy.farmmanagement.MyStatic.postAllReport;
import static com.sanshy.farmmanagement.MyStatic.sowingCropLocation;
import static com.sanshy.farmmanagement.MyStatic.sowingCropRemark;
import static com.sanshy.farmmanagement.MyStatic.staticDate;

public class SowingCrop extends AppCompatActivity {

    TextView SowingTitle,SowingDate;
    EditText SowingCropAmount,SowingCropAreaOfLand;
    AutoCompleteTextView SowingCropRemark,SowingCropServiceProviderName,SowingCropPartnerName;
    RadioGroup SowingCropModeOfPayment;
    CheckBox SowingCropPaidByPartner;
    LinearLayout SowingCropPaidPartnerContainer;

    String CropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sowing_crop);

        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);

        SowingTitle = findViewById(R.id.sowing_crop_title);
        SowingDate = findViewById(R.id.sowing_show_date);

        SowingCropAmount = findViewById(R.id.sowing_crop_amount);
        SowingCropAreaOfLand = findViewById(R.id.sowing_land_area_of_service);

        SowingCropRemark = findViewById(R.id.sowing_crop_remark);
        SowingCropServiceProviderName = findViewById(R.id.sowing_service_provider_name);
        SowingCropPartnerName = findViewById(R.id.sowing_partner_name);

        SowingCropModeOfPayment = findViewById(R.id.sowing_mode_of_payment);

        SowingCropPaidByPartner = findViewById(R.id.sowing_paid_by_partner);

        SowingCropPaidPartnerContainer = findViewById(R.id.sowing_paid_partner_container);

        SowingCropPaidByPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SowingCropPaidPartnerContainer.setVisibility(View.VISIBLE);
                }
                else{
                    SowingCropPaidPartnerContainer.setVisibility(View.GONE);
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

        singleCropLocation.document(CropId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if ((Boolean) documentSnapshot.get(PARTNER)){
                    SowingCropPaidByPartner.setVisibility(View.VISIBLE);
                    long pC = (long) documentSnapshot.get(PARTNER_COUNT);
                    int PC = (int) pC;
                    switch (PC){
                        case 5 : PartnerList.add(documentSnapshot.get(PARTNER_NAME_5).toString());
                        case 4 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_4).toString());
                        case 3 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_3).toString());
                        case 2 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_2).toString());
                        case 1 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_1).toString());
                    }
                    setSnipper(SowingCrop.this,SowingCropPartnerName,PartnerList);
                }

            }
        });

        sowingCropRemark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(SowingCrop.this,SowingCropRemark,RemarkList);
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
                    setSnipper(SowingCrop.this,SowingCropServiceProviderName,ServiceProviderList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    boolean isDate = false;
    public void dateChooserSowing(View view){
        isDate = ChooseDateDialog(this,SowingDate);
    }

    String RemarkSt;
    String AmountSt;
    String AreaOfLandSt;
    String ServiceProviderNameSt;
    String PartnerNameSt;
    boolean CashMode = true;
    boolean ByPartner = false;

    public void saveSowingCrop(View view){

        RemarkSt = SowingCropRemark.getText().toString();
        AmountSt = SowingCropAmount.getText().toString();
        AreaOfLandSt = SowingCropAreaOfLand.getText().toString();
        ServiceProviderNameSt = SowingCropServiceProviderName.getText().toString();
        PartnerNameSt = SowingCropPartnerName.getText().toString();

        ByPartner = SowingCropPaidByPartner.isChecked();

        int CashId = SowingCropModeOfPayment.getCheckedRadioButtonId();

        CashMode = R.id.sowing_cash_mode == CashId;

        if(RemarkSt.isEmpty()){
            SowingCropRemark.setError(getString(R.string.fill_it));
            return;
        }
        if (AmountSt.isEmpty()){
            SowingCropAmount.setError(getString(R.string.fill_it));
            return;
        }
        if (AreaOfLandSt.isEmpty()){

            AreaOfLandSt = "0";

        }
        if (ServiceProviderNameSt.isEmpty()){
            SowingCropServiceProviderName.setError(getString(R.string.fill_it));
            return;
        }

        if (!(ServiceProviderList.contains(ServiceProviderNameSt))){
            ShowDialog(this,getString(R.string.first_add_service_provider));
            return;
        }
        if (ByPartner&&PartnerNameSt.isEmpty()){
            SowingCropPartnerName.setError(getString(R.string.fill_it));
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
            sowingCropRemark.setValue(RemarkList);
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
        SaveItem.put(MyStatic.REMARK,item.getSowingRemark());
        SaveItem.put(CROP_ID,item.getCropId());
        SaveItem.put(MyStatic.AMOUNT,item.getSowingAmount());
        SaveItem.put(MyStatic.AREA_OF_LAND,item.getSowingLandArea());
        SaveItem.put(DATE,item.getSowingDate());
        SaveItem.put(MyStatic.CASH_MODE,item.isCashService());
        SaveItem.put(SERVICE_PROVIDER_NAME,item.getServiceProviderName());
        SaveItem.put(SERVICE_PROVIDER_ID,item.getServiceProviderId());
        SaveItem.put(MyStatic.BY_PARTNER,item.isCheckPartner());
        if (ByPartner){
            SaveItem.put(PARTNER_NAME,item.getPartnerName());
            SaveItem.put(PARTNER_ID,item.getPartnerId());
        }

        sowingCropLocation(CropId).document(Key).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HideProgress();
                if (task.isSuccessful()){
                    purchaseServiceProviderLocation(ServiceProviderNameSt).document(Key).set(SaveItem);
                    postSingleReport(ALL,CropId,CashMode,AmountSt,SOWING);
                    postAllReport(ALL,CashMode,AmountSt,SOWING);
                    if (ByPartner){
                        postSingleReport(PartnerNameSt,CropId,CashMode,AmountSt,SOWING);
                        postAllReport(PartnerNameSt,CashMode,AmountSt,SOWING);
                    }
                    else{
                        postSingleReport(SELF,CropId,CashMode,AmountSt,SOWING);
                        postAllReport(SELF,CashMode,AmountSt,SOWING);
                    }
                    if (!CashMode){
                        addRemainingAmountServiceProvider(ServiceProviderNameSt,Double.parseDouble(AmountSt));
                        postAllRemainingReport(ALL,AmountSt, MyStatic.SERVICE_PROVIDER);
                        if (ByPartner){
                            postAllRemainingReport(PartnerNameSt,AmountSt, MyStatic.SERVICE_PROVIDER);
                        }
                        else{
                            postAllRemainingReport(SELF,AmountSt, MyStatic.SERVICE_PROVIDER);
                        }
                    }


                    ShowDialog(SowingCrop.this,getString(R.string.saved));
                }
                else{
                    ShowDialog(SowingCrop.this,getString(R.string.try_again_later_or_send_feedback));
                }
            }
        });

    }



    public void cancelSowingCrop(View view){
        finish();
    }


}
