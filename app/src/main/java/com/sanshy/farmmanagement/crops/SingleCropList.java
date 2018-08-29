package com.sanshy.farmmanagement.crops;

public class SingleCropList {

    String CropId;
    String YearOfReaping;
    String YearOfSowing;
    String CropName;
    String CropYear;
    String CropLandArea;
    String CropType;
    boolean partner;
    String Partner1;
    String Partner2;
    String Partner3;
    String Partner4;
    String Partner5;

    public SingleCropList(String cropId, String yearOfReaping, String yearOfSowing, String cropName, String cropYear, String cropLandArea, String cropType, boolean partner) {
        CropId = cropId;
        YearOfReaping = yearOfReaping;
        YearOfSowing = yearOfSowing;
        CropName = cropName;
        CropYear = cropYear;
        CropLandArea = cropLandArea;
        CropType = cropType;
        this.partner = partner;
    }

    public SingleCropList(String cropId, String yearOfReaping, String yearOfSowing, String cropName, String cropYear, String cropLandArea, String cropType, boolean partner, String partner1, String partner2, String partner3, String partner4, String partner5) {
        CropId = cropId;
        YearOfReaping = yearOfReaping;
        YearOfSowing = yearOfSowing;
        CropName = cropName;
        CropYear = cropYear;
        CropLandArea = cropLandArea;
        CropType = cropType;
        this.partner = partner;
        Partner1 = partner1;
        Partner2 = partner2;
        Partner3 = partner3;
        Partner4 = partner4;
        Partner5 = partner5;
    }

    public String getCropId() {
        return CropId;
    }

    public void setCropId(String cropId) {
        CropId = cropId;
    }

    public String getYearOfReaping() {
        return YearOfReaping;
    }

    public void setYearOfReaping(String yearOfReaping) {
        YearOfReaping = yearOfReaping;
    }

    public String getYearOfSowing() {
        return YearOfSowing;
    }

    public void setYearOfSowing(String yearOfSowing) {
        YearOfSowing = yearOfSowing;
    }

    public String getCropName() {
        return CropName;
    }

    public void setCropName(String cropName) {
        CropName = cropName;
    }

    public String getCropYear() {
        return CropYear;
    }

    public void setCropYear(String cropYear) {
        CropYear = cropYear;
    }

    public String getCropLandArea() {
        return CropLandArea;
    }

    public void setCropLandArea(String cropLandArea) {
        CropLandArea = cropLandArea;
    }

    public String getCropType() {
        return CropType;
    }

    public void setCropType(String cropType) {
        CropType = cropType;
    }

    public boolean isPartner() {
        return partner;
    }

    public void setPartner(boolean partner) {
        this.partner = partner;
    }

    public String getPartner1() {
        return Partner1;
    }

    public void setPartner1(String partner1) {
        Partner1 = partner1;
    }

    public String getPartner2() {
        return Partner2;
    }

    public void setPartner2(String partner2) {
        Partner2 = partner2;
    }

    public String getPartner3() {
        return Partner3;
    }

    public void setPartner3(String partner3) {
        Partner3 = partner3;
    }

    public String getPartner4() {
        return Partner4;
    }

    public void setPartner4(String partner4) {
        Partner4 = partner4;
    }

    public String getPartner5() {
        return Partner5;
    }

    public void setPartner5(String partner5) {
        Partner5 = partner5;
    }
}
