package com.sanshy.farmmanagement.extras;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.MyStatic;
import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.ThreeColumnsList;
import com.sanshy.farmmanagement.ThreeColumnsListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.AMOUNT_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE_SIDE_BUSINESS_KEY;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.OtherExpRemark;
import static com.sanshy.farmmanagement.MyStatic.REMARK_SIDE_FOR_ALL;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.otherExpLocation;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class OtherExpenditureList extends AppCompatActivity {
    
    ListView OtherExpenditureListView;
    ArrayList<ThreeColumnsList> HEList = new ArrayList<>();
    ArrayList<SingleExpOrInc> DataList = new ArrayList<>();
    ThreeColumnsListAdapter myAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_expenditure_list);

        OtherExpenditureListView = findViewById(R.id.other_expenditure_history_list);

        myAdapter = new ThreeColumnsListAdapter(this, HEList);

        OtherExpenditureListView.setAdapter(myAdapter);

        OtherExpenditureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OtherExpenditureList.this);

                builder.setTitle(getString(R.string.undo));
                builder.setMessage(getString(R.string.remark)+" : "+DataList.get(position).getRemark()+"\n" +
                        getString(R.string.amount)+" : "+String.valueOf(DataList.get(position).getAmount())+"\n" +
                        getString(R.string.date)+" : "+DateToString(DataList.get(position).getDate()));
                builder.setPositiveButton(getString(R.string.undo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowProgress(OtherExpenditureList.this);
                        otherExpLocation.document(DataList.get(position).getId()).delete().addOnCompleteListener(OtherExpenditureList.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                HEList.remove(position);
                                DataList.remove(position);
                                myAdapter.notifyDataSetChanged();
                                HideProgress();
                            }
                        });
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel),null);

                builder.create().show();
            }
        });

        AddData();

    }


    @Override
    protected void onStart() {
        super.onStart();
        OtherExpRemark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RemarkList = (ArrayList<String>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddData() {
        ShowProgress(this);

        otherExpLocation.orderBy(MyStatic.DATE_SIDE_BUSINESS_KEY, Query.Direction.DESCENDING).get().addOnSuccessListener(this, myListener);

    }
    private void addData(double minAmount, double maxAmount) {
        ShowProgress(this);
        otherExpLocation
                .whereGreaterThanOrEqualTo(AMOUNT_SIDE_FOR_ALL,minAmount)
                .whereLessThanOrEqualTo(AMOUNT_SIDE_FOR_ALL,maxAmount)
                .orderBy(MyStatic.AMOUNT_SIDE_FOR_ALL, Query.Direction.ASCENDING)
                .get().addOnSuccessListener(this, myListener).addOnFailureListener(myFailData);
    }
    private void addData(double minAmount, double maxAmount, final Date date1, final Date date2) {
        ShowProgress(this);
        otherExpLocation
                .whereGreaterThanOrEqualTo(AMOUNT_SIDE_FOR_ALL,minAmount)
                .whereLessThanOrEqualTo(AMOUNT_SIDE_FOR_ALL,maxAmount)
                .orderBy(MyStatic.AMOUNT_SIDE_FOR_ALL, Query.Direction.ASCENDING)
                .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                HEList.clear();
                DataList.clear();
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                    if (!((documentSnapshot.getDate(DATE_SIDE_BUSINESS_KEY).after(date1)
                            ||documentSnapshot.getDate(DATE_SIDE_BUSINESS_KEY).equals(date1))
                            &&(documentSnapshot.getDate(DATE_SIDE_BUSINESS_KEY).before(date2)
                            ||documentSnapshot.getDate(DATE_SIDE_BUSINESS_KEY).equals(date2)))) {
                        continue;
                    }

                    Map<String, Object> MapObj = new HashMap<>();
                    MapObj = documentSnapshot.getData();
                    assert MapObj != null;
                    SingleExpOrInc Data = new SingleExpOrInc(MapObj.get(MyStatic.ID_SIDE_FOR_ALL).toString(),MapObj.get(MyStatic.REMARK_SIDE_FOR_ALL).toString(),(Double)MapObj.get(MyStatic.AMOUNT_SIDE_FOR_ALL),(Date) MapObj.get(MyStatic.DATE_SIDE_FOR_ALL));
                    DataList.add(Data);
                    ThreeColumnsList item = new ThreeColumnsList(DateToString(Data.getDate()),Data.getRemark(),String.valueOf(Data.getAmount()));
                    HEList.add(item);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        }).addOnFailureListener(myFailData);
    }
    private void addData(double minAmount, double maxAmount, final Date date1, final Date date2,String remark) {
        ShowProgress(this);
        otherExpLocation
                .whereGreaterThanOrEqualTo(AMOUNT_SIDE_FOR_ALL,minAmount)
                .whereLessThanOrEqualTo(AMOUNT_SIDE_FOR_ALL,maxAmount)
                .whereEqualTo(REMARK_SIDE_FOR_ALL,remark)
                .orderBy(MyStatic.AMOUNT_SIDE_FOR_ALL, Query.Direction.ASCENDING)
                .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                HEList.clear();
                DataList.clear();
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                    if (!((documentSnapshot.getDate(DATE_SIDE_BUSINESS_KEY).after(date1)
                            ||documentSnapshot.getDate(DATE_SIDE_BUSINESS_KEY).equals(date1))
                            &&(documentSnapshot.getDate(DATE_SIDE_BUSINESS_KEY).before(date2)
                            ||documentSnapshot.getDate(DATE_SIDE_BUSINESS_KEY).equals(date2)))) {
                        continue;
                    }

                    Map<String, Object> MapObj = new HashMap<>();
                    MapObj = documentSnapshot.getData();
                    assert MapObj != null;
                    SingleExpOrInc Data = new SingleExpOrInc(MapObj.get(MyStatic.ID_SIDE_FOR_ALL).toString(),MapObj.get(MyStatic.REMARK_SIDE_FOR_ALL).toString(),(Double)MapObj.get(MyStatic.AMOUNT_SIDE_FOR_ALL),(Date) MapObj.get(MyStatic.DATE_SIDE_FOR_ALL));
                    DataList.add(Data);
                    ThreeColumnsList item = new ThreeColumnsList(DateToString(Data.getDate()),Data.getRemark(),String.valueOf(Data.getAmount()));
                    HEList.add(item);
                }
                myAdapter.notifyDataSetChanged();
                HideProgress();
            }
        }).addOnFailureListener(myFailData);

    }
    private void addData(double minAmount, double maxAmount,String remark) {
        ShowProgress(this);
        otherExpLocation
                .whereGreaterThanOrEqualTo(AMOUNT_SIDE_FOR_ALL,minAmount)
                .whereLessThanOrEqualTo(AMOUNT_SIDE_FOR_ALL,maxAmount)
                .whereEqualTo(REMARK_SIDE_FOR_ALL,remark)
                .orderBy(MyStatic.AMOUNT_SIDE_FOR_ALL, Query.Direction.ASCENDING)
                .get().addOnSuccessListener(this, myListener).addOnFailureListener(myFailData);
    }
    private void addData(Date date1, Date date2) {
        ShowProgress(this);
        otherExpLocation
                .whereGreaterThanOrEqualTo(DATE_SIDE_BUSINESS_KEY,date1)
                .whereLessThanOrEqualTo(DATE_SIDE_BUSINESS_KEY,date2)
                .orderBy(DATE_SIDE_BUSINESS_KEY, Query.Direction.DESCENDING).get().addOnSuccessListener(this, myListener).addOnFailureListener(myFailData);
    }
    private void addData(Date date1, Date date2,String remark) {
        ShowProgress(this);
        otherExpLocation
                .whereGreaterThanOrEqualTo(DATE_SIDE_BUSINESS_KEY,date1)
                .whereLessThanOrEqualTo(DATE_SIDE_BUSINESS_KEY,date2)
                .whereEqualTo(REMARK_SIDE_FOR_ALL,remark)
                .orderBy(DATE_SIDE_BUSINESS_KEY, Query.Direction.DESCENDING).get().addOnSuccessListener(this, myListener).addOnFailureListener(myFailData);
    }
    private void addData(String remark) {
        ShowProgress(this);
        otherExpLocation
                .whereEqualTo(REMARK_SIDE_FOR_ALL,remark)
                .orderBy(DATE_SIDE_BUSINESS_KEY, Query.Direction.DESCENDING)
                .get().addOnSuccessListener(this, myListener).addOnFailureListener(myFailData);
    }

    ArrayList<String> RemarkList = new ArrayList<>();

    String RemarkString;
    String MinAmountString;
    String MaxAmountString;

    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;

    public void FilterOtherExpenditureHistory(View view){


        singleDate = false;
        betweenDate =false;
        singleChooseDate = false;
        fromChooseDate = false;
        toChooseDate = false;

        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_for_side_business,null);

        final AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
        setSnipper(this,RemarkFilter, RemarkList);

        final EditText MinAmountFilter = customFilterView.findViewById(R.id.min_amount_filter);
        final EditText MaxAmountFilter = customFilterView.findViewById(R.id.max_amount_filter);

        RadioGroup DateFilter = customFilterView.findViewById(R.id.date_filter);
        RadioButton SingleDateFilter = customFilterView.findViewById(R.id.single_date_filter);
        RadioButton BetweenDateFilter = customFilterView.findViewById(R.id.between_date_filter);


        final LinearLayout SingleDateContainer = customFilterView.findViewById(R.id.single_date_filter_container);
        Button SingleChooseDateFilter = customFilterView.findViewById(R.id.single_choose_date_filter);
        final TextView SingleShowDateFilter = customFilterView.findViewById(R.id.single_show_date_filter);

        SingleChooseDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseDate = ChooseDateDialogSingle(OtherExpenditureList.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(OtherExpenditureList.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(OtherExpenditureList.this,ToShowDate);
            }
        });


        Button FilterBt = customFilterView.findViewById(R.id.filter_bt);


        DateFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.single_date_filter){
                    singleDate = true;
                    betweenDate = false;
                    SingleDateContainer.setVisibility(View.VISIBLE);
                    BetweenDateContainer.setVisibility(View.GONE);
                }else{
                    singleDate = false;
                    betweenDate = true;
                    SingleDateContainer.setVisibility(View.GONE);
                    BetweenDateContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        FilterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RemarkString = RemarkFilter.getText().toString();
                MinAmountString = MinAmountFilter.getText().toString();
                MaxAmountString = MaxAmountFilter.getText().toString();

                if (MinAmountString.isEmpty()&&!MaxAmountString.isEmpty()){
                    MinAmountFilter.setText(R.string.default_min_amount);
                    MinAmountString = getString(R.string.default_min_amount);
                }
                if (MaxAmountString.isEmpty()&&!MinAmountString.isEmpty()){
                    MaxAmountFilter.setText(R.string.default_max_amount);
                    MaxAmountString = getString(R.string.default_max_amount);
                }

                /*
                 * Remark Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(MinAmountString.isEmpty())
                        &&(MaxAmountString.isEmpty())

                        &&(!RemarkString.isEmpty())


                        ){

                    if (RemarkList.contains(RemarkString)){
                        addData(RemarkString);
                    }else{
                        onWrongValue();
                    }

                }
                

                /*
                  Remark and Amount Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(!MinAmountString.isEmpty())
                        &&(!MaxAmountString.isEmpty())

                        &&(!RemarkString.isEmpty())


                        ){

                    if (RemarkList.contains(RemarkString)){
                        addData(Double.parseDouble(MinAmountString),
                                Double.parseDouble(MaxAmountString),
                                RemarkString
                        );
                    }else{
                        onWrongValue();
                    }

                }

                /*
                  Amount Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(!MinAmountString.isEmpty())
                        &&(!MaxAmountString.isEmpty())

                        &&(RemarkString.isEmpty())


                        ){

                    addData(Double.parseDouble(MinAmountString),Double.parseDouble(MaxAmountString));

                }

                /*
                  Single Date Filter
                 */

                if (singleDate&&singleChooseDate){

                    /*
                    Single Date
                     */
                    if ((MinAmountString.isEmpty())
                            &&(MaxAmountString.isEmpty())

                            &&(RemarkString.isEmpty())


                            ){

                        addData(singleDateStatic,singleDateStaticTill);

                    }

                    /*
                    Amount Filter With Single Date
                     */
                    else if ((!MinAmountString.isEmpty())
                            &&(!MaxAmountString.isEmpty())

                            &&(RemarkString.isEmpty())


                            ){

                        addData(Double.parseDouble(MinAmountString),Double.parseDouble(MaxAmountString),
                                singleDateStatic,singleDateStaticTill);
                    }

                    /*
                    Remark filter
                     */
                    else if ((MinAmountString.isEmpty())
                            &&(MaxAmountString.isEmpty())

                            &&(!RemarkString.isEmpty())


                            ){
                        if (RemarkList.contains(RemarkString)){
                            addData(singleDateStatic, singleDateStaticTill,RemarkString);
                        }else{
                            onWrongValue();
                        }
                    }

                    /*
                    Amount and Remark Single Date
                     */
                    else if ((!MinAmountString.isEmpty())
                            &&(!MaxAmountString.isEmpty())

                            &&(!RemarkString.isEmpty())

                            ){
                        if (RemarkList.contains(RemarkString)){
                            addData(Double.parseDouble(MinAmountString),Double.parseDouble(MaxAmountString),
                                    singleDateStatic,singleDateStaticTill,RemarkString);
                        }else{
                            onWrongValue();
                        }

                    }

                }


                /*
                  Between Date Filter
                 */

                if (betweenDate&&toChooseDate&&fromChooseDate){

                    /*
                    Between Date
                     */
                    if ((MinAmountString.isEmpty())
                            &&(MaxAmountString.isEmpty())

                            &&(RemarkString.isEmpty())


                            ){
                        addData(fromDateStatic,toDateStatic);
                    }

                    /*
                    Amount Filter
                     */
                    else if ((!MinAmountString.isEmpty())
                            &&(!MaxAmountString.isEmpty())

                            &&(RemarkString.isEmpty())


                            ){
                        addData(Double.parseDouble(MinAmountString),Double.parseDouble(MaxAmountString),
                                fromDateStatic,toDateStatic);
                    }
                    
                    /*
                    Remark filter
                     */
                    else if ((MinAmountString.isEmpty())
                            &&(MaxAmountString.isEmpty())

                            &&(!RemarkString.isEmpty())


                            ){
                        if (RemarkList.contains(RemarkString)){
                            addData(fromDateStatic, toDateStatic,RemarkString);
                        }else{
                            onWrongValue();
                        }
                    }



                    /*
                    Amount and Remark Single Date
                     */
                    else if ((!MinAmountString.isEmpty())
                            &&(!MaxAmountString.isEmpty())

                            &&(!RemarkString.isEmpty())

                            ){
                        if (RemarkList.contains(RemarkString)){
                            addData(Double.parseDouble(MinAmountString),Double.parseDouble(MaxAmountString),
                                    fromDateStatic,toDateStatic,RemarkString);
                        }else{
                            onWrongValue();
                        }

                    }


                }

                builder.dismiss();
            }
        });

        builder.setView(customFilterView);

        builder.show();
    }


    public void onWrongValue(){
        ShowDialog(OtherExpenditureList.this,getString(R.string.please_fill_correct_value));
    }

    OnSuccessListener<QuerySnapshot> myListener = new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            HEList.clear();
            DataList.clear();
            for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                Map<String, Object> MapObj = new HashMap<>();
                MapObj = documentSnapshot.getData();
                assert MapObj != null;
                SingleExpOrInc Data = new SingleExpOrInc(MapObj.get(MyStatic.ID_SIDE_FOR_ALL).toString(),MapObj.get(MyStatic.REMARK_SIDE_FOR_ALL).toString(),(Double)MapObj.get(MyStatic.AMOUNT_SIDE_FOR_ALL),(Date) MapObj.get(MyStatic.DATE_SIDE_FOR_ALL));
                DataList.add(Data);
                ThreeColumnsList item = new ThreeColumnsList(DateToString(Data.getDate()),Data.getRemark(),String.valueOf(Data.getAmount()));
                HEList.add(item);
            }
            myAdapter.notifyDataSetChanged();
            HideProgress();
        }
    };

    OnFailureListener myFailData = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.d("Firestore", "Index : "+e.toString());
            System.out.println(e.toString());
        }
    };
}
