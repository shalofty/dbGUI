package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class Customers {
    private final String customerName, customerAddress, postalCode, customerPhone, countryDivision;
    private final int customerID, divisionID;

    /**
     * Customers constructor
     * */
    public Customers(String customerName, String customerAddress, String postalCode,
                     String customerPhone, String countryDivision, int customerID, int divisionID) {
        this.customerName = customerName; // set the customer name
        this.customerAddress = customerAddress; // set the customer address
        this.postalCode = postalCode; // set the postal code
        this.customerPhone = customerPhone; // set the customer phone
        this.countryDivision = countryDivision; // set the country division
        this.customerID = customerID; // set the customer ID
        this.divisionID = divisionID; // set the division ID
    }

    /**
     * getCustomerName method to
     * @return customerName
     * */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * getCustomerAddress method to
     * @return customerAddress
     * */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * getPostalCode method to
     * @return postalCode
     * */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * getCustomerPhone method to
     * @return customerPhone
     * */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * getCountryDivision method to
     * @return countryDivision
     * */
    public String getCountryDivision() {
        return countryDivision;
    }

    /**
     * getCustomerID method to
     * @return customerID
     * */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * getDivisionID method to
     * @return divisionID
     * */
    public int getDivisionID() {
        return divisionID;
    }

    public static ObservableList<Customers> getCustomersList() {
        return FXCollections.<Customers>observableArrayList();
    }

    /**
     * toString method to
     * @return customerName, customerAddress, postalCode, customerPhone, countryDivision, customerID, divisionID as a string
     * used for debugging easy debugging, because the method is automatically called when the object is printed
     * */
    @Override
    public String toString() {
        return "Customer{" +
                "customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", countryDivision='" + countryDivision + '\'' +
                ", customerID=" + customerID +
                ", divisionID=" + divisionID +
                '}';
    }
}
