package com.sanshy.farmmanagement.crops;

import java.util.Date;

public class SingleServiceProviderData {

    String ServiceProviderId;
    String ServiceProviderName;
    long ServiceProviderPhone;
    String ServiceProviderAddress;
    String ServiceProviderServiceType;
    double ServiceProviderRemainingPayment;
    Date ServiceProviderAddingDate;

    public SingleServiceProviderData(String serviceProviderId, String serviceProviderName, long serviceProviderPhone, String serviceProviderAddress, String serviceProviderServiceType, double serviceProviderRemainingPayment, Date serviceProviderAddingDate) {
        ServiceProviderId = serviceProviderId;
        ServiceProviderName = serviceProviderName;
        ServiceProviderPhone = serviceProviderPhone;
        ServiceProviderAddress = serviceProviderAddress;
        ServiceProviderServiceType = serviceProviderServiceType;
        ServiceProviderRemainingPayment = serviceProviderRemainingPayment;
        ServiceProviderAddingDate = serviceProviderAddingDate;
    }

    public String getServiceProviderName() {
        return ServiceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        ServiceProviderName = serviceProviderName;
    }

    public long getServiceProviderPhone() {
        return ServiceProviderPhone;
    }

    public void setServiceProviderPhone(long serviceProviderPhone) {
        ServiceProviderPhone = serviceProviderPhone;
    }

    public String getServiceProviderAddress() {
        return ServiceProviderAddress;
    }

    public void setServiceProviderAddress(String serviceProviderAddress) {
        ServiceProviderAddress = serviceProviderAddress;
    }

    public String getServiceProviderId() {
        return ServiceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        ServiceProviderId = serviceProviderId;
    }

    public String getServiceProviderServiceType() {
        return ServiceProviderServiceType;
    }

    public void setServiceProviderServiceType(String serviceProviderServiceType) {
        ServiceProviderServiceType = serviceProviderServiceType;
    }

    public double getServiceProviderRemainingPayment() {
        return ServiceProviderRemainingPayment;
    }

    public void setServiceProviderRemainingPayment(double serviceProviderRemainingPayment) {
        ServiceProviderRemainingPayment = serviceProviderRemainingPayment;
    }

    public Date getServiceProviderAddingDate() {
        return ServiceProviderAddingDate;
    }

    public void setServiceProviderAddingDate(Date serviceProviderAddingDate) {
        ServiceProviderAddingDate = serviceProviderAddingDate;
    }
}
