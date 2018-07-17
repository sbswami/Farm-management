package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sanshy.farmmanagement.R;

import java.util.ArrayList;

public class BuyerCrops extends AppCompatActivity {

    ListView BuyerCropList;
    SingleBuyerCropListAdapter myAdapter;
    ArrayList<SingleBuyerCropsList> BuyerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_crops);

        BuyerCropList = findViewById(R.id.buyer_crops_list);

        myAdapter = new SingleBuyerCropListAdapter(this,BuyerList);

        BuyerCropList.setAdapter(myAdapter);
        BuyerCropsAddData();
    }

    public void BuyerCropsAddData(){
        SingleBuyerCropsList lister = new SingleBuyerCropsList("Raone","Phone "+8552,"Landing Amount "+200);
        BuyerList.add(lister);
        lister = new SingleBuyerCropsList("Raone","Phone "+8552,"Landing Amount "+200);
        BuyerList.add(lister);
        lister = new SingleBuyerCropsList("Raone","Phone "+8552,"Landing Amount "+200);
        BuyerList.add(lister);
        lister = new SingleBuyerCropsList("Raone","Phone "+8552,"Landing Amount "+200);
        BuyerList.add(lister);
        lister = new SingleBuyerCropsList("Raone","Phone "+8552,"Landing Amount "+200);
        BuyerList.add(lister);
        lister = new SingleBuyerCropsList("Raone","Phone "+8552,"Landing Amount "+200);
        BuyerList.add(lister);

        myAdapter.notifyDataSetChanged();
    }

    public void addBuyerCropsBt(View view){
        startActivity(new Intent(this,AddBuyerCrops.class));
    }
}
