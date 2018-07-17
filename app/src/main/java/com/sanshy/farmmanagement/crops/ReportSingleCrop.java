package com.sanshy.farmmanagement.crops;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sanshy.farmmanagement.R;

import java.util.ArrayList;

public class ReportSingleCrop extends AppCompatActivity {

    ListView SingleReportListView;
    SingleCropReportAdapter myAdapter;
    ArrayList<SingleCropReportSmallList> reportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_single_crop);

        SingleReportListView = findViewById(R.id.single_report_list);

        myAdapter = new SingleCropReportAdapter(this,reportList);

        SingleReportListView.setAdapter(myAdapter);

        ReportSingleCropAddReportItem();
    }

    public void filterSingleReport(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String temp[] = {"All","Self","Partner 1","Partner 2"};

        builder.setSingleChoiceItems(temp, 4, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ReportSingleCrop.this, temp[which], Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    public void ReportSingleCropAddReportItem(){
        SingleCropReportSmallList lister = new SingleCropReportSmallList("Sowing","Total = "+500,"Cash = "+100,"Borrow = "+400);
        reportList.add(lister);
        lister = new SingleCropReportSmallList("Reaping","Total = "+500,"Cash = "+100,"Borrow = "+400);
        reportList.add(lister);
        lister = new SingleCropReportSmallList("Ok","Total = "+500,"Cash = "+100,"Borrow = "+400);
        reportList.add(lister);
        lister = new SingleCropReportSmallList("Oin","Total = "+500,"Cash = "+100,"Borrow = "+400);
        reportList.add(lister);
        myAdapter.notifyDataSetChanged();

    }
}
