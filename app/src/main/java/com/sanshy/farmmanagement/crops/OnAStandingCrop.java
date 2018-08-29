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
import static com.sanshy.farmmanagement.MyStatic.ON_A_STANDING;
import static com.sanshy.farmmanagement.MyStatic.PARTNER;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_COUNT;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_1;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_2;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_3;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_4;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_5;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_ID;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER_NAME;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.addRemainingAmountServiceProvider;
import static com.sanshy.farmmanagement.MyStatic.onAStandingCropLocation;
import static com.sanshy.farmmanagement.MyStatic.onAStandingCropRemark;
import static com.sanshy.farmmanagement.MyStatic.postAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.postAllReport;
import static com.sanshy.farmmanagement.MyStatic.postSingleReport;
import static com.sanshy.farmmanagement.MyStatic.purchaseServiceProviderLocation;
import static com.sanshy.farmmanagement.MyStatic.serviceProviderList;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;
import static com.sanshy.farmmanagement.MyStatic.staticDate;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER;

public class OnAStandingCrop extends AppCompatActivity {

    TextView OnAStandingCropTitle,OnAStandingCropDateShow;
    EditText OnAStandingCropAmount,OnAStandingCropAreaOfLand;
    AutoCompleteTextView OnAStandingCropRemark,OnAStandingCropServiceProviderName,OnAStandingCropPartnerName;
    RadioGroup OnAStandingCropModeOfPayment;
    CheckBox OnAStandingCropPaidByPartner;
    LinearLayout OnAStandingCropPaidPartnerContainer;

    String CropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_astanding_crop);

        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);

        OnAStandingCropTitle = findViewById(R.id.on_a_standing_crop_title);
        OnAStandingCropDateShow = findViewById(R.id.on_a_standing_crop_show_date);

        OnAStandingCropAmount = findViewById(R.id.on_a_standing_crop_amount);
        OnAStandingCropAreaOfLand = findViewById(R.id.on_a_standing_crop_land_area_of_service);

        OnAStandingCropRemark = findViewById(R.id.on_a_standing_crop_remark);
        OnAStandingCropServiceProviderName = findViewById(R.id.on_a_standing_crop_service_provider_name);
        OnAStandingCropPartnerName = findViewById(R.id.on_a_standing_crop_partner_name);

        OnAStandingCropModeOfPayment = findViewById(R.id.on_a_standing_crop_mode_of_payment);

        OnAStandingCropPaidByPartner = findViewById(R.id.on_a_standing_crop_paid_by_partner);

        OnAStandingCropPaidPartnerContainer = findViewById(R.id.on_a_standing_crop_paid_partner_container);

        OnAStandingCropPaidByPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    OnAStandingCropPaidPartnerContainer.setVisibility(View.VISIBLE);
                }
                else{
                    OnAStandingCropPaidPartnerContainer.setVisibility(View.GONE);
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

        onAStandingCropRemark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(OnAStandingCrop.this,OnAStandingCropRemark,RemarkList);
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
                    OnAStandingCropPaidByPartner.setVisibility(View.VISIBLE);
                    long pC = (long) documentSnapshot.get(PARTNER_COUNT);
                    int PC = (int) pC;
                    switch (PC){
                        case 5 : PartnerList.add(documentSnapshot.get(PARTNER_NAME_5).toString());
                        case 4 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_4).toString());
                        case 3 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_3).toString());
                        case 2 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_2).toString());
                        case 1 :PartnerList.add(documentSnapshot.get(PARTNER_NAME_1).toString());
                    }
                    setSnipper(OnAStandingCrop.this,OnAStandingCropPartnerName,PartnerList);
                }

            }
        });

        serviceProviderList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ServiceProviderList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(OnAStandingCrop.this,OnAStandingCropServiceProviderName,ServiceProviderList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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



    public void saveOnAStandingCropBt(View view){

        RemarkSt = OnAStandingCropRemark.getText().toString();
        AmountSt = OnAStandingCropAmount.getText().toString();
        AreaOfLandSt = OnAStandingCropAreaOfLand.getText().toString();
        ServiceProviderNameSt = OnAStandingCropServiceProviderName.getText().toString();
        PartnerNameSt = OnAStandingCropPartnerName.getText().toString();

        ByPartner = OnAStandingCropPaidByPartner.isChecked();

        int CashId = OnAStandingCropModeOfPayment.getCheckedRadioButtonId();

        CashMode = R.id.sowing_cash_mode == CashId;

        if(RemarkSt.isEmpty()){
            OnAStandingCropRemark.setError(getString(R.string.fill_it));
            return;
        }
        if (AmountSt.isEmpty()){
            OnAStandingCropAmount.setError(getString(R.string.fill_it));
            return;
        }
        if (AreaOfLandSt.isEmpty()){

            AreaOfLandSt = "0";

        }
        if (ServiceProviderNameSt.isEmpty()){
            OnAStandingCropServiceProviderName.setError(getString(R.string.fill_it));
            return;
        }

        if (!(ServiceProviderList.contains(ServiceProviderNameSt))){
            ShowDialog(this,getString(R.string.first_add_service_provider));
            return;
        }
        if (ByPartner&&PartnerNameSt.isEmpty()){
            OnAStandingCropPartnerName.setError(getString(R.string.fill_it));
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
            onAStandingCropRemark.setValue(RemarkList);
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

        onAStandingCropLocation(CropId).document(Key).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HideProgress();
                if (task.isSuccessful()){
                    purchaseServiceProviderLocation(ServiceProviderNameSt).document(Key).set(SaveItem);
                    postSingleReport(ALL,CropId,CashMode,AmountSt,ON_A_STANDING);
                    postAllReport(ALL,CashMode,AmountSt,ON_A_STANDING);
                    if (ByPartner){
                        postSingleReport(PartnerNameSt,CropId,CashMode,AmountSt,ON_A_STANDING);
                        postAllReport(PartnerNameSt,CashMode,AmountSt,ON_A_STANDING);
                    }
                    else{
                        postSingleReport(SELF,CropId,CashMode,AmountSt,ON_A_STANDING);
                        postAllReport(SELF,CashMode,AmountSt,ON_A_STANDING);
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
                    ShowDialog(OnAStandingCrop.this,getString(R.string.saved));
                }
                else{
                    ShowDialog(OnAStandingCrop.this,getString(R.string.try_again_later_or_send_feedback));
                }
            }
        });

    }


    public void cancelOnAStandingCropBt(View view){
        finish();
    }
    boolean isDate = false;
    public void dateChooserOnAStandingCropBt(View view){
        isDate = ChooseDateDialog(this,OnAStandingCropDateShow);
    }
}
