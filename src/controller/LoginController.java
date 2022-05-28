package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointment;
import DBAcess.AppointmentDB;
import DBAcess.UserDB;
import helper.Logger;

/**This class is for login screen*/
public class LoginController implements Initializable {
    
    @FXML
    private TextField usernameTxt;
    
    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label passwordLabel;
    
    @FXML
    private Label mainMessage;
    
    @FXML 
    private Label languageMessage;
    
    @FXML
    private Button loginButton;
    
    private String errorHeader;
    private String errorTitle;
    private String errorText;

    /**Initialize for login screen*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("languages/login", locale);
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        mainMessage.setText(rb.getString("message"));
        languageMessage.setText(rb.getString("language"));
        errorHeader = rb.getString("errorheader");
        errorTitle = rb.getString("errortitle");
        errorText = rb.getString("errortext");
    }

    /**Used to see entered username and password is true from database*/
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        boolean validUser = UserDB.login(username, password);
            if(validUser){
                Logger.userLogger(username, validUser);
                ((Node) (event.getSource())).getScene().getWindow().hide();
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
                root.setStyle("-fx-font-family: 'Times New Roman';");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                appointmentAlert();
            }else  {
                Logger.userLogger(username, validUser);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.setTitle(errorTitle);
                alert.setHeaderText(errorHeader);
                alert.setContentText(errorText);
                alert.showAndWait();
            }
    }

    /**Used to show alert on user login for appointments in within 15 minutes*/
    public void appointmentAlert() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a, MM-dd-yy");
        ObservableList<Appointment> appointments = AppointmentDB.getUserAptList();
        LocalTime loginTime = LocalTime.now();
        int counter = 0;
        for (Appointment a : appointments) {
            counter++;
            Timestamp time = a.getAptStartTime();
            LocalDateTime appointmentTime = time.toLocalDateTime();
            long timeDifference = ChronoUnit.MINUTES.between(loginTime, appointmentTime);
            if (timeDifference > 0 && timeDifference <= 15) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have an Upcoming Appointment in " + timeDifference + " minutes. \nAppointment ID: " + a.getAptID() + " \nStarts at:" + appointmentTime.format(dtf));
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.showAndWait();
                break;
            } else if (counter != appointments.size()) {
                continue;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have no upcoming appointments.");
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.showAndWait();
            }
        }
    }
    
}
