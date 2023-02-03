package NumericNexus;

import Exceptions.ExceptionHandler;
import Models.Appointments;
import Models.Customers;

import java.util.Random;

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
            ExceptionHandler.eAlert(e); // eAlert method
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
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }
}
