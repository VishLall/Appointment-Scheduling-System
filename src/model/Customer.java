package model;

import java.sql.Timestamp;

/**This class if for Customer model*/
public class Customer {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerZip;
    private String customerPhone;
    private Timestamp timestamp;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int customerDivisionId;


    /**Constructor for CustomerDB.getAllCustomers()*/
    public Customer(int customerId, String customerName, String customerAddress, String customerPhone, String customerZip,
                    Timestamp createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int customerDivisionId) {
        this.customerId =  customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerZip = customerZip;
        this.timestamp = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerDivisionId = customerDivisionId;
    }
    /**Constructor for CustomerDB.populateModifyCustomer()*/
    public Customer(int customerID, String customerName) {
        this.customerId = customerID;
        this.customerName = customerName;
    }

    /**Constructor for CustomerDB.populateCustomerModFields()*/
    public Customer(String customerName, String customerAddress, String customerZip, String customerPhone) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerZip = customerZip;
        this.customerPhone = customerPhone;
    }

    public Customer(String customerName, String customerAddress, String customerZip, String customerPhone, int customerDivisionId) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerZip = customerZip;
        this.customerPhone = customerPhone;
        this.customerDivisionId = customerDivisionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerZip() {
        return customerZip;
    }

    public void setCustomerZip(String customerZip) {
        this.customerZip = customerZip;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getCustomerDivisionId() {
        return customerDivisionId;
    }

    public void setCustomerDivisionId(int customerDivisionId) {
        this.customerDivisionId = customerDivisionId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**Override toString to display as "ID: Customer ID - Customer Name"*/
    @Override
    public  String toString(){
        return ("ID: "+ Integer.toString(customerId)+ " - "+ customerName );
    }
}


