package com.sanshy.farmmanagement.crops;

public class SingleBuyerCropsList {

    String BuyerId;
    String BuyerName;
    String BuyerPhone;
    String RemainingAmount;

    public SingleBuyerCropsList(String buyerId, String buyerName, String buyerPhone, String remainingAmount) {
        BuyerId = buyerId;
        BuyerName = buyerName;
        BuyerPhone = buyerPhone;
        RemainingAmount = remainingAmount;
    }

    public String getBuyerId() {
        return BuyerId;
    }

    public void setBuyerId(String buyerId) {
        BuyerId = buyerId;
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
