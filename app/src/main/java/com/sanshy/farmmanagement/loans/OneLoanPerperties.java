package com.sanshy.farmmanagement.loans;

import java.util.Date;

public class OneLoanPerperties {

    String Id;
    String Remark;
    double LoanAmount;
    double InterestRate;
    boolean PerMonthInterest;
    boolean SimpleInterest;
    boolean YearlyCompound;
    boolean SixMonthlyCompound;
    boolean ThreeMonthlyCompound;
    Date StartDate;

    String LoanPersonId;
    String LoanPersonName;

    public OneLoanPerperties(String id, String remark, double loanAmount, double interestRate, boolean perMonthInterest, boolean simpleInterest, boolean yearlyCompound, boolean sixMonthlyCompound, boolean threeMonthlyCompound, Date startDate, String loanPersonId, String loanPersonName) {
        Id = id;
        Remark = remark;
        LoanAmount = loanAmount;
        InterestRate = interestRate;
        PerMonthInterest = perMonthInterest;
        SimpleInterest = simpleInterest;
        YearlyCompound = yearlyCompound;
        SixMonthlyCompound = sixMonthlyCompound;
        ThreeMonthlyCompound = threeMonthlyCompound;
        StartDate = startDate;
        LoanPersonId = loanPersonId;
        LoanPersonName = loanPersonName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public double getLoanAmount() {
        return LoanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        LoanAmount = loanAmount;
    }

    public double getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(double interestRate) {
        InterestRate = interestRate;
    }

    public boolean isPerMonthInterest() {
        return PerMonthInterest;
    }

    public void setPerMonthInterest(boolean perMonthInterest) {
        PerMonthInterest = perMonthInterest;
    }

    public boolean isSimpleInterest() {
        return SimpleInterest;
    }

    public void setSimpleInterest(boolean simpleInterest) {
        SimpleInterest = simpleInterest;
    }

    public boolean isYearlyCompound() {
        return YearlyCompound;
    }

    public void setYearlyCompound(boolean yearlyCompound) {
        YearlyCompound = yearlyCompound;
    }

    public boolean isSixMonthlyCompound() {
        return SixMonthlyCompound;
    }

    public void setSixMonthlyCompound(boolean sixMonthlyCompound) {
        SixMonthlyCompound = sixMonthlyCompound;
    }

    public boolean isThreeMonthlyCompound() {
        return ThreeMonthlyCompound;
    }

    public void setThreeMonthlyCompound(boolean threeMonthlyCompound) {
        ThreeMonthlyCompound = threeMonthlyCompound;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public String getLoanPersonId() {
        return LoanPersonId;
    }

    public void setLoanPersonId(String loanPersonId) {
        LoanPersonId = loanPersonId;
    }

    public String getLoanPersonName() {
        return LoanPersonName;
    }

    public void setLoanPersonName(String loanPersonName) {
        LoanPersonName = loanPersonName;
    }
}
