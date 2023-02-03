package TheAgency;

import Exceptions.ExceptionHandler;
import Helper.JDBC;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

public class AgentFord {
    /**
     * trackActivity tracks the activity of the user and logs it to the activity log
     * viewable in the activity log tab
     * */
    @FXML
    public static void gatherIntel(TextArea textArea) {
        try {
            StackTraceElement trace = Thread.currentThread().getStackTrace()[2]; // get the stack trace
            String codeLog = trace.getFileName() + " . Line" + trace.getLineNumber() + ". " + trace.getMethodName() + "()"; // get the file name, line number, and method name
            String userLog = Instant.now() + " User: " + JDBC.getUsername() + ". Session ID: " + JDBC.getConnection() + ". "; // get the username, connection, and time
            textArea.appendText(userLog + codeLog + "\n"); // append the log to the text area
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e);
            throw e;
        }
    }

    /**
     * exportActivity exports the activity log to a text file in the ActivityLog folder
     * when the export button is clicked
     * */
    @FXML public static void deBriefing(TextArea textArea) {
        try {
            String time = String.valueOf(LocalDateTime.now()).replace(":", "-"); // replace : with -
            String userName = JDBC.getUsername(); // get username
            String fileName = time + "_" + userName + ".txt"; // create file name
            String filePath = "ActivityLog/"; // create file path
            FileWriter fileWriter = new FileWriter(filePath + fileName); // create file writer
            fileWriter.write(textArea.getText()); // write to file
            fileWriter.close(); // close file writer
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * trackLogins tracks the logins and displays them in the dbActivityTextArea
     * */
    @FXML public static void frontDoorSurveillance(TextArea textArea) {
        try (BufferedReader eyeSpy = new BufferedReader(new FileReader("ActivityLog/loginActivity.txt"))) {
            StringBuilder agentZero = new StringBuilder(); // create a string builder
            String spyLine = eyeSpy.readLine(); // read the first line
            while (spyLine != null) {
                spyLine = eyeSpy.readLine(); // read the next line
                if (spyLine != null) {
                    agentZero.append(spyLine).append("\n"); // append the line to the string builder
                }
            }
            textArea.appendText(agentZero.toString()); // append the string builder to the text area
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e);
        }
    }
}
