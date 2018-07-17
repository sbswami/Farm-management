package com.sanshy.farmmanagement.crops;

public class SingleBuyerCropsList {

    String BuyerName;
    String BuyerPhone;
    String RemainingAmount;

    public SingleBuyerCropsList(String buyerName, String buyerPhone, String remainingAmount) {
        BuyerName = buyerName;
        BuyerPhone = buyerPhone;
        RemainingAmount = remainingAmount;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getBuyerPhone() {
        return BuyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        BuyerPhone = buyerPhone;
    }

    public String getRemainingAmount() {
        return RemainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        RemainingAmount = remainingAmount;
    }
}
