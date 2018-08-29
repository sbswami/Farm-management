package com.sanshy.farmmanagement.crops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.loans.LoanPerson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.CROP_TYPE;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_ID;
import static com.sanshy.farmmanagement.MyStatic.CURRENT_CROP_NAME;
import static com.sanshy.farmmanagement.MyStatic.DATE;
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
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.YEAR_OF_REAPING;
import static com.sanshy.farmmanagement.MyStatic.YEAR_OF_SOWING;
import static com.sanshy.farmmanagement.MyStatic.singleCropLocation;

public class MyCrops extends AppCompatActivity {

    ListView CropListView;

    ArrayList<SingleCropList> CropList = new ArrayList<>();
    SingleCropListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crops);

        CropListView = findViewById(R.id.crops_list);
        myAdapter = new SingleCropListAdapter(this,CropList);

        CropListView.setAdapter(myAdapter);

        CropListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MyCrops.this,OptionForSingleCrop.class);
                intent.putExtra(CURRENT_CROP_NAME,CropList.get(position).getCropName());
                intent.putExtra(CURRENT_CROP_ID,CropList.get(position).getCropId());
                intent.putExtra(YEAR_OF_SOWING,CropList.get(position).getYearOfSowing());
                intent.putExtra(YEAR_OF_REAPING,CropList.get(position).getYearOfReaping());
                startActivity(intent);
            }
        });
        CropListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyCrops.this,SingleCrop.class);

                intent.putExtra(CURRENT_CROP_NAME,CropList.get(position).getCropName());
                intent.putExtra(CURRENT_CROP_ID,CropList.get(position).getCropId());

                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addCrops();
    }

    public void addCrops(){

        ShowProgress(this);
        singleCropLocation.orderBy(MyStatic.DATE_OF_CROP_ADDING, Query.Direction.DESCENDING).get().addOnSuccessListener(this,new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                CropList.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){

                    Map<String, Object> CloudItem = queryDocumentSnapshot.getData();

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

                    SingleCropList item = new SingleCropList(Data.getCropId(),Data.getYearOfReaping(),Data.getYearOfSowing(),Data.getCropName(),Data.getYearOfSowing()+"-"+Data.getYearOfReaping(),String.valueOf(Data.getLandArea()),Data.getCropType(),Data.isPartnerExists(),Data.getPartnerCropName1(),Data.getPartnerCropName2(),Data.getPartnerCropName3(),Data.getPartnerCropName4(),Data.getPartnerCropName5());
                    CropList.add(item);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        });

    }

    public void addCropBt(View view){
        try{
            startActivity(new Intent(this,AddCrop.class));
        }catch(Exception ex){
            MyStatic.ShowDialog(this,ex.toString());
        }
    }
}
