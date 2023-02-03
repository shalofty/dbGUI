package models;

public class Country {
    private final int countryID;
    private final String countryName;

    /**
     * @param countryID, countryName
     * */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * @return countryID
     * */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @return countryName
     * */
    public String getCountryName() {
        return countryName;
    }

    /**
     * toString method to
     * @return countryID + countryName as a string
     * used for debugging easy debugging, because the method is automatically called when the object is printed
     * */
    @Override
    public String toString() {
        return "Country{" +
                "countryID=" + countryID +
                ", countryName='" + countryName + '\'' +
                "}";
    }
}
