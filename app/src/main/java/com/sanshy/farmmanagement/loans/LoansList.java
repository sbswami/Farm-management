package com.sanshy.farmmanagement.loans;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.R;
import com.sanshy.farmmanagement.crops.IncomeCropHistory;
import com.sanshy.farmmanagement.crops.SingleFourColumnsList;
import com.sanshy.farmmanagement.crops.SingleFourColumnsListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.sanshy.farmmanagement.MyStatic.ALL;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.AMOUNT_AFTER_LAST_PAYMENT;
import static com.sanshy.farmmanagement.MyStatic.BANK_MODE;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogFrom;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogSingle;
import static com.sanshy.farmmanagement.MyStatic.ChooseDateDialogTo;
import static com.sanshy.farmmanagement.MyStatic.DATE;
import static com.sanshy.farmmanagement.MyStatic.DateToString;
import static com.sanshy.farmmanagement.MyStatic.GET_MODE;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.GIVEN_LOAN_PAID;
import static com.sanshy.farmmanagement.MyStatic.GOT_LOAN;
import static com.sanshy.farmmanagement.MyStatic.HideProgress;
import static com.sanshy.farmmanagement.MyStatic.ID;
import static com.sanshy.farmmanagement.MyStatic.INTEREST_RATE;
import static com.sanshy.farmmanagement.MyStatic.LAST_PAY_DATE;
import static com.sanshy.farmmanagement.MyStatic.LOAN_COMPLETE;
import static com.sanshy.farmmanagement.MyStatic.LOAN_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_PERSON_ID;
import static com.sanshy.farmmanagement.MyStatic.LOAN_PERSON_NAME;
import static com.sanshy.farmmanagement.MyStatic.LOAN_TYPE;
import static com.sanshy.farmmanagement.MyStatic.PAID_AMOUNT;
import static com.sanshy.farmmanagement.MyStatic.PAID_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.PAID_LOAN;
import static com.sanshy.farmmanagement.MyStatic.PAYING_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.PER_MONTH_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.REMARK;
import static com.sanshy.farmmanagement.MyStatic.SIMPLE_INTEREST;
import static com.sanshy.farmmanagement.MyStatic.SIX_MONTHLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.ShowDialog;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.THREE_MONTHLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.YEARLY_COMPOUND;
import static com.sanshy.farmmanagement.MyStatic.fromDateStatic;
import static com.sanshy.farmmanagement.MyStatic.incomeCropLocation;
import static com.sanshy.farmmanagement.MyStatic.lessLoanAllReport;
import static com.sanshy.farmmanagement.MyStatic.loanPersonList;
import static com.sanshy.farmmanagement.MyStatic.loanRemarkList;
import static com.sanshy.farmmanagement.MyStatic.paidGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.paidGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.reportGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.reportGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.setSnipper;
import static com.sanshy.farmmanagement.MyStatic.singleDateStatic;
import static com.sanshy.farmmanagement.MyStatic.singleDateStaticTill;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGetLoanPersonLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGiveLoanLocation;
import static com.sanshy.farmmanagement.MyStatic.singleGiveLoanPersonLocation;
import static com.sanshy.farmmanagement.MyStatic.toDateStatic;

public class LoansList extends AppCompatActivity {

    boolean GetMode;

    ListView LoanListView;
    SingleFourColumnsListAdapter myAdapter;
    ArrayList<SingleFourColumnsList> FourList = new ArrayList<>();
    ArrayList<OneLoanProperties> LoanList = new ArrayList<>();

    CheckBox PaidCheck,UnpaidCheck;
    boolean paidC = true;
    boolean unpaidC = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans_list);

        Intent intent = getIntent();

        GetMode = intent.getBooleanExtra(GET_MODE,true);

        PaidCheck = findViewById(R.id.paid_loan_check);
        UnpaidCheck = findViewById(R.id.unpaid_loan_check);

        PaidCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                paidC = isChecked;
                addData();
            }
        });
        UnpaidCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                unpaidC = isChecked;
                addData();
            }
        });

        LoanListView = findViewById(R.id.loan_list);
        myAdapter = new SingleFourColumnsListAdapter(this,FourList);
        LoanListView.setAdapter(myAdapter);

        LoanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(LoansList.this,OptionForSingleLoan.class);
                    intent1.putExtra(LOAN_ID,LoanList.get(position).getId());
                    intent1.putExtra(GET_MODE,GetMode);
                    intent1.putExtra(LOAN_COMPLETE,LoanList.get(position).isLoanComplete());
                    startActivity(intent1);
            }
        });

        LoanListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ShowDetailDi(position);
                return true;
            }
        });

    }

    public void ShowDetailDi(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoansList.this);

        final String ThisLoanId = LoanList.get(position).getId();
        final String ThisLoanPerson = LoanList.get(position).getLoanPersonName();
        final Date ThisLoanDate = LoanList.get(position).getStartDate();
        final double ThisLoanAmount = LoanList.get(position).getLoanAmount();
        final boolean ThisisPaid = LoanList.get(position).isLoanComplete();


        LayoutInflater layoutInflater = (LayoutInflater) LoansList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_show_loan_detail,null);


        TextView Remark = customView.findViewById(R.id.remark_dialog);
        TextView Amount = customView.findViewById(R.id.amount_dialog);
        TextView Date = customView.findViewById(R.id.date_dialog);
        TextView LoanPerson = customView.findViewById(R.id.loan_person_dialog);
        TextView InterestRate = customView.findViewById(R.id.interest_rate_dialog);
        TextView InterestRateType = customView.findViewById(R.id.interest_rate_type_dialog);
        TextView InterestType = customView.findViewById(R.id.interest_type_dialog);
        TextView PaidAmount = customView.findViewById(R.id.paid_amount);
        TextView AfterLastPayment = customView.findViewById(R.id.after_last_payment);
        TextView LastPayDate = customView.findViewById(R.id.last_pay_date);
        TextView InterestCountingWay = customView.findViewById(R.id.interest_counting_way);

        Remark.setText(LoanList.get(position).getRemark());
        Amount.setText(String.valueOf(LoanList.get(position).getLoanAmount()));
        Date.setText(DateToString(LoanList.get(position).getStartDate()));
        LoanPerson.setText(LoanList.get(position).getLoanPersonName());
        InterestRate.setText(String.valueOf(LoanList.get(position).getInterestRate()));
        PaidAmount.setText(String.valueOf(LoanList.get(position).getPaidAmount()));
        AfterLastPayment.setText(String.valueOf(LoanList.get(position).getAmountAfterLastPayment()));
        LastPayDate.setText(DateToString(LoanList.get(position).getLastPayDate()));
        if (LoanList.get(position).isBankMode()){
            InterestCountingWay.setText(getString(R.string.banking));
        }else{
            InterestCountingWay.setText(getString(R.string.traditional));
        }
        String InterestRateTypeString;
        if (LoanList.get(position).isPerMonthInterest()){
            InterestRateTypeString = getString(R.string.per_month);
        }else {
            InterestRateTypeString = getString(R.string.per_year);
        }
        InterestRateType.setText(InterestRateTypeString);
        String InterestTypeString;
        if (LoanList.get(position).isYearlyCompound()){
            InterestTypeString = getString(R.string.yearly_compound);
        }else if (LoanList.get(position).isSixMonthlyCompound()){
            InterestTypeString = getString(R.string.six_monthly_compound);
        }else if (LoanList.get(position).isThreeMonthlyCompound()){
            InterestTypeString = getString(R.string.three_monthly_compound);
        }else{
            InterestTypeString = getString(R.string.simple_interest);
        }
        InterestType.setText(InterestTypeString);

        builder.setView(customView);
        builder.setPositiveButton(getString(R.string.ok),null);
        builder.setNegativeButton(getString(R.string.undo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoansList.this);

                builder1.setTitle(getString(R.string.undo));
                builder1.setMessage(R.string.undo_loan_message);
                builder1.setPositiveButton(getString(R.string.undo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowProgress(LoansList.this);
                        if (GetMode){
                            reportGetLoanLocation(ThisLoanId,ALL).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGetLoanLocation(ThisLoanId,ALL).document(DocId).delete();
                                    }
                                }
                            });
                            reportGetLoanLocation(ThisLoanId,ThisLoanPerson).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGetLoanLocation(ThisLoanId,ThisLoanPerson).document(DocId).delete();
                                    }
                                }
                            });


                            lessLoanAllReport(ALL,GOT_LOAN,ThisLoanDate,ThisLoanAmount);
                            paidGetLoanLocation(ThisLoanId).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        double PayingInterest = documentSnapshot.getDouble(PAYING_INTEREST);
                                        Date payingDate = documentSnapshot.getDate(DATE);

                                        lessLoanAllReport(ALL,PAID_INTEREST,payingDate,PayingInterest);
                                        lessLoanAllReport(ALL,PAID_LOAN,payingDate,PayingInterest);

                                        paidGetLoanLocation(ThisLoanId).document(DocId).delete();
                                    }
                                    HideProgress();
                                    FourList.remove(position);
                                    LoanList.remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    ShowDialog(LoansList.this,getString(R.string.deleted));
                                }
                            });
                            singleGetLoanPersonLocation(ThisLoanPerson).document(ThisLoanId).delete();
                            singleGetLoanLocation.document(ThisLoanId).delete();
                        }
                        else{
                            reportGiveLoanLocation(ThisLoanId,ALL).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGiveLoanLocation(ThisLoanId,ALL).document(DocId).delete();
                                    }
                                }
                            });
                            reportGiveLoanLocation(ThisLoanId,ThisLoanPerson).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGiveLoanLocation(ThisLoanId,ThisLoanPerson).document(DocId).delete();
                                    }
                                }
                            });


                            lessLoanAllReport(ALL,GIVEN_LOAN,ThisLoanDate,ThisLoanAmount);
                            paidGiveLoanLocation(ThisLoanId).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        double PayingInterest = documentSnapshot.getDouble(PAYING_INTEREST);
                                        Date payingDate = documentSnapshot.getDate(DATE);

                                        lessLoanAllReport(ALL,GIVEN_LOAN_INTEREST,payingDate,PayingInterest);
                                        lessLoanAllReport(ALL,GIVEN_LOAN_PAID,payingDate,PayingInterest);

                                        paidGiveLoanLocation(ThisLoanId).document(DocId).delete();
                                    }
                                    HideProgress();
                                    LoanList.remove(position);
                                    FourList.remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    ShowDialog(LoansList.this,getString(R.string.deleted));
                                }
                            });
                            singleGiveLoanPersonLocation(ThisLoanPerson).document(ThisLoanId).delete();
                            singleGiveLoanLocation.document(ThisLoanId).delete();
                        }

                    }
                });

                builder1.create().show();
            }
        });
        builder.setNeutralButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!ThisisPaid){
                    ShowDialog(LoansList.this, getString(R.string.can_not_delete_bacause_payment_still_remaining));
                    return;
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoansList.this);

                builder1.setTitle(getString(R.string.delete));
                builder1.setMessage(R.string.delete_paid_loan_message);
                builder1.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowProgress(LoansList.this);
                        if (GetMode){

                            reportGetLoanLocation(ThisLoanId,ALL).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGetLoanLocation(ThisLoanId,ALL).document(DocId).delete();
                                    }
                                }
                            });

                            reportGetLoanLocation(ThisLoanId,ThisLoanPerson).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGetLoanLocation(ThisLoanId,ThisLoanPerson).document(DocId).delete();
                                    }
                                }
                            });


                            paidGetLoanLocation(ThisLoanId).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        paidGetLoanLocation(ThisLoanId).document(DocId).delete();
                                    }
                                    HideProgress();
                                    LoanList.remove(position);
                                    FourList.remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    ShowDialog(LoansList.this,getString(R.string.deleted));
                                }
                            });
                            singleGetLoanPersonLocation(ThisLoanPerson).document(ThisLoanId).delete();
                            singleGetLoanLocation.document(ThisLoanId).delete();
                        }
                        else{
                            reportGiveLoanLocation(ThisLoanId,ALL).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGiveLoanLocation(ThisLoanId,ALL).document(DocId).delete();
                                    }
                                }
                            });
                            reportGiveLoanLocation(ThisLoanId,ThisLoanPerson).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        reportGiveLoanLocation(ThisLoanId,ThisLoanPerson).document(DocId).delete();
                                    }
                                }
                            });


                            paidGiveLoanLocation(ThisLoanId).get().addOnSuccessListener(LoansList.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        String DocId = documentSnapshot.getId();
                                        paidGiveLoanLocation(ThisLoanId).document(DocId).delete();
                                    }
                                    HideProgress();
                                    LoanList.remove(position);
                                    FourList.remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    ShowDialog(LoansList.this,getString(R.string.deleted));
                                }
                            });
                            singleGiveLoanPersonLocation(ThisLoanPerson).document(ThisLoanId).delete();
                            singleGiveLoanLocation.document(ThisLoanId).delete();
                        }

                    }
                });

                builder1.create().show();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addData();
    }

    ArrayList<Date> datesList = new ArrayList<>();

    private void addData() {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation.orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation.orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addData(String remark) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereEqualTo(REMARK, remark)
                    .orderBy(DATE, Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereEqualTo(REMARK, remark)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addDataLoanPerson(String loanPerson) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(DATE, Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addDataLoanPerson(String remark,String loanPerson) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .whereEqualTo(REMARK, remark)
                    .orderBy(DATE, Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .whereEqualTo(REMARK, remark)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addData(Date date1, Date date2) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addRemarkDateData(Date date1, Date date2, String remark) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .whereEqualTo(REMARK,remark)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .whereEqualTo(REMARK,remark)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addLoanPersonDate(Date date1, Date date2, String loanPerson) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addRemarkLoanPersonData(Date date1, Date date2, String remark, String loanPerson) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .whereEqualTo(REMARK, remark)
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(DATE,date1)
                    .whereLessThanOrEqualTo(DATE,date2)
                    .whereEqualTo(REMARK, remark)
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(DATE, Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addData(double minAmount, double maxAmount) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addRemarkAmount(double minAmount, double maxAmount, String remark) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(REMARK, remark)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(REMARK, remark)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addLoanPersonAmount(double minAmount, double maxAmount, String loanPerson) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addLoanPersonRemarkAmount(double minAmount, double maxAmount, String remark,String loanPerson) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(REMARK, remark)
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(REMARK, remark)
                    .whereEqualTo(LOAN_PERSON_NAME, loanPerson)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }
    private void addData(double minAmount, double maxAmount, final Date date1, final Date date2) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }

                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            });
        }

    }
    private void addDateAmountRemarkData(double minAmount, double maxAmount, final Date date1, final Date date2, String remark) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(REMARK,remark)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }

                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(REMARK,remark)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            });
        }

    }
    private void addDateAmountLoanPersonData(double minAmount, double maxAmount, final Date date1, final Date date2, String loanPerson) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(LOAN_PERSON_NAME,loanPerson)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }

                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(LOAN_PERSON_NAME,loanPerson)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }

                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            });
        }

    }
    private void addDateAmountRemarkLoanPersonData(double minAmount, double maxAmount, final Date date1, final Date date2, String remark, String loanPerson) {
        if (!(paidC||unpaidC)){
            ShowDialog(this,getString(R.string.please_check_any_one));
            return;
        }
        FourList.clear();
        LoanList.clear();
        ShowProgress(this);
        if (GetMode){
            singleGetLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(REMARK,remark)
                    .whereEqualTo(LOAN_PERSON_NAME,loanPerson)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }else {
            singleGiveLoanLocation
                    .whereGreaterThanOrEqualTo(AMOUNT, minAmount)
                    .whereLessThanOrEqualTo(AMOUNT, maxAmount)
                    .whereEqualTo(REMARK,remark)
                    .whereEqualTo(LOAN_PERSON_NAME,loanPerson)
                    .orderBy(AMOUNT, Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if (paidC&&!unpaidC&&!documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }
                        if (!paidC&&unpaidC&&documentSnapshot.getBoolean(LOAN_COMPLETE)){
                            continue;
                        }

                        if (!((documentSnapshot.getDate(DATE).after(date1)
                                ||documentSnapshot.getDate(DATE).equals(date1))
                                &&(documentSnapshot.getDate(DATE).before(date2)
                                ||documentSnapshot.getDate(DATE).equals(date2)))) {
                            continue;
                        }
                        Map<String, Object> CloudItem = documentSnapshot.getData();
                        OneLoanProperties item = new OneLoanProperties(
                                CloudItem.get(ID).toString(),
                                CloudItem.get(REMARK).toString(),
                                (Double) CloudItem.get(AMOUNT),
                                (Double) CloudItem.get(INTEREST_RATE),
                                (Boolean) CloudItem.get(PER_MONTH_INTEREST),
                                (Boolean) CloudItem.get(SIMPLE_INTEREST),
                                (Boolean) CloudItem.get(YEARLY_COMPOUND),
                                (Boolean) CloudItem.get(SIX_MONTHLY_COMPOUND),
                                (Boolean) CloudItem.get(THREE_MONTHLY_COMPOUND),
                                (Date) CloudItem.get(DATE),
                                (Boolean) CloudItem.get(LOAN_COMPLETE),
                                (Date) CloudItem.get(LAST_PAY_DATE),
                                (Double) CloudItem.get(AMOUNT_AFTER_LAST_PAYMENT),
                                (Double) CloudItem.get(PAID_AMOUNT),
                                (Boolean) CloudItem.get(BANK_MODE),
                                CloudItem.get(LOAN_PERSON_ID).toString(),
                                CloudItem.get(LOAN_PERSON_NAME).toString()
                        );
                        SingleFourColumnsList iitem = new SingleFourColumnsList(
                                DateToString(item.getStartDate()),
                                item.getRemark(),
                                String.valueOf(item.getLoanAmount()),
                                item.getLoanPersonName()
                        );
                        FourList.add(iitem);
                        LoanList.add(item);
                    }
                    myAdapter.notifyDataSetChanged();
                    HideProgress();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowDialog(LoansList.this,e.toString());
                    Log.d("FireStore", "Firestore Index "+e.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+e.toString());
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        loanPersonList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    LoanPersonList = (ArrayList<String>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loanRemarkList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    RemarkList = (ArrayList<String>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ArrayList<String> RemarkList = new ArrayList<>();
    ArrayList<String> LoanPersonList = new ArrayList<>();

    boolean singleDate = false;
    boolean betweenDate = false;

    boolean singleChooseDate = false;
    boolean fromChooseDate = false;
    boolean toChooseDate = false;

    String minAmountFilterSt;
    String maxAmountFilterSt;

    String remarkFilterSt;
    String loanPersonFilterSt;

    
    public void filterLoanList(View v){

        singleDate = false;
        betweenDate =false;
        singleChooseDate = false;
        fromChooseDate = false;
        toChooseDate = false;


        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customFilterView = layoutInflater.inflate(R.layout.custom_filter_for_loan_list,null);

        final AutoCompleteTextView RemarkFilter = customFilterView.findViewById(R.id.remark_filter);
        setSnipper(this,RemarkFilter,RemarkList);
        final AutoCompleteTextView LoanPersonFilter = customFilterView.findViewById(R.id.loan_person_filter);
        setSnipper(this,LoanPersonFilter,LoanPersonList);

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
                singleChooseDate = ChooseDateDialogSingle(LoansList.this,SingleShowDateFilter);
            }
        });

        final LinearLayout BetweenDateContainer = customFilterView.findViewById(R.id.between_dates_cotainer);
        Button FromChooseDate = customFilterView.findViewById(R.id.from_choose_date_filter);
        final TextView FromShowDate = customFilterView.findViewById(R.id.from_show_date_filter);

        FromChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromChooseDate = ChooseDateDialogFrom(LoansList.this,FromShowDate);
            }
        });

        Button ToChooseDate = customFilterView.findViewById(R.id.to_choose_date_filter);
        final TextView ToShowDate = customFilterView.findViewById(R.id.to_show_date_filter);

        ToChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChooseDate = ChooseDateDialogTo(LoansList.this,ToShowDate);
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
                

                minAmountFilterSt = MinAmountFilter.getText().toString();
                maxAmountFilterSt = MaxAmountFilter.getText().toString();

                remarkFilterSt = RemarkFilter.getText().toString();
                loanPersonFilterSt = LoanPersonFilter.getText().toString();


                if (minAmountFilterSt.isEmpty()&&!maxAmountFilterSt.isEmpty()){
                    MinAmountFilter.setText(R.string.default_min_amount);
                    minAmountFilterSt = getString(R.string.default_min_amount);
                }
                if (maxAmountFilterSt.isEmpty()&&!minAmountFilterSt.isEmpty()){
                    MaxAmountFilter.setText(R.string.default_max_amount);
                    maxAmountFilterSt = getString(R.string.default_max_amount);
                }

                /*
                 * Remark Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(minAmountFilterSt.isEmpty())
                        &&(maxAmountFilterSt.isEmpty())
                        
                        &&(!remarkFilterSt.isEmpty())
                        &&(loanPersonFilterSt.isEmpty())
                        
                        ){

                    if (RemarkList.contains(remarkFilterSt)){
                        addData(remarkFilterSt);
                    }else{
                        onWrongValue();
                    }

                }

                /*
                  LoanPerson Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(minAmountFilterSt.isEmpty())
                        &&(maxAmountFilterSt.isEmpty())
                        
                        &&(remarkFilterSt.isEmpty())
                        &&(!loanPersonFilterSt.isEmpty())
                        
                        ){

                    if (LoanPersonList.contains(loanPersonFilterSt)){
                        addDataLoanPerson(loanPersonFilterSt);
                    }else{
                        onWrongValue();
                    }

                }

                /*
                  Remark with LoanPerson Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(minAmountFilterSt.isEmpty())
                        &&(maxAmountFilterSt.isEmpty())
                        
                        &&(!remarkFilterSt.isEmpty())
                        &&(!loanPersonFilterSt.isEmpty())
                        
                        ){

                    if (LoanPersonList.contains(loanPersonFilterSt)&&RemarkList.contains(remarkFilterSt)){
                        addDataLoanPerson(remarkFilterSt,loanPersonFilterSt);
                    }else{
                        onWrongValue();
                    }
                }

                /*
                  Remark and Amount Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(!minAmountFilterSt.isEmpty())
                        &&(!maxAmountFilterSt.isEmpty())
                        
                        &&(!remarkFilterSt.isEmpty())
                        &&(loanPersonFilterSt.isEmpty())
                        
                        ){

                    if (RemarkList.contains(remarkFilterSt)){
                        addRemarkAmount(Double.parseDouble(minAmountFilterSt),
                                Double.parseDouble(maxAmountFilterSt),
                                remarkFilterSt
                                );
                    }else{
                        onWrongValue();
                    }

                }


                /*
                 * LoanPerson and Amount Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(!minAmountFilterSt.isEmpty())
                        &&(!maxAmountFilterSt.isEmpty())
                        
                        &&(remarkFilterSt.isEmpty())
                        &&(!loanPersonFilterSt.isEmpty())
                        
                        ){

                    if (LoanPersonList.contains(loanPersonFilterSt)){
                        addLoanPersonAmount(Double.parseDouble(minAmountFilterSt),
                                Double.parseDouble(maxAmountFilterSt),
                                loanPersonFilterSt
                        );
                    }else{
                        onWrongValue();
                    }
                }

                /*
                 * LoanPerson and Remark and Amount Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(!minAmountFilterSt.isEmpty())
                        &&(!maxAmountFilterSt.isEmpty())
                        
                        &&(!remarkFilterSt.isEmpty())
                        &&(!loanPersonFilterSt.isEmpty())
                        
                        ){

                    if (LoanPersonList.contains(loanPersonFilterSt)&&RemarkList.contains(remarkFilterSt)){
                        addLoanPersonRemarkAmount(Double.parseDouble(minAmountFilterSt),
                                Double.parseDouble(maxAmountFilterSt),
                                remarkFilterSt,
                                loanPersonFilterSt
                        );
                    }else{
                        onWrongValue();
                    }
                }



                /*
                  Amount Filter
                 */
                if ((!singleDate)&&(!betweenDate)
                        &&(!minAmountFilterSt.isEmpty())
                        &&(!maxAmountFilterSt.isEmpty())
                        
                        &&(remarkFilterSt.isEmpty())
                        &&(loanPersonFilterSt.isEmpty())
                        
                        ){

                    addData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt));

                }

                /*
                  Single Date Filter
                 */

                if (singleDate&&singleChooseDate){

                    /*
                    Single Date
                     */
                    if ((minAmountFilterSt.isEmpty())
                            &&(maxAmountFilterSt.isEmpty())
                            
                            &&(remarkFilterSt.isEmpty())
                            &&(loanPersonFilterSt.isEmpty())
                            
                            ){

                        addData(singleDateStatic,singleDateStaticTill);

                    }

                    /*
                    Amount Filter With Single Date
                     */
                    else if ((!minAmountFilterSt.isEmpty())
                            &&(!maxAmountFilterSt.isEmpty())
                            
                            &&(remarkFilterSt.isEmpty())
                            &&(loanPersonFilterSt.isEmpty())
                            
                            ){

                        addData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                singleDateStatic,singleDateStaticTill);
                    }

                    /*
                    Remark filter
                     */
                    else if ((minAmountFilterSt.isEmpty())
                            &&(maxAmountFilterSt.isEmpty())
                            
                            &&(!remarkFilterSt.isEmpty())
                            &&(loanPersonFilterSt.isEmpty())
                            
                            ){
                        if (RemarkList.contains(remarkFilterSt)){
                            addRemarkDateData(singleDateStatic, singleDateStaticTill,remarkFilterSt);
                        }else{
                            onWrongValue();
                        }
                    }

                    /*
                    LoanPerson filter
                     */
                    else if ((minAmountFilterSt.isEmpty())
                            &&(maxAmountFilterSt.isEmpty())
                            
                            &&(remarkFilterSt.isEmpty())
                            &&(!loanPersonFilterSt.isEmpty())
                            
                            ){
                        if (LoanPersonList.contains(loanPersonFilterSt)){
                            addLoanPersonDate(singleDateStatic, singleDateStaticTill,loanPersonFilterSt);
                        }else{
                            onWrongValue();
                        }
                    }

                    /*
                    Remark and LoanPerson filter
                     */
                    else if ((minAmountFilterSt.isEmpty())
                            &&(maxAmountFilterSt.isEmpty())
                            
                            &&(!remarkFilterSt.isEmpty())
                            &&(!loanPersonFilterSt.isEmpty())
                            
                            ){
                        if (LoanPersonList.contains(loanPersonFilterSt)&&RemarkList.contains(remarkFilterSt)){
                            addRemarkLoanPersonData(singleDateStatic, singleDateStaticTill,remarkFilterSt,loanPersonFilterSt);
                        }else{
                            onWrongValue();
                        }
                    }

                    /*
                    Amount and Remark Single Date
                     */
                    else if ((!minAmountFilterSt.isEmpty())
                            &&(!maxAmountFilterSt.isEmpty())

                            &&(!remarkFilterSt.isEmpty())
                            &&(loanPersonFilterSt.isEmpty())
                            ){
                        if (RemarkList.contains(remarkFilterSt)){
                            addDateAmountRemarkData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                    singleDateStatic,singleDateStaticTill,remarkFilterSt);
                        }else{
                            onWrongValue();
                        }
                        
                    }
                    
                    /*
                    Amount and LoanPerson Single Date
                     */
                    else if ((!minAmountFilterSt.isEmpty())
                            &&(!maxAmountFilterSt.isEmpty())

                            &&(remarkFilterSt.isEmpty())
                            &&(!loanPersonFilterSt.isEmpty())
                            ){
                        if (LoanPersonList.contains(loanPersonFilterSt)){
                            addDateAmountLoanPersonData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                    singleDateStatic,singleDateStaticTill,loanPersonFilterSt);
                        }else{
                            onWrongValue();
                        }

                    }

                    /*
                    Amount and Remark and LoanPerson Single Date
                     */
                    else if ((!minAmountFilterSt.isEmpty())
                            &&(!maxAmountFilterSt.isEmpty())

                            &&(!remarkFilterSt.isEmpty())
                            &&(!loanPersonFilterSt.isEmpty())
                            ){
                        if (RemarkList.contains(remarkFilterSt)&&LoanPersonList.contains(loanPersonFilterSt)){
                            addDateAmountRemarkLoanPersonData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                    singleDateStatic,singleDateStaticTill,remarkFilterSt,loanPersonFilterSt);
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
                    if ((minAmountFilterSt.isEmpty())
                            &&(maxAmountFilterSt.isEmpty())
                            
                            &&(remarkFilterSt.isEmpty())
                            &&(loanPersonFilterSt.isEmpty())
                            
                            ){
                        addData(fromDateStatic,toDateStatic);
                    }

                    /*
                    Amount Filter
                     */
                    else if ((!minAmountFilterSt.isEmpty())
                            &&(!maxAmountFilterSt.isEmpty())
                            
                            &&(remarkFilterSt.isEmpty())
                            &&(loanPersonFilterSt.isEmpty())
                            
                            ){
                        addData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                fromDateStatic,toDateStatic);
                    }
                    
                    /*
                    Remark filter
                     */
                    else if ((minAmountFilterSt.isEmpty())
                            &&(maxAmountFilterSt.isEmpty())

                            &&(!remarkFilterSt.isEmpty())
                            &&(loanPersonFilterSt.isEmpty())

                            ){
                        if (RemarkList.contains(remarkFilterSt)){
                            addRemarkDateData(fromDateStatic, toDateStatic,remarkFilterSt);
                        }else{
                            onWrongValue();
                        }
                    }

                    /*
                    LoanPerson filter
                     */
                    else if ((minAmountFilterSt.isEmpty())
                            &&(maxAmountFilterSt.isEmpty())

                            &&(remarkFilterSt.isEmpty())
                            &&(!loanPersonFilterSt.isEmpty())

                            ){
                        if (LoanPersonList.contains(loanPersonFilterSt)){
                            addLoanPersonDate(fromDateStatic, toDateStatic,loanPersonFilterSt);
                        }else{
                            onWrongValue();
                        }
                    }

                    /*
                    Remark and LoanPerson filter
                     */
                    else if ((minAmountFilterSt.isEmpty())
                            &&(maxAmountFilterSt.isEmpty())

                            &&(!remarkFilterSt.isEmpty())
                            &&(!loanPersonFilterSt.isEmpty())

                            ){
                        if (LoanPersonList.contains(loanPersonFilterSt)&&RemarkList.contains(remarkFilterSt)){
                            addRemarkLoanPersonData(fromDateStatic, toDateStatic,remarkFilterSt,loanPersonFilterSt);
                        }else{
                            onWrongValue();
                        }
                    }

                    /*
                    Amount and Remark Single Date
                     */
                    else if ((!minAmountFilterSt.isEmpty())
                            &&(!maxAmountFilterSt.isEmpty())

                            &&(!remarkFilterSt.isEmpty())
                            &&(loanPersonFilterSt.isEmpty())
                            ){
                        if (RemarkList.contains(remarkFilterSt)){
                            addDateAmountRemarkData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                    fromDateStatic,toDateStatic,remarkFilterSt);
                        }else{
                            onWrongValue();
                        }

                    }
                    
                    /*
                    Amount and LoanPerson Single Date
                     */
                    else if ((!minAmountFilterSt.isEmpty())
                            &&(!maxAmountFilterSt.isEmpty())

                            &&(remarkFilterSt.isEmpty())
                            &&(!loanPersonFilterSt.isEmpty())
                            ){
                        if (LoanPersonList.contains(loanPersonFilterSt)){
                            addDateAmountLoanPersonData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                    fromDateStatic,toDateStatic,loanPersonFilterSt);
                        }else{
                            onWrongValue();
                        }

                    }

                    /*
                    Amount and Remark and LoanPerson Single Date
                     */
                    else if ((!minAmountFilterSt.isEmpty())
                            &&(!maxAmountFilterSt.isEmpty())

                            &&(!remarkFilterSt.isEmpty())
                            &&(!loanPersonFilterSt.isEmpty())
                            ){
                        if (RemarkList.contains(remarkFilterSt)&&LoanPersonList.contains(loanPersonFilterSt)){
                            addDateAmountRemarkLoanPersonData(Double.parseDouble(minAmountFilterSt),Double.parseDouble(maxAmountFilterSt),
                                    fromDateStatic,toDateStatic,remarkFilterSt,loanPersonFilterSt);
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
        ShowDialog(LoansList.this,getString(R.string.please_fill_correct_value));
    }

}
