package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sanshy.farmmanagement.R;

import java.util.ArrayList;
import java.util.Date;

import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_ID;
import static com.sanshy.farmmanagement.MyStatic.PARTNER_NAME;
import static com.sanshy.farmmanagement.MyStatic.ShowDetails;

public class PartnerCropSingle extends AppCompatActivity {
    ListView HistoryList;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();
    SingleFourColumnsListAdapter myAdapter;

    String PartnerName;
    String PartnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_crop_single);

        Intent intent = getIntent();

        PartnerName = intent.getStringExtra(PARTNER_NAME);
        PartnerId = intent.getStringExtra(PARTNER_ID);

        HistoryList = findViewById(R.id.partner_sharing_list);

        myAdapter = new SingleFourColumnsListAdapter(this,FourList);

        HistoryList.setAdapter(myAdapter);

        AddHistoryList();
    }

    private void AddHistoryList() {
        SingleFourColumnsList item = new SingleFourColumnsList("wheat","2018","2018","Papu");
        FourList.add(item);
        FourList.add(item);
        FourList.add(item);
        FourList.add(item);
    }
}
