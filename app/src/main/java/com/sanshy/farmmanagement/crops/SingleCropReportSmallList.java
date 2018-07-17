package com.sanshy.farmmanagement.crops;

public class SingleCropReportSmallList {
    String title;
    String total;
    String cash;
    String borrow;

    public SingleCropReportSmallList(String title, String total, String cash, String borrow) {
        this.title = title;
        this.total = total;
        this.cash = cash;
        this.borrow = borrow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getBorrow() {
        return borrow;
    }

    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }
}
