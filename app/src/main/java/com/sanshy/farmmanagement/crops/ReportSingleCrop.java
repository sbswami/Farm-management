package com.sanshy.farmmanagement.crops;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.BORROW_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.CASH_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.CROP_WAY;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.INCOME;
import static com.sanshy.farmmanagement.MyStatic.ON_A_STANDING;
import static com.sanshy.farmmanagement.MyStatic.PARTNER;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_COUNT;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_1;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_2;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_3;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_4;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME_5;
import static com.sanshy.farmmanagement.MyStatic.REAPING;
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SOWING;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;
import static com.sanshy.farmmanagement.MyStatic.singleCropReportLocation;

public class ReportSingleCrop extends AppCompatActivity {

    ListView SingleReportListView;
    SingleCropReportAdapter myAdapter;
    ArrayList<SingleCropReportSmallList> reportList = new ArrayList<>();

    String CropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_single_crop);

        Intent intent = getIntent();
        CropId = intent.getStringExtra(CURRENT_CROP_ID);

        SingleReportListView = findViewById(R.id.single_report_list);

        myAdapter = new SingleCropReportAdapter(this,reportList);

        SingleReportListView.setAdapter(myAdapter);

        ReportSingleCropAddReportItem(ALL);
    }

    ArrayList<String> FilterList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        FilterList.add(0,getString(R.string.all));
        FilterList.add(1,getString(R.string.self));

        singleCropLocation.document(CropId).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getBoolean(PARTNER)){
                    long pC = documentSnapshot.getLong(PARTNER_COUNT);
                    int PC = (int) pC;
                    switch (PC){
                        case 5:
                            FilterList.add(documentSnapshot.getString(PARTNER_NAME_5));
                        case 4:
                            FilterList.add(documentSnapshot.getString(PARTNER_NAME_4));
                        case 3:
                            FilterList.add(documentSnapshot.getString(PARTNER_NAME_3));
                        case 2:
                            FilterList.add(documentSnapshot.getString(PARTNER_NAME_2));
                        case 1:
                            FilterList.add(documentSnapshot.getString(PARTNER_NAME_1));
                    }
                }
            }
        });
    }

    String FilterStringC;

    public void filterSingleReport(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String temp[] = new String[FilterList.size()];
        for (int i = 0; i < FilterList.size(); i++){
            temp[i] = FilterList.get(i);
        }

        String ChoosedString;

        builder.setSingleChoiceItems(temp, 4, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ReportSingleCrop.this, temp[which], Toast.LENGTH_SHORT).show();

                FilterStringC = temp[which];
                if (which==0){
                    FilterStringC = ALL;
                }else if(which==1){
                    FilterStringC = SELF;
                }

            }
        }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReportSingleCropAddReportItem(FilterStringC);
            }
        });

        builder.create().show();
    }

    public void ReportSingleCropAddReportItem(String FilterType){
        reportList.clear();
        ShowProgress(this);
        singleCropReportLocation(CropId,FilterType).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    String CropWay = (String) documentSnapshot.get(CROP_WAY);
                    String CropWayName = "";
                    switch (CropWay) {
                        case SOWING:
                            CropWayName = getString(R.string.sowing);
                            break;
                        case REAPING:
                            CropWayName = getString(R.string.reaping);
                            break;
                        case ON_A_STANDING:
                            CropWayName = getString(R.string.on_a_standing);
                            break;
                        case INCOME:
                            CropWayName = getString(R.string.income);
                            break;
                    }
                    Map<String, Object> CloudItem = documentSnapshot.getData();
                    double cashAmount = (double) CloudItem.get(CASH_AMOUNT);
                    double borrowAmount = (double) CloudItem.get(BORROW_AMOUNT);
                    double total = cashAmount+borrowAmount;

                    SingleCropReportSmallList item = new SingleCropReportSmallList(CropWayName,String.valueOf(total),String.valueOf(cashAmount),String.valueOf(borrowAmount));
                    reportList.add(item);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }
}
