package com.sanshy.farmmanagement.crops;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.BORROW_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.BUYER;
import static com.sanshy.farmmanagement.MyStatic.CASH_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.CROP_WAY;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.INCOME;
import static com.sanshy.farmmanagement.MyStatic.ON_A_STANDING;
import static com.sanshy.farmmanagement.MyStatic.REAPING;
import static com.sanshy.farmmanagement.MyStatic.REMAINING_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.SELF;
import static com.sanshy.farmmanagement.MyStatic.SERVICE_PROVIDER;
import static com.sanshy.farmmanagement.MyStatic.SOWING;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.cropReportLocation;
import static com.sanshy.farmmanagement.MyStatic.partnerList;

public class AllCropsReport extends AppCompatActivity {

    ListView SingleReportListView;
    SingleCropReportAdapter myAdapter;
    TextView UnpaidAmountBuyerTextView,UnpaidAmountServiceProviderTextView;
    ArrayList<SingleCropReportSmallList> reportList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_crops_report);

        UnpaidAmountBuyerTextView = findViewById(R.id.unpaid_amount_buyer);
        UnpaidAmountServiceProviderTextView = findViewById(R.id.unpaid_amount_service_provider);

        SingleReportListView = findViewById(R.id.report_list);

        myAdapter = new SingleCropReportAdapter(this,reportList);

        SingleReportListView.setAdapter(myAdapter);

        ReportSingleCropAddReportItem(ALL,true);

    }

    ArrayList<String> PartnerList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
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

    String AppliedFilter;

    public void filterSingleReport(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String temp[] = new String[PartnerList.size()+2];
        temp[0] = getString(R.string.all);
        temp[1] = getString(R.string.self);
        for (int i = 0; i < PartnerList.size(); i++){
            int p = i+2;
            temp[p] = PartnerList.get(i);
        }

        builder.setSingleChoiceItems(temp, 4, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AllCropsReport.this, temp[which], Toast.LENGTH_SHORT).show();
                AppliedFilter = temp[which];
                if (which==0){
                    AppliedFilter = ALL;
                }else if(which==1){
                    AppliedFilter = SELF;
                }
            }
        }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReportSingleCropAddReportItem(AppliedFilter,false);
            }
        });

        builder.create().show();
    }

    public void ReportSingleCropAddReportItem(String FilterType,boolean check){
        reportList.clear();
        if (check){
            UnpaidAmountServiceProviderTextView.setVisibility(View.VISIBLE);
            UnpaidAmountBuyerTextView.setVisibility(View.VISIBLE);
        }else{
            UnpaidAmountBuyerTextView.setVisibility(View.GONE);
            UnpaidAmountServiceProviderTextView.setVisibility(View.GONE);
        }
        ShowProgress(this);
        cropReportLocation(FilterType).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    String CropWay = (String) documentSnapshot.get(CROP_WAY);
                    String CropWayName = "";
                    if (CropWay.equals(BUYER)){
                        double remainingAmount = (double) documentSnapshot.get(REMAINING_AMOUNT);
                        UnpaidAmountBuyerTextView.setText(String.valueOf(remainingAmount));
                        continue;
                    }
                    if (CropWay.equals(SERVICE_PROVIDER)){
                        double remainingAmount = (double) documentSnapshot.get(REMAINING_AMOUNT);
                        UnpaidAmountServiceProviderTextView.setText(String.valueOf(remainingAmount));
                        continue;
                    }
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
                            default:
                                continue;
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
