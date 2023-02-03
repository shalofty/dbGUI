package numericNexus;

import models.Appointments;
import models.Customers;
import models.Users;
import theAgency.AgentFord;

import java.sql.SQLException;
import java.util.Random;

import static controllers.CentralNervousSystem.theCrimeScene;

public class NumberGenie {
    /**
     * generateAppointmentID generates a new appointment ID
     * @return newID
     * */
    public static int magicAppointment() throws RuntimeException {
        try {
            Random randy = new Random();
            int maxID = 9999; // max appointment ID is 9999
            int newID = randy.nextInt(maxID); // generate new ID
            // check if the new ID is already in use
            for (Appointments appointment : Appointments.getAppointmentsList()) {
                // if the new ID is already in use, generate a new ID
                if (appointment.getAppointmentID() == newID) {
                    return magicAppointment();
                }
            }
            return newID;
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
            throw e;
        }
    }

    /**
     * magicCustomer generates a new customer ID
     * @return newID
     * */
    public static int magicCustomer() throws RuntimeException {
        try {
            Random randy = new Random();
            int maxID = 9999; // max appointment ID is 9999
            int newID = randy.nextInt(maxID); // generate new ID
            // check if the new ID is already in use
            for (Customers customer : Customers.getCustomersList()) {
                // if the new ID is already in use, generate a new ID
                if (customer.getCustomerID() == newID) {
                    return magicCustomer();
                }
            }
            return newID;
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
            throw e;
        }
    }

    /**
     * magicUser generates a new user ID
     * @return newID
     * */
    public static int magicUser() throws RuntimeException, SQLException {
        try {
            Random randy = new Random();
            int maxID = 9999; // max appointment ID is 9999
            int newID = randy.nextInt(maxID); // generate new ID
            // check if the new ID is already in use
            for (Users users : Users.getAllUsers()) {
                // if the new ID is already in use, generate a new ID
                if (users.getUserID() == newID) {
                    return magicUser(); // recursion
                }
            }
            return newID; // return the new ID
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
            throw e;
        }
    }
}
