package models;

public final class Division {
    private final String divisionName;
    private final int divisionID, countryID;

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
     * @param divisionID, divisionName, countryID
     * @return Division object
     * */
    public static Division create(int divisionID, String divisionName, int countryID) {
        if (divisionID <= 0) {
            throw new IllegalArgumentException("Invalid division ID");
        }
        if (divisionName == null || divisionName.isEmpty()) {
            throw new IllegalArgumentException("Invalid division name");
        }
        if (countryID <= 0) {
            throw new IllegalArgumentException("Invalid country ID");
        }
        return new Division(divisionID, divisionName, countryID);
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
