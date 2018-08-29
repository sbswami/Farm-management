package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AREA_OF_LAND;
import static com.sanshy.farmmanagement.MyStatic.BUYER_ID;
import static com.sanshy.farmmanagement.MyStatic.BUYER_NAME;
import static com.sanshy.farmmanagement.MyStatic.CASH_MODE;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialog;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.INCOME;
import static com.sanshy.farmmanagement.MyStatic.PARTNER;
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
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.addRemainingAmountBuyer;
import static com.sanshy.farmmanagement.MyStatic.buyerList;
import static com.sanshy.farmmanagement.MyStatic.incomeCropLocation;
import static com.sanshy.farmmanagement.MyStatic.incomeCropRemark;
import static com.sanshy.farmmanagement.MyStatic.postAllRemainingReport;
import static com.sanshy.farmmanagement.MyStatic.postAllReport;
import static com.sanshy.farmmanagement.MyStatic.postSingleReport;
import static com.sanshy.farmmanagement.MyStatic.purchaseBuyerLocation;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;
import static com.sanshy.farmmanagement.MyStatic.staticDate;
import static com.sanshy.farmmanagement.MyStatic.CROP_ID;

public class IncomeCrop extends AppCompatActivity {

    TextView IncomeCropTitle,IncomeCropDateShow,IncomeCropMyIncome;
    EditText IncomeCropAmount,IncomeCropAreaOfLand,IncomeCropRate,IncomeCropWeight;
    AutoCompleteTextView IncomeCropRemark,IncomeCropBuyerName;
    RadioGroup IncomeCropModeOfPayment;
    LinearLayout IncomeCropPartnerContainer;

    LinearLayout IncomeCropPartner1,IncomeCropPartner2,IncomeCropPartner3,IncomeCropPartner4,IncomeCropPartner5;
    TextView IncomeCropPartnerName1,IncomeCropPartnerName2,IncomeCropPartnerName3,IncomeCropPartnerName4,IncomeCropPartnerName5;
    EditText IncomeSharePercentagePartner1,IncomeSharePercentagePartner2,IncomeSharePercentagePartner3,IncomeSharePercentagePartner4,IncomeSharePercentagePartner5;
    TextView IncomeShareAmountPartner1,IncomeShareAmountPartner2,IncomeShareAmountPartner3,IncomeShareAmountPartner4,IncomeShareAmountPartner5;

    String CropId;

    String RemarkSt;
    String CropRateSt;
    String CropWeightSt;
    String AreaOfLandSt;
    String BuyerNameSt;
    boolean CashBuyer = true;
    boolean PartnerExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_crop);


        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);

        IncomeCropTitle = findViewById(R.id.income_crop_title);
        IncomeCropDateShow = findViewById(R.id.income_crop_show_date);
        IncomeCropMyIncome = findViewById(R.id.income_crop_my_share);

        IncomeCropAmount = findViewById(R.id.income_crop_amount);
        IncomeCropAreaOfLand = findViewById(R.id.income_crop_land_area_of_service);
        IncomeCropRate = findViewById(R.id.income_crop_rate_per_quantity);
        IncomeCropWeight = findViewById(R.id.income_crop_weight);

        IncomeCropRemark = findViewById(R.id.income_crop_remark);
        IncomeCropBuyerName = findViewById(R.id.income_crop_buyer_name);

        IncomeCropModeOfPayment = findViewById(R.id.income_crop_mode_of_payment);

        IncomeCropPartnerContainer = findViewById(R.id.income_crop_partner_container);

        IncomeCropPartner1 = findViewById(R.id.income_crop_partner_1);
        IncomeCropPartner2 = findViewById(R.id.income_crop_partner_2);
        IncomeCropPartner3 = findViewById(R.id.income_crop_partner_3);
        IncomeCropPartner4 = findViewById(R.id.income_crop_partner_4);
        IncomeCropPartner5 = findViewById(R.id.income_crop_partner_5);

        IncomeCropPartnerName1 = findViewById(R.id.income_crop_partner_name_1);
        IncomeCropPartnerName2 = findViewById(R.id.income_crop_partner_name_2);
        IncomeCropPartnerName3 = findViewById(R.id.income_crop_partner_name_3);
        IncomeCropPartnerName4 = findViewById(R.id.income_crop_partner_name_4);
        IncomeCropPartnerName5 = findViewById(R.id.income_crop_partner_name_5);

        IncomeSharePercentagePartner1 = findViewById(R.id.income_crop_partner_share_percentage_1);
        IncomeSharePercentagePartner2 = findViewById(R.id.income_crop_partner_share_percentage_2);
        IncomeSharePercentagePartner3 = findViewById(R.id.income_crop_partner_share_percentage_3);
        IncomeSharePercentagePartner4 = findViewById(R.id.income_crop_partner_share_percentage_4);
        IncomeSharePercentagePartner5 = findViewById(R.id.income_crop_partner_share_percentage_5);

        IncomeShareAmountPartner1 = findViewById(R.id.income_crop_partner_share_amount_1);
        IncomeShareAmountPartner2 = findViewById(R.id.income_crop_partner_share_amount_2);
        IncomeShareAmountPartner3 = findViewById(R.id.income_crop_partner_share_amount_3);
        IncomeShareAmountPartner4 = findViewById(R.id.income_crop_partner_share_amount_4);
        IncomeShareAmountPartner5 = findViewById(R.id.income_crop_partner_share_amount_5);
        
        IncomeCropWeight.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                double rate = Double.parseDouble(IncomeCropRate.getText().toString().isEmpty()?"0":IncomeCropRate.getText().toString());
                double weight = Double.parseDouble(IncomeCropWeight.getText().toString().isEmpty()?"0":IncomeCropWeight.getText().toString());
                
                double Amount = rate*weight;
                
                IncomeCropAmount.setText(String.valueOf(Amount));
                Calculate();
                return false;
            }
        });
        
        IncomeCropAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Calculate();
                return false;
            }
        });
        IncomeSharePercentagePartner1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Calculate();
                return false;
            }
        });
        IncomeSharePercentagePartner2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Calculate();
                return false;
            }
        });
        IncomeSharePercentagePartner3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Calculate();
                return false;
            }
        });
        IncomeSharePercentagePartner4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Calculate();
                return false;
            }
        });
        IncomeSharePercentagePartner5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Calculate();
                return false;
            }
        });

    }

    double P1S = 0;
    double P2S = 0;
    double P3S = 0;
    double P4S = 0;
    double P5S = 0;

    int PPC = 0;
    
    String AmountSt;
    String PN1;
    String PN2;
    String PN3;
    String PN4;
    String PN5;


    ArrayList<String> RemarkList = new ArrayList<>();
    ArrayList<String> BuyerList = new ArrayList<>();
    
    @Override
    protected void onStart() {
        super.onStart();

        ShowProgress(this);
        singleCropLocation.document(CropId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Map<String, Object> CloudItem = documentSnapshot.getData();

                    PartnerExist = (boolean) CloudItem.get(PARTNER);
                    long pC = (long) CloudItem.get(PARTNER_COUNT);
                    int PC = (int) pC;
                    PPC = PC;
                    if (PartnerExist)
                        IncomeCropPartnerContainer.setVisibility(View.VISIBLE);
                    switch (PC){
                        case 5 : IncomeCropPartner5.setVisibility(View.VISIBLE);
                        P5S = (Double) CloudItem.get(PARTNER_SHARE_5);
                            IncomeSharePercentagePartner5.setText(CloudItem.get(PARTNER_SHARE_5).toString());
                            IncomeCropPartnerName5.setText(CloudItem.get(PARTNER_NAME_5).toString());
                            PN5 = CloudItem.get(PARTNER_NAME_5).toString();
                            
                        case 4 : IncomeCropPartner4.setVisibility(View.VISIBLE);
                            P4S = (Double) CloudItem.get(PARTNER_SHARE_4);
                            IncomeSharePercentagePartner4.setText(CloudItem.get(PARTNER_SHARE_4).toString());
                            IncomeCropPartnerName4.setText(CloudItem.get(PARTNER_NAME_4).toString());
                            PN4 = CloudItem.get(PARTNER_NAME_4).toString();
                            
                        case 3 : IncomeCropPartner3.setVisibility(View.VISIBLE);
                            P3S = (Double) CloudItem.get(PARTNER_SHARE_3);
                            IncomeSharePercentagePartner3.setText(CloudItem.get(PARTNER_SHARE_3).toString());
                            IncomeCropPartnerName3.setText(CloudItem.get(PARTNER_NAME_3).toString());
                            PN3 = CloudItem.get(PARTNER_NAME_3).toString();
                            
                        case 2 : IncomeCropPartner2.setVisibility(View.VISIBLE);
                            P2S = (Double) CloudItem.get(PARTNER_SHARE_2);
                            IncomeSharePercentagePartner2.setText(CloudItem.get(PARTNER_SHARE_2).toString());
                            IncomeCropPartnerName2.setText(CloudItem.get(PARTNER_NAME_2).toString());
                            PN2 = CloudItem.get(PARTNER_NAME_2).toString();
                            
                        case 1 : IncomeCropPartner1.setVisibility(View.VISIBLE);
                            P1S = (Double) CloudItem.get(PARTNER_SHARE_1);
                            IncomeSharePercentagePartner1.setText(CloudItem.get(PARTNER_SHARE_1).toString());
                            IncomeCropPartnerName1.setText(CloudItem.get(PARTNER_NAME_1).toString());
                            PN1 = CloudItem.get(PARTNER_NAME_1).toString();
                            
                    }

                }
                HideProgress();
            }
        });

        incomeCropRemark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(IncomeCrop.this,IncomeCropRemark,RemarkList);
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
                    setSnipper(IncomeCrop.this,IncomeCropBuyerName, BuyerList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    
    double PA5;
    double PA4;
    double PA3;
    double PA2;
    double PA1;
    
    double MA;
    
    public void Calculate(){
        AmountSt = IncomeCropAmount.getText().toString();
        double amount = 0.0;
        if (AmountSt.isEmpty()){
            Toast.makeText(this, getString(R.string.fill_it), Toast.LENGTH_SHORT).show();
            return;
        }else{
            amount = Double.parseDouble(AmountSt);
        }
        switch (PPC){
            case 0 : IncomeCropMyIncome.setText(AmountSt);
            MA = amount;
            break;
            case 5 : 
                P5S = Double.parseDouble(IncomeSharePercentagePartner5.getText().toString().isEmpty()?"0":IncomeSharePercentagePartner5.getText().toString());
                if (P5S==0){
                    IncomeSharePercentagePartner5.setText("0");
                }
                PA5 = P5S*(amount/100);
                IncomeShareAmountPartner5.setText(String.valueOf(PA5));
                
            case 4 :
                P4S = Double.parseDouble(IncomeSharePercentagePartner4.getText().toString().isEmpty()?"0":IncomeSharePercentagePartner4.getText().toString());
                if (P4S==0){
                    IncomeSharePercentagePartner4.setText("0");
                }
                PA4 = P4S*(amount/100);
                IncomeShareAmountPartner4.setText(String.valueOf(PA4));
                
            case 3 :
                P3S = Double.parseDouble(IncomeSharePercentagePartner3.getText().toString().isEmpty()?"0":IncomeSharePercentagePartner3.getText().toString());
                if (P3S==0){
                    IncomeSharePercentagePartner3.setText("0");
                }
                PA3 = P3S*(amount/100);
                IncomeShareAmountPartner3.setText(String.valueOf(PA3));
                
            case 2 :
                P2S = Double.parseDouble(IncomeSharePercentagePartner2.getText().toString().isEmpty()?"0":IncomeSharePercentagePartner2.getText().toString());
                if (P2S==0){
                    IncomeSharePercentagePartner2.setText("0");
                }
                PA2 = P2S*(amount/100);
                IncomeShareAmountPartner2.setText(String.valueOf(PA2));
                
            case 1 :
                P1S = Double.parseDouble(IncomeSharePercentagePartner1.getText().toString().isEmpty()?"0":IncomeSharePercentagePartner1.getText().toString());
                if (P1S==0){
                    IncomeSharePercentagePartner1.setText("0");
                }
                PA1 = P1S*(amount/100);
                IncomeShareAmountPartner1.setText(String.valueOf(PA1));
                
                double elseShare = P1S+P2S+P3S+P4S+P5S;
                double SelfRemain = 100 - elseShare;
                if (SelfRemain<0){
                    Toast.makeText(this, getString(R.string.share_must_below_100), Toast.LENGTH_SHORT).show();
                }else {
                    MA = SelfRemain*(amount/100);
                    IncomeCropMyIncome.setText(String.valueOf(MA));
                }
                
        }
    }


    
    public void saveIncomeCropBt(View view){
        RemarkSt = IncomeCropRemark.getText().toString();
        CropRateSt = IncomeCropRate.getText().toString();
        CropWeightSt = IncomeCropWeight.getText().toString();
        AreaOfLandSt = IncomeCropAreaOfLand.getText().toString();
        BuyerNameSt = IncomeCropBuyerName.getText().toString();
        AmountSt = IncomeCropAmount.getText().toString();
        
        CashBuyer = IncomeCropModeOfPayment.getCheckedRadioButtonId() == R.id.income_crop_cash_mode;
        
        if (RemarkSt.isEmpty()){
            IncomeCropRemark.setError(getString(R.string.fill_it));
            return;
        }
        if (CropWeightSt.isEmpty()){
            IncomeCropWeight.setError(getString(R.string.fill_it));
            return;
        }
        if (CropRateSt.isEmpty()){
            IncomeCropRate.setError(getString(R.string.fill_it));
            return;
        }
        if (BuyerNameSt.isEmpty()){
            IncomeCropBuyerName.setError(getString(R.string.fill_it));
            return;
        }
        if (AmountSt.isEmpty()){
            IncomeCropAmount.setError(getString(R.string.fill_it));
            return;
        }
        if (!isDate){
            ShowDialog(this,getString(R.string.please_choose_any_date));
            return;
        }
        if (!BuyerList.contains(BuyerNameSt)){
            ShowDialog(this,getString(R.string.first_add_buyer));
            return;
        }
        if (AreaOfLandSt.isEmpty()){
            AreaOfLandSt="0";
        }
        
        
        
        SaveIt();
        
        
    }

    private void SaveIt() {
        final String Key = TimeStampKey(new Date());
        SingleIncomeCropData item = new SingleIncomeCropData(
                Key,
                RemarkSt,
                CropId,
                Double.parseDouble(AmountSt),
                Double.parseDouble(CropRateSt),
                Double.parseDouble(CropWeightSt),
                MA,
                staticDate,
                Double.parseDouble(AreaOfLandSt),
                CashBuyer,
                BuyerNameSt,
                BuyerNameSt,
                PPC,
                PartnerExist);
        switch (PPC){
            case 1 : item = new SingleIncomeCropData(
                    Key,
                    RemarkSt,
                CropId,
                    Double.parseDouble(AmountSt),
                    Double.parseDouble(CropRateSt),
                    Double.parseDouble(CropWeightSt),
                    MA,
                    staticDate,
                    Double.parseDouble(AreaOfLandSt),
                    CashBuyer,
                    BuyerNameSt,
                    BuyerNameSt,
                    PPC,
                    PartnerExist,
                    PA1,P1S,PN1,PN1);
            break;
            case 2 : item = new SingleIncomeCropData(
                    Key,
                    RemarkSt,
                CropId,
                    Double.parseDouble(AmountSt),
                    Double.parseDouble(CropRateSt),
                    Double.parseDouble(CropWeightSt),
                    MA,
                    staticDate,
                    Double.parseDouble(AreaOfLandSt),
                    CashBuyer,
                    BuyerNameSt,
                    BuyerNameSt,
                    PPC,
                    PartnerExist,
                    PA1,P1S,PN1,PN1,
                    PA2,P2S,PN2,PN2);
            break;
            case 3 : item = new SingleIncomeCropData(
                    Key,
                    RemarkSt,
                CropId,
                    Double.parseDouble(AmountSt),
                    Double.parseDouble(CropRateSt),
                    Double.parseDouble(CropWeightSt),
                    MA,
                    staticDate,
                    Double.parseDouble(AreaOfLandSt),
                    CashBuyer,
                    BuyerNameSt,
                    BuyerNameSt,
                    PPC,
                    PartnerExist,
                    PA1,P1S,PN1,PN1,
                    PA2,P2S,PN2,PN2,
                    PA3,P3S,PN3,PN3);
            break;
            case 4 : item = new SingleIncomeCropData(
                    Key,
                    RemarkSt,
                CropId,
                    Double.parseDouble(AmountSt),
                    Double.parseDouble(CropRateSt),
                    Double.parseDouble(CropWeightSt),
                    MA,
                    staticDate,
                    Double.parseDouble(AreaOfLandSt),
                    CashBuyer,
                    BuyerNameSt,
                    BuyerNameSt,
                    PPC,
                    PartnerExist,
                    PA1,P1S,PN1,PN1,
                    PA2,P2S,PN2,PN2,
                    PA3,P3S,PN3,PN3,
                    PA4,P4S,PN4,PN4);
                break;
                
            case 5 : item = new SingleIncomeCropData(
                    Key,
                    RemarkSt,
                CropId,
                    Double.parseDouble(AmountSt),
                    Double.parseDouble(CropRateSt),
                    Double.parseDouble(CropWeightSt),
                    MA,
                    staticDate,
                    Double.parseDouble(AreaOfLandSt),
                    CashBuyer,
                    BuyerNameSt,
                    BuyerNameSt,
                    PPC,
                    PartnerExist,
                    PA1,P1S,PN1,PN1,
                    PA2,P2S,PN2,PN2,
                    PA3,P3S,PN3,PN3,
                    PA4,P4S,PN4,PN4,
                    PA5,P5S,PN5,PN5);
                break;
        }
        
        final Map<String, Object> SaveItem = new HashMap<>();
        
        SaveItem.put(ID,item.getIncomeId());
        SaveItem.put(REMARK,item.getIncomeRemark());
        SaveItem.put(CROP_ID,item.getCropId());
        SaveItem.put(AMOUNT,item.getIncomeAmount());
        SaveItem.put(MyStatic.RATE, item.getIncomeCropRate());
        SaveItem.put(MyStatic.WEIGHT,item.getIncomeCropWeight());
        SaveItem.put(MyStatic.SELF_INCOME,item.getSelfShareAmount());
        SaveItem.put(DATE,item.getIncomeDate());
        SaveItem.put(AREA_OF_LAND,item.getIncomeLandArea());
        SaveItem.put(CASH_MODE,item.isCashBuyer());
        SaveItem.put(BUYER_NAME,item.getBuyerCropsName());
        SaveItem.put(BUYER_ID,item.getBuyerCropsId());
        SaveItem.put(PARTNER_COUNT,item.getPartnerCount());
        SaveItem.put(PARTNER,item.isCheckPartner());
        
        switch (PPC){
            case 5 : 
                SaveItem.put(MyStatic.PARTNER_AMOUNT_5,PA5);
                SaveItem.put(PARTNER_NAME_5,PN5);
                SaveItem.put(PARTNER_SHARE_5,P5S);
                SaveItem.put(PARTNER_ID_5,PN5);
                
            case 4 :
                SaveItem.put(MyStatic.PARTNER_AMOUNT_4,PA4);
                SaveItem.put(PARTNER_NAME_4,PN4);
                SaveItem.put(PARTNER_SHARE_4,P4S);
                SaveItem.put(PARTNER_ID_4,PN4);
                
            case  3 :
                SaveItem.put(MyStatic.PARTNER_AMOUNT_3,PA3);
                SaveItem.put(PARTNER_NAME_3,PN3);
                SaveItem.put(PARTNER_SHARE_3,P3S);
                SaveItem.put(PARTNER_ID_3,PN3);
                
            case 2 :
                SaveItem.put(MyStatic.PARTNER_AMOUNT_2,PA2);
                SaveItem.put(PARTNER_NAME_2,PN2);
                SaveItem.put(PARTNER_SHARE_2,P2S);
                SaveItem.put(PARTNER_ID_2,PN2);
                
            case 1 :
                SaveItem.put(MyStatic.PARTNER_AMOUNT_1,PA1);
                SaveItem.put(PARTNER_NAME_1,PN1);
                SaveItem.put(PARTNER_SHARE_1,P1S);
                SaveItem.put(PARTNER_ID_1,PN1);
                
        }

        ShowProgress(this);
        incomeCropLocation(CropId).document(Key).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HideProgress();
                if (task.isSuccessful()){
                    purchaseBuyerLocation(BuyerNameSt).document(Key).set(SaveItem);
                    postSingleReport(ALL,CropId,CashBuyer,AmountSt,INCOME);
                    postAllReport(ALL,CashBuyer,AmountSt,INCOME);
                    if (!CashBuyer){
                        addRemainingAmountBuyer(BuyerNameSt,Double.parseDouble(AmountSt));
                        postAllRemainingReport(ALL,AmountSt, MyStatic.BUYER);
                        if (PartnerExist) {
                            switch (PPC) {
                                case 5:
                                    postAllRemainingReport(PN5,String.valueOf(PA5), MyStatic.BUYER);
                                case 4:
                                    postAllRemainingReport(PN4,String.valueOf(PA4), MyStatic.BUYER);
                                case 3:
                                    postAllRemainingReport(PN3,String.valueOf(PA3), MyStatic.BUYER);
                                case 2:
                                    postAllRemainingReport(PN2,String.valueOf(PA2), MyStatic.BUYER);
                                case 1:
                                    postAllRemainingReport(PN1,String.valueOf(PA1), MyStatic.BUYER);
                            }
                        }
                        postAllRemainingReport(SELF,String.valueOf(MA), MyStatic.BUYER);
                    }

                    if (PartnerExist) {
                        switch (PPC) {
                            case 5:
                                postSingleReport(PN5, CropId,CashBuyer,String.valueOf(PA5),INCOME);
                                postAllReport(PN5,CashBuyer,String.valueOf(PA5),INCOME);
                            case 4:
                                postSingleReport(PN4, CropId,CashBuyer,String.valueOf(PA4),INCOME);
                                postAllReport(PN4,CashBuyer,String.valueOf(PA4),INCOME);
                            case 3:
                                postSingleReport(PN3, CropId,CashBuyer,String.valueOf(PA3),INCOME);
                                postAllReport(PN3,CashBuyer,String.valueOf(PA3),INCOME);
                            case 2:
                                postSingleReport(PN2, CropId,CashBuyer,String.valueOf(PA2),INCOME);
                                postAllReport(PN2,CashBuyer,String.valueOf(PA2),INCOME);
                            case 1:
                                postSingleReport(PN1, CropId,CashBuyer,String.valueOf(PA1),INCOME);
                                postAllReport(PN1,CashBuyer,String.valueOf(PA1),INCOME);
                        }
                    }
                    postSingleReport(SELF,CropId,CashBuyer,String.valueOf(MA),INCOME);
                    postAllReport(SELF,CashBuyer,String.valueOf(MA),INCOME);

                    ShowDialog(IncomeCrop.this, getString(R.string.saved));
                }
                else{
                    ShowDialog(IncomeCrop.this,getString(R.string.try_again_later_or_send_feedback));
                }
            }
        });
        
    }
    
    public void cancelIncomeCropBt(View view){
        finish();
    }
    boolean isDate = false;
    public void dateChooserIncomeCropBt(View view){
        isDate = ChooseDateDialog(this,IncomeCropDateShow);
    }
}
