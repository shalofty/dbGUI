package Exceptions;

import javafx.scene.control.Alert;

public class ExceptionHandler {
    public static void eAlert(Exception e) {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("eAlert Error");
        alert.setHeaderText("There is an error in " + trace.getFileName());
        alert.setContentText("Error: " + e.getMessage() +
                "\nCause: " + e.getCause() +
                "\nMethod : " + trace.getMethodName() +
                "\nLine: " + trace.getLineNumber());
        System.out.println("Error: " + e.getMessage());
        System.out.println("Cause: " + e.getCause());
        e.printStackTrace();
        alert.showAndWait();
    }
}
