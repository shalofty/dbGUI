package company;

import Exceptions.ExceptionHandler;
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
            launch(args);
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e);
        }
    }
}