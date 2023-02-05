package dataAccess;

import models.Division;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionMapper {
    public static Division mapDivision(ResultSet set) throws SQLException {
        int divisionID = set.getInt("Division_ID"); // Division_ID
        String divisionName = set.getString("Division"); // Division
        int countryID = set.getInt("Country_ID"); // Country_ID
        return new Division(divisionID, divisionName, countryID); // return the division
    }
}
