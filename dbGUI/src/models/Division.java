package models;

public class Division {
    private String divisionName;
    private int divisionID;
    public int countryID;

    /**
     * constructor for Division
     * @param divisionID, divisionName, countryID
     * */
    public Division(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**
     * @return divisionName
     * */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @return divisionID
     * */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @return countryID
     * */
    public int getCountryID() {
        return countryID;
    }

    /**
     * toString method to
     * @return divisionID + divisionName + countryID as a string
     * used for debugging easy debugging, because the method is automatically called when the object is printed
     * */
    @Override
    public String toString() {
        return "Division{" +
                "divisionID=" + divisionID +
                ", divisionName='" + divisionName + '\'' +
                ", countryID=" + countryID +
                '}';
    }
}
