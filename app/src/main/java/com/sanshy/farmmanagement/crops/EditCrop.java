package com.sanshy.farmmanagement.crops;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.doubleclick.CustomRenderedAd;
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

import static com.sanshy.farmmanagement.MyStatic.AREA_OF_LAND;
import static com.sanshy.farmmanagement.MyStatic.CROP_TYPE;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_NAME;
import static com.sanshy.farmmanagement.MyStatic.CurrentUserId;
import static com.sanshy.farmmanagement.MyStatic.DATE_OF_CROP_ADDING;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.LAND_OF_AREA;
import static com.sanshy.farmmanagement.MyStatic.NAME;
import static com.sanshy.farmmanagement.MyStatic.PARTNER;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_COUNT;
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
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.TimeStampKey;
import static com.sanshy.farmmanagement.MyStatic.YEAR_OF_REAPING;
import static com.sanshy.farmmanagement.MyStatic.YEAR_OF_SOWING;
import static com.sanshy.farmmanagement.MyStatic.cropType;
import static com.sanshy.farmmanagement.MyStatic.getCurrentUserId;
import static com.sanshy.farmmanagement.MyStatic.partnerList;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;

public class EditCrop extends AppCompatActivity {

    EditText CropName,YearOfSowing,YearOfReaping,AreaOfLand;
    AutoCompleteTextView CropType;

    CheckBox PartnerExistenceChecker;

    int pC = 0;
    int oldPC = 0;

    LinearLayout P1,P2,P3,P4,P5;
    LinearLayout PAll;
    EditText SharingPercentagePartner1,SharingPercentagePartner2,SharingPercentagePartner3,SharingPercentagePartner4,SharingPercentagePartner5;
    AutoCompleteTextView PartnerName1, PartnerName2, PartnerName3, PartnerName4, PartnerName5;

    ArrayList<String> CropTypeHintList = new ArrayList<>();
    ArrayList<String> PartnerList = new ArrayList<>();

    String CurrentCropName;
    String CurrentCropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_crop);

        Intent intent = getIntent();
        CurrentCropId = intent.getStringExtra(CURRENT_CROP_ID);

        try{
            CropName = findViewById(R.id.edit_crop_name);
            YearOfSowing = findViewById(R.id.edit_crop_year_of_sowing);
            YearOfReaping = findViewById(R.id.edit_crop_year_of_reaping);
            AreaOfLand = findViewById(R.id.edit_crop_land_area);

            PartnerExistenceChecker = findViewById(R.id.edit_crop_partner_exits_chooser);

            CropType = findViewById(R.id.edit_crop_type);

            PAll = findViewById(R.id.edit_crop_exist_contatner);

            P1 = findViewById(R.id.partner_1_container);
            P2 = findViewById(R.id.partner_2_container);
            P3 = findViewById(R.id.partner_3_container);
            P4 = findViewById(R.id.partner_4_container);
            P5 = findViewById(R.id.partner_5_container);

            SharingPercentagePartner1 = findViewById(R.id.edit_crop_partner_percentage1);
            SharingPercentagePartner2 = findViewById(R.id.edit_crop_partner_percentage2);
            SharingPercentagePartner3 = findViewById(R.id.edit_crop_partner_percentage3);
            SharingPercentagePartner4 = findViewById(R.id.edit_crop_partner_percentage4);
            SharingPercentagePartner5 = findViewById(R.id.edit_crop_partner_percentage5);

            PartnerName1 = findViewById(R.id.edit_crop_partner_name1);
            PartnerName2 = findViewById(R.id.edit_crop_partner_name2);
            PartnerName3 = findViewById(R.id.edit_crop_partner_name3);
            PartnerName4 = findViewById(R.id.edit_crop_partner_name4);
            PartnerName5 = findViewById(R.id.edit_crop_partner_name5);

            PartnerExistenceChecker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        PAll.setVisibility(View.VISIBLE);
                    }else{
                        PAll.setVisibility(View.GONE);
                        pC = 0;
                        P1.setVisibility(View.GONE);
                        P2.setVisibility(View.GONE);
                        P3.setVisibility(View.GONE);
                        P4.setVisibility(View.GONE);
                        P5.setVisibility(View.GONE);

                    }
                    PartnerExist = isChecked;
                }
            });

        }catch (Exception ex){
            MyStatic.ShowDialog(this,ex.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        singleCropLocation.document(CurrentCropId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                YearOfSowing.setText(documentSnapshot.getString(YEAR_OF_SOWING));
                YearOfReaping.setText(documentSnapshot.getString(YEAR_OF_REAPING));
                AreaOfLand.setText(String.valueOf(documentSnapshot.getDouble(LAND_OF_AREA)));
                CropType.setText(documentSnapshot.getString(CROP_TYPE));
                CropName.setText(documentSnapshot.getString(NAME));

                if (documentSnapshot.getBoolean(PARTNER)){
                    PartnerExistenceChecker.setChecked(true);
                    PAll.setVisibility(View.VISIBLE);
                    long PC = documentSnapshot.getLong(PARTNER_COUNT);
                    pC = (int) PC;
                    oldPC = pC;
                    switch (pC){
                        case 5:
                            P5.setVisibility(View.VISIBLE);
                            PartnerName5.setText(documentSnapshot.getString(PARTNER_NAME_5));
                            SharingPercentagePartner5.setText(String.valueOf(documentSnapshot.getDouble(PARTNER_SHARE_5)));

                        case 4:
                            P4.setVisibility(View.VISIBLE);
                            PartnerName4.setText(documentSnapshot.getString(PARTNER_NAME_4));
                            SharingPercentagePartner4.setText(String.valueOf(documentSnapshot.getDouble(PARTNER_SHARE_4)));

                        case 3:
                            P3.setVisibility(View.VISIBLE);
                            PartnerName3.setText(documentSnapshot.getString(PARTNER_NAME_3));
                            SharingPercentagePartner3.setText(String.valueOf(documentSnapshot.getDouble(PARTNER_SHARE_3)));

                        case 2:
                            P2.setVisibility(View.VISIBLE);
                            PartnerName2.setText(documentSnapshot.getString(PARTNER_NAME_2));
                            SharingPercentagePartner2.setText(String.valueOf(documentSnapshot.getDouble(PARTNER_SHARE_2)));

                        case 1:
                            P1.setVisibility(View.VISIBLE);
                            PartnerName1.setText(documentSnapshot.getString(PARTNER_NAME_1));
                            SharingPercentagePartner1.setText(String.valueOf(documentSnapshot.getDouble(PARTNER_SHARE_1)));

                    }

                }else{
                    PartnerExistenceChecker.setChecked(false);
                    PAll.setVisibility(View.GONE);
                }

            }
        });

        cropType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CropTypeHintList.clear();
                if (dataSnapshot.exists()){
                    CropTypeHintList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(EditCrop.this,CropType,CropTypeHintList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        partnerList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PartnerList.clear();
                if (dataSnapshot.exists()){
                    PartnerList = (ArrayList<String>) dataSnapshot.getValue();
                    setSnipper(EditCrop.this,PartnerName2,PartnerList);
                    setSnipper(EditCrop.this,PartnerName1,PartnerList);
                    setSnipper(EditCrop.this,PartnerName3,PartnerList);
                    setSnipper(EditCrop.this,PartnerName4,PartnerList);
                    setSnipper(EditCrop.this,PartnerName5,PartnerList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void NumberOfPartnerChooseBt(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String[] pCount = {"1","2","3","4","5"};

        builder.setTitle(getString(R.string.number_of_partners))
                .setSingleChoiceItems(pCount, 5, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EditCrop.this, getString(R.string.partner)+" = "+pCount[which], Toast.LENGTH_SHORT).show();
                        pC = Integer.parseInt(pCount[which]);
                    }
                })
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        pC = Integer.parseInt(pCount[which]);
                        switch (pC){
                            case 5 : P5.setVisibility(View.VISIBLE);
                            case 4 : P4.setVisibility(View.VISIBLE);
                            case 3 : P3.setVisibility(View.VISIBLE);
                            case 2 : P2.setVisibility(View.VISIBLE);
                            case 1 : P1.setVisibility(View.VISIBLE);
                        }
                    }
                });

        builder.create().show();
    }

    String NameString;
    String YearOfSowingString;
    String YearOfReapingString;
    String LandOfAreaString;
    String CropTypeString;
    boolean PartnerExist;

    String PNString1;
    String PNString2;
    String PNString3;
    String PNString4;
    String PNString5;

    String PSPString1;
    String PSPString2;
    String PSPString3;
    String PSPString4;
    String PSPString5;

    String CropId;

    SingleCropData item;

    public void EditCropBt(View view){

        NameString = CropName.getText().toString();
        YearOfSowingString = YearOfSowing.getText().toString();
        YearOfReapingString = YearOfReaping.getText().toString();
        LandOfAreaString = AreaOfLand.getText().toString();
        CropTypeString = CropType.getText().toString();

        CropId = CurrentCropId;

        item = new SingleCropData(CropId,NameString,CropTypeString,YearOfSowingString,YearOfReapingString,new Date(),Double.parseDouble(LandOfAreaString),PartnerExist,pC);

        if (!(CropTypeHintList.contains(CropTypeString))){
            CropTypeHintList.add(CropTypeString);
            cropType.setValue(CropTypeHintList);
        }

        if (NameString.isEmpty()){
            CropName.setError(getString(R.string.fill_it));
            return;
        }
        if (YearOfReapingString.isEmpty()){
            YearOfReaping.setError(getString(R.string.fill_it));
            return;
        }
        if (YearOfSowingString.isEmpty()){
            YearOfSowing.setError(getString(R.string.fill_it));
            return;
        }
        if (LandOfAreaString.isEmpty()){
            AreaOfLand.setError(getString(R.string.fill_it));
            return;
        }
        if ((YearOfSowingString.length()!=4)||(YearOfReapingString.length()!=4)){
            ShowDialog(this,getString(R.string.please_enter_a_year));
            return;
        }
        if (pC<oldPC){
            ShowDialog(this,getString(R.string.you_are_removing_some_partners_that_will_cause));
            return;
        }



        if (PartnerExist){
            switch (pC){
                case 5 : PNString5 = PartnerName5.getText().toString();
                    PSPString5 = SharingPercentagePartner5.getText().toString();
                    if (PNString5.isEmpty()){
                        PartnerName5.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (PSPString5.isEmpty()){
                        SharingPercentagePartner5.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (0>=Double.parseDouble(PSPString5)||Double.parseDouble(PSPString5)>=100){
                        SharingPercentagePartner5.setError(getString(R.string.share_must_below_100));
                        return;
                    }
                    if (!(PartnerList.contains(PNString5))){
                        ShowDialog(this,getString(R.string.partner_does_not_exist));
                        return;
                    }
                case 4 : PNString4 = PartnerName4.getText().toString();
                    PSPString4 = SharingPercentagePartner4.getText().toString();
                    if (PNString4.isEmpty()){
                        PartnerName4.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (PSPString4.isEmpty()){
                        SharingPercentagePartner4.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (0>=Double.parseDouble(PSPString4)||Double.parseDouble(PSPString4)>=100){
                        SharingPercentagePartner4.setError(getString(R.string.share_must_below_100));
                        return;
                    }

                    if (!(PartnerList.contains(PNString4))){
                        ShowDialog(this,getString(R.string.partner_does_not_exist));
                        return;
                    }
                case 3 : PNString3 = PartnerName3.getText().toString();
                    PSPString3 = SharingPercentagePartner2.getText().toString();
                    if (PNString3.isEmpty()){
                        PartnerName3.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (PSPString3.isEmpty()){
                        SharingPercentagePartner3.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (0>=Double.parseDouble(PSPString3)||Double.parseDouble(PSPString3)>=100){
                        SharingPercentagePartner3.setError(getString(R.string.share_must_below_100));
                        return;
                    }
                    if (!(PartnerList.contains(PNString3))){
                        ShowDialog(this,getString(R.string.partner_does_not_exist));
                        return;
                    }
                case 2 : PNString2 = PartnerName2.getText().toString();
                    PSPString2 = SharingPercentagePartner2.getText().toString();
                    if (PNString2.isEmpty()){
                        PartnerName2.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (PSPString2.isEmpty()){
                        SharingPercentagePartner2.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (0>=Double.parseDouble(PSPString2)||Double.parseDouble(PSPString2)>=100){
                        SharingPercentagePartner2.setError(getString(R.string.share_must_below_100));
                        return;
                    }

                    if (!(PartnerList.contains(PNString2))){
                        ShowDialog(this,getString(R.string.partner_does_not_exist));
                        return;
                    }
                case 1 : PNString1 = PartnerName1.getText().toString();
                    PSPString1 = SharingPercentagePartner1.getText().toString();
                    if (PNString1.isEmpty()){
                        PartnerName1.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (PSPString1.isEmpty()){
                        SharingPercentagePartner1.setError(getString(R.string.fill_it));
                        return;
                    }
                    if (0>=Double.parseDouble(PSPString1)||Double.parseDouble(PSPString1)>=100){
                        SharingPercentagePartner1.setError(getString(R.string.share_must_below_100));
                        return;
                    }
                    if (!(PartnerList.contains(PNString1))){
                        ShowDialog(this,getString(R.string.partner_does_not_exist));
                        return;
                    }
            }
            switch (pC){
                case 1 :
                    item = new SingleCropData(CropId,NameString,CropTypeString,YearOfSowingString,YearOfReapingString,new Date(),Double.parseDouble(LandOfAreaString),PartnerExist,pC,PNString1,PNString1,Double.parseDouble(PSPString1));
                    break;

                case 2 : if ((Double.parseDouble(PSPString1)+Double.parseDouble(PSPString2))>100){
                    ShowDialog(this,getString(R.string.share_must_below_100));
                    return;
                }

                    if (PNString1.equals(PNString2)){
                        ShowDialog(this,getString(R.string.duplicate_partner_entered));
                        return;
                    }
                    item = new SingleCropData(CropId,NameString,CropTypeString,YearOfSowingString,YearOfReapingString,new Date(),Double.parseDouble(LandOfAreaString),PartnerExist,pC,PNString1,PNString1,Double.parseDouble(PSPString1),PNString2,PNString2,Double.parseDouble(PSPString2));
                    break;

                case 3 : if ((Double.parseDouble(PSPString1)+Double.parseDouble(PSPString2)+Double.parseDouble(PSPString3))>100){
                    ShowDialog(this,getString(R.string.share_must_below_100));
                    return;
                }
                    if ((PNString1.equals(PNString2))||(PNString1.equals(PNString3))||((PNString2.equals(PNString3)))){
                        ShowDialog(this,getString(R.string.duplicate_partner_entered));
                        return;
                    }
                    item = new SingleCropData(CropId,NameString,CropTypeString,YearOfSowingString,YearOfReapingString,new Date(),Double.parseDouble(LandOfAreaString),PartnerExist,pC,PNString1,PNString1,Double.parseDouble(PSPString1),PNString2,PNString2,Double.parseDouble(PSPString2),PNString3,PNString3,Double.parseDouble(PSPString3));
                    break;

                case 4 : if ((Double.parseDouble(PSPString1)+Double.parseDouble(PSPString2)+Double.parseDouble(PSPString3)+Double.parseDouble(PSPString4))>100){
                    ShowDialog(this,getString(R.string.share_must_below_100));
                    return;
                }
                    if ((PNString1.equals(PNString2))||(PNString1.equals(PNString3))||(PNString1.equals(PNString4))||((PNString2.equals(PNString3))||(PNString2.equals(PNString4))||(PNString3.equals(PNString4)))){
                        ShowDialog(this,getString(R.string.duplicate_partner_entered));
                        return;
                    }
                    item = new SingleCropData(CropId,NameString,CropTypeString,YearOfSowingString,YearOfReapingString,new Date(),Double.parseDouble(LandOfAreaString),PartnerExist,pC,PNString1,PNString1,Double.parseDouble(PSPString1),PNString2,PNString2,Double.parseDouble(PSPString2),PNString3,PNString3,Double.parseDouble(PSPString3),PNString4,PNString4,Double.parseDouble(PSPString4));
                    break;

                case 5 : if ((Double.parseDouble(PSPString1)+Double.parseDouble(PSPString2)+Double.parseDouble(PSPString3)+Double.parseDouble(PSPString4)+Double.parseDouble(PSPString5))>100){
                    ShowDialog(this,getString(R.string.share_must_below_100));
                    return;
                }
                    if ((PNString1.equals(PNString2))||(PNString1.equals(PNString3))||(PNString1.equals(PNString4))||(PNString1.equals(PNString5))||((PNString2.equals(PNString3))||(PNString2.equals(PNString4))||(PNString2.equals(PNString5))||(PNString3.equals(PNString4))||(PNString3.equals(PNString5))||(PNString4.equals(PNString5)))){
                        ShowDialog(this,getString(R.string.duplicate_partner_entered));
                        return;
                    }
                    item = new SingleCropData(CropId,NameString,CropTypeString,YearOfSowingString,YearOfReapingString,new Date(),Double.parseDouble(LandOfAreaString),PartnerExist,pC,PNString1,PNString1,Double.parseDouble(PSPString1),PNString2,PNString2,Double.parseDouble(PSPString2),PNString3,PNString3,Double.parseDouble(PSPString3),PNString4,PNString4,Double.parseDouble(PSPString4),PNString5,PNString5,Double.parseDouble(PSPString5));
                    break;
            }
        }


        SaveToCloud();

    }
    public void SaveToCloud(){
        ShowProgress(this);

        Map<String, Object> SaveItem = new HashMap<>();
        SaveItem.put(ID,item.getCropId());
        SaveItem.put(NAME,item.getCropName());
        SaveItem.put(CROP_TYPE,item.getCropType());
        SaveItem.put(YEAR_OF_REAPING,item.getYearOfReaping());
        SaveItem.put(YEAR_OF_SOWING,item.getYearOfSowing());
        SaveItem.put(DATE_OF_CROP_ADDING,item.getDateOfCropAdding());
        SaveItem.put(MyStatic.LAND_OF_AREA,item.getLandArea());
        SaveItem.put(MyStatic.PARTNER,item.isPartnerExists());
        SaveItem.put(MyStatic.PARTNER_COUNT,item.getPartnerCount());

        switch (pC){
            case 5 : SaveItem.put(MyStatic.PARTNER_ID_5,item.getPartnerCropId5());
                SaveItem.put(MyStatic.PARTNER_NAME_5,item.getPartnerCropName5());
                SaveItem.put(MyStatic.PARTNER_SHARE_5,item.getPartnerPercentage5());

            case 4 : SaveItem.put(MyStatic.PARTNER_ID_4,item.getPartnerCropId4());
                SaveItem.put(MyStatic.PARTNER_NAME_4,item.getPartnerCropName4());
                SaveItem.put(MyStatic.PARTNER_SHARE_4,item.getPartnerPercentage4());

            case 3 : SaveItem.put(MyStatic.PARTNER_ID_3,item.getPartnerCropId3());
                SaveItem.put(MyStatic.PARTNER_NAME_3,item.getPartnerCropName3());
                SaveItem.put(MyStatic.PARTNER_SHARE_3,item.getPartnerPercentage3());

            case 2 : SaveItem.put(MyStatic.PARTNER_ID_2,item.getPartnerCropId2());
                SaveItem.put(MyStatic.PARTNER_NAME_2,item.getPartnerCropName2());
                SaveItem.put(MyStatic.PARTNER_SHARE_2,item.getPartnerPercentage2());

            case 1 : SaveItem.put(MyStatic.PARTNER_ID_1,item.getPartnerCropId1());
                SaveItem.put(MyStatic.PARTNER_NAME_1,item.getPartnerCropName1());
                SaveItem.put(MyStatic.PARTNER_SHARE_1,item.getPartnerPercentage1());

        }

        singleCropLocation.document(CropId).set(SaveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HideProgress();
                if (task.isSuccessful()){
                    ShowDialog(EditCrop.this,getString(R.string.saved));
                }
                else{
                    ShowDialog(EditCrop.this,getString(R.string.try_again_later_or_send_feedback));
                }
            }
        });

    }
    public void EditCropCancelBt(View view){
        finish();
    }

}
