package controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**This class is for the Main screen*/
public class MainController implements Initializable {
    Stage stage;
    Parent scene;

    /**Scene change to Customer main screen*/
    @FXML
    public void handleCustomerButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerMain.fxml"));
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**Scene change to Appointment main screen*/
    @FXML
    public void handleAppointmentButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Scene change to Reports main screen*/
    @FXML
    public void handleReportsButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsMain.fxml"));
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Opens log file*/
    @FXML
    public void handleLogsButton() throws IOException {
        File file = new File("login_activity.txt");
        if (file.exists()) {
            if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
            }
        }
    }

    /**Initialize for Main screen*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
