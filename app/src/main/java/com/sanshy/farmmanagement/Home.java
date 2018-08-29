package com.sanshy.farmmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.sanshy.farmmanagement.crops.AllCropsReport;
import com.sanshy.farmmanagement.crops.BuyerCrops;
import com.sanshy.farmmanagement.crops.MyCrops;
import com.sanshy.farmmanagement.crops.PartnerCrop;
import com.sanshy.farmmanagement.crops.ServiceProviders;
import com.sanshy.farmmanagement.extras.HomeExpenditure;
import com.sanshy.farmmanagement.extras.OtherExpenditure;
import com.sanshy.farmmanagement.extras.SideBusiness;
import com.sanshy.farmmanagement.loans.LoanPerson;
import com.sanshy.farmmanagement.loans.LoansList;
import com.sanshy.farmmanagement.loans.NewLoan;
import com.sanshy.farmmanagement.loans.PayHistory;
import com.sanshy.farmmanagement.loans.ReportLoan;

import java.util.Arrays;
import java.util.List;

import static com.sanshy.farmmanagement.MyStatic.LOAN_TYPE;
import static com.sanshy.farmmanagement.MyStatic.RC_SIGN_IN;
import static com.sanshy.farmmanagement.MyStatic.ShowProgress;
import static com.sanshy.farmmanagement.MyStatic.currentUser;
import static com.sanshy.farmmanagement.MyStatic.db;
import static com.sanshy.farmmanagement.MyStatic.mainData;
import static com.sanshy.farmmanagement.MyStatic.mainDoc;
import static com.sanshy.farmmanagement.MyStatic.setCurrentUserId;

public class Home extends AppCompatActivity {

    LinearLayout cropContainer,loanContainer,extraContainer,settingContainer;

    LinearLayout myCrops,serviceProvider,partnerCrops,allCropsReport;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_crop:
                    cropContainer.setVisibility(View.VISIBLE);
                    loanContainer.setVisibility(View.GONE);
                    extraContainer.setVisibility(View.GONE);
                    settingContainer.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_loan:
                    loanContainer.setVisibility(View.VISIBLE);
                    cropContainer.setVisibility(View.GONE);
                    extraContainer.setVisibility(View.GONE);
                    settingContainer.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_extra:
                    extraContainer.setVisibility(View.VISIBLE);
                    cropContainer.setVisibility(View.GONE);
                    loanContainer.setVisibility(View.GONE);
                    settingContainer.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_setting:
                    settingContainer.setVisibility(View.VISIBLE);
                    extraContainer.setVisibility(View.GONE);
                    cropContainer.setVisibility(View.GONE);
                    loanContainer.setVisibility(View.GONE);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signIn();

        cropContainer = findViewById(R.id.crop_container);
        loanContainer = findViewById(R.id.loan_container);
        extraContainer = findViewById(R.id.extra_container);
        settingContainer = findViewById(R.id.setting_container);

        myCrops = findViewById(R.id.my_crops);
        serviceProvider = findViewById(R.id.service_provider);
        partnerCrops = findViewById(R.id.partner_crop);
        allCropsReport = findViewById(R.id.all_crops_report);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                setCurrentUserId(currentUser.getUid());
                // ...
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setCancelable(false);

                builder.setMessage(R.string.please_sign_in_again);
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signIn();
                    }
                });

                builder.create().show();

                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    public void signIn(){
        if (currentUser==null){
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }

    }

    public void myCropsBt(View view){
        startActivity(new Intent(this,MyCrops.class));
    }
    public void serviceProviderBt(View view){
        startActivity(new Intent(this,ServiceProviders.class));
    }
    public void partnerCropsBt(View view){
        startActivity(new Intent(this, PartnerCrop.class));
    }
    public void buyerCropBt(View view){
        startActivity(new Intent(this, BuyerCrops.class));
    }
    public void allCropsReportBt(View view){
        startActivity(new Intent(this, AllCropsReport.class));
    }

    public void newLoanBt(View view){
        startActivity(new Intent(this,NewLoan.class));
    }
    public void loanPersonBt(View view){
        startActivity(new Intent(this, LoanPerson.class));
    }
    public void getLoanListBt(View view){
        Intent intent = new Intent(this, LoansList.class);

        intent.putExtra(LOAN_TYPE,getString(R.string.get_type));

        startActivity(intent);
    }
    public void giveLoanListBt(View view){
        Intent intent = new Intent(this, LoansList.class);

        intent.putExtra(LOAN_TYPE,getString(R.string.give_type));

        startActivity(intent);
    }
    public void getAndPaidLoanListBt(View view){
        Intent intent = new Intent(this, PayHistory.class);

        intent.putExtra(LOAN_TYPE,getString(R.string.get_type));

        startActivity(intent);
    }
    public void giveAndPaidLoanListBt(View view){
        Intent intent = new Intent(this, PayHistory.class);

        intent.putExtra(LOAN_TYPE,getString(R.string.give_type));

        startActivity(intent);
    }
    public void ReportBt(View view){
        startActivity(new Intent(this, ReportLoan.class));
    }

    public void SideBusinessBt(View view){
        startActivity(new Intent(this,SideBusiness.class));
    }
    public void HomeExpenditureBt(View view){
        startActivity(new Intent(this, HomeExpenditure.class));
    }
    public void OtherExpenditureBt(View view){
        startActivity(new Intent(this, OtherExpenditure.class));
    }

    public void ResetAllBt(View view){
        mainDoc.delete();
        mainData.removeValue();
    }

}
