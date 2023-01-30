package models;

public final class Customers {
    private final String customerName, customerAddress, postalCode, customerPhone, countryDivision;
    private final int customerID, divisionID;

    /**
     * Customers constructor
     * */
    public Customers(String customerName, String customerAddress, String postalCode,
                     String customerPhone, String countryDivision, int customerID, int divisionID) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.customerPhone = customerPhone;
        this.countryDivision = countryDivision;
        this.customerID = customerID;
        this.divisionID = divisionID;
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
