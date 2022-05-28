package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**This is the main method of the Scheduler Application*/
public class Main extends Application {

    public static void main(String[] args) {
        // Test Language
        //Locale.setDefault(new Locale("fr"));

        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();
        root.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
