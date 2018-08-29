package com.sanshy.farmmanagement.crops;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.CROP_TYPE;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_NAME;
import static com.sanshy.farmmanagement.MyStatic.DATE_OF_CROP_ADDING;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.LAND_OF_AREA;
import static com.sanshy.farmmanagement.MyStatic.NAME;
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
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.YEAR_OF_REAPING;
import static com.sanshy.farmmanagement.MyStatic.YEAR_OF_SOWING;
import static com.sanshy.farmmanagement.MyStatic.singleCropBackUpLocation;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;

public class SingleCrop extends AppCompatActivity {

    TextView Name,YOS,YOR,AOL,CT,P;

    String CropId;
    String CropName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_crop);

        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);
        CropName = intent.getStringExtra(CURRENT_CROP_NAME);

        Name = findViewById(R.id.crop_name);
        YOS = findViewById(R.id.year_of_sowing);
        YOR = findViewById(R.id.year_of_reaping);
        AOL = findViewById(R.id.area_of_land);
        CT = findViewById(R.id.crop_type);
        P = findViewById(R.id.partners);

    }

    public void deleteCropBt(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(SingleCrop.this);

        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.are_you_sure_you_want_to_delete);

        builder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                singleCropBackUpLocation.document(CropId).set(CloudItem);
                singleCropLocation.document(CropId).delete();
                finish();
            }
        });
        builder.setPositiveButton(getString(R.string.close),null);

        builder.create().show();
    }
    Map<String, Object> CloudItem = new HashMap<>();
    @Override
    protected void onStart() {
        super.onStart();
        ShowProgress(this);
        singleCropLocation.document(CropId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (!documentSnapshot.exists()){return;}

                CloudItem = documentSnapshot.getData();

                long PC = (Long) CloudItem.get(PARTNER_COUNT);

                int pC = (int) PC;

                SingleCropData Data = new SingleCropData(
                        CloudItem.get(ID).toString(),
                        CloudItem.get(NAME).toString(),
                        CloudItem.get(CROP_TYPE).toString(),
                        CloudItem.get(YEAR_OF_SOWING).toString(),
                        CloudItem.get(YEAR_OF_REAPING).toString(),
                        (Date) CloudItem.get(DATE_OF_CROP_ADDING),
                        (Double) CloudItem.get(LAND_OF_AREA),
                        (Boolean) CloudItem.get(PARTNER),
                        (Long) CloudItem.get(PARTNER_COUNT)
                );

                switch (pC){

                    case 1 :
                        Data  = new SingleCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(NAME).toString(),
                                CloudItem.get(CROP_TYPE).toString(),
                                CloudItem.get(YEAR_OF_SOWING).toString(),
                                CloudItem.get(YEAR_OF_REAPING).toString(),
                                (Date) CloudItem.get(DATE_OF_CROP_ADDING),
                                (Double) CloudItem.get(LAND_OF_AREA),
                                (Boolean) CloudItem.get(PARTNER),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_1)
                        );

                        break;

                    case 2 :
                        Data  = new SingleCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(NAME).toString(),
                                CloudItem.get(CROP_TYPE).toString(),
                                CloudItem.get(YEAR_OF_SOWING).toString(),
                                CloudItem.get(YEAR_OF_REAPING).toString(),
                                (Date) CloudItem.get(DATE_OF_CROP_ADDING),
                                (Double) CloudItem.get(LAND_OF_AREA),
                                (Boolean) CloudItem.get(PARTNER),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_ID_2).toString(),
                                CloudItem.get(PARTNER_NAME_2).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_2)
                        );

                        break;

                    case 3 :
                        Data  = new SingleCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(NAME).toString(),
                                CloudItem.get(CROP_TYPE).toString(),
                                CloudItem.get(YEAR_OF_SOWING).toString(),
                                CloudItem.get(YEAR_OF_REAPING).toString(),
                                (Date) CloudItem.get(DATE_OF_CROP_ADDING),
                                (Double) CloudItem.get(LAND_OF_AREA),
                                (Boolean) CloudItem.get(PARTNER),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_ID_2).toString(),
                                CloudItem.get(PARTNER_NAME_2).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_2),
                                CloudItem.get(PARTNER_ID_3).toString(),
                                CloudItem.get(PARTNER_NAME_3).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_3)
                        );

                        break;

                    case 4 :
                        Data  = new SingleCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(NAME).toString(),
                                CloudItem.get(CROP_TYPE).toString(),
                                CloudItem.get(YEAR_OF_SOWING).toString(),
                                CloudItem.get(YEAR_OF_REAPING).toString(),
                                (Date) CloudItem.get(DATE_OF_CROP_ADDING),
                                (Double) CloudItem.get(LAND_OF_AREA),
                                (Boolean) CloudItem.get(PARTNER),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_ID_2).toString(),
                                CloudItem.get(PARTNER_NAME_2).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_2),
                                CloudItem.get(PARTNER_ID_3).toString(),
                                CloudItem.get(PARTNER_NAME_3).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_3),
                                CloudItem.get(PARTNER_ID_4).toString(),
                                CloudItem.get(PARTNER_NAME_4).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_4)
                        );

                        break;

                    case 5 :
                        Data  = new SingleCropData(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(NAME).toString(),
                                CloudItem.get(CROP_TYPE).toString(),
                                CloudItem.get(YEAR_OF_SOWING).toString(),
                                CloudItem.get(YEAR_OF_REAPING).toString(),
                                (Date) CloudItem.get(DATE_OF_CROP_ADDING),
                                (Double) CloudItem.get(LAND_OF_AREA),
                                (Boolean) CloudItem.get(PARTNER),
                                (Long) CloudItem.get(PARTNER_COUNT),
                                CloudItem.get(PARTNER_ID_1).toString(),
                                CloudItem.get(PARTNER_NAME_1).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_1),
                                CloudItem.get(PARTNER_ID_2).toString(),
                                CloudItem.get(PARTNER_NAME_2).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_2),
                                CloudItem.get(PARTNER_ID_3).toString(),
                                CloudItem.get(PARTNER_NAME_3).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_3),
                                CloudItem.get(PARTNER_ID_4).toString(),
                                CloudItem.get(PARTNER_NAME_4).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_4),
                                CloudItem.get(PARTNER_ID_5).toString(),
                                CloudItem.get(PARTNER_NAME_5).toString(),
                                (Double) CloudItem.get(PARTNER_SHARE_5)
                        );

                        break;
                }

                Name.setText(Data.getCropName());
                YOR.setText(Data.getYearOfReaping());
                YOS.setText(Data.getYearOfSowing());
                AOL.setText(String.valueOf(Data.getLandArea()));
                CT.setText(Data.getCropType());
                P.setText(Data.getPartnerCropName1()+" "+getString(R.string.share)+" "+Data.getPartnerPercentage1()+"\n" +
                        Data.getPartnerCropName2()+" "+getString(R.string.share)+" "+Data.getPartnerPercentage2()+"\n" +
                        Data.getPartnerCropName3()+" "+getString(R.string.share)+" "+Data.getPartnerPercentage3()+"\n" +
                        Data.getPartnerCropName4()+" "+getString(R.string.share)+" "+Data.getPartnerPercentage4()+"\n" +
                        Data.getPartnerCropName5()+" "+getString(R.string.share)+" "+Data.getPartnerPercentage5());

                HideProgress();

            }
        });
    }
}
