package com.sanshy.farmmanagement.crops;

import java.util.Date;

public class SingleBuyerCropsData {

    String BuyerCropsId;
    String BuyerCropsName;
    long BuyerCropsPhone;
    String BuyerCropsAddress;
    double BuyerCropsRemainingPayment;
    Date BuyerCropsAddingDate;

    public SingleBuyerCropsData(String buyerCropsId, String buyerCropsName, long buyerCropsPhone, String buyerCropsAddress, double buyerCropsRemainingPayment, Date buyerCropsAddingDate) {
        BuyerCropsId = buyerCropsId;
        BuyerCropsName = buyerCropsName;
        BuyerCropsPhone = buyerCropsPhone;
        BuyerCropsAddress = buyerCropsAddress;
        BuyerCropsRemainingPayment = buyerCropsRemainingPayment;
        BuyerCropsAddingDate = buyerCropsAddingDate;
    }

    public String getBuyerCropsId() {
        return BuyerCropsId;
    }

    public void setBuyerCropsId(String buyerCropsId) {
        BuyerCropsId = buyerCropsId;
    }

    public String getBuyerCropsName() {
        return BuyerCropsName;
    }

    public void setBuyerCropsName(String buyerCropsName) {
        BuyerCropsName = buyerCropsName;
    }

    public long getBuyerCropsPhone() {
        return BuyerCropsPhone;
    }

    public void setBuyerCropsPhone(long buyerCropsPhone) {
        BuyerCropsPhone = buyerCropsPhone;
    }

    public String getBuyerCropsAddress() {
        return BuyerCropsAddress;
    }

    public void setBuyerCropsAddress(String buyerCropsAddress) {
        BuyerCropsAddress = buyerCropsAddress;
    }

    public double getBuyerCropsRemainingPayment() {
        return BuyerCropsRemainingPayment;
    }

    public void setBuyerCropsRemainingPayment(double buyerCropsRemainingPayment) {
        BuyerCropsRemainingPayment = buyerCropsRemainingPayment;
    }

    public Date getBuyerCropsAddingDate() {
        return BuyerCropsAddingDate;
    }

    public void setBuyerCropsAddingDate(Date buyerCropsAddingDate) {
        BuyerCropsAddingDate = buyerCropsAddingDate;
    }
}
