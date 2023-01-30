package company;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception{
//        JDBC.openConnection(); // Test connection to database
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("/views/loginMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 630, 415);
        stage.setResizable(false);
        stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        try {
            JDBC.openConnection();
            launch(args);
            JDBC.closeConnection();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        }
    }
}