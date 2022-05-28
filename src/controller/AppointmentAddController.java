package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import DBAcess.AppointmentDB;
import DBAcess.ContactDB;
import DBAcess.CustomerDB;
import DBAcess.UserDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

/**This class is for the add appointment screen*/
public class AppointmentAddController implements Initializable {

    @FXML
    private TextField addAptTitleField;

    @FXML
    private ComboBox<String> addAptDescField;

    @FXML
    private TextField addAptLocationField;

    @FXML
    private DatePicker addAptDatePicker;

    @FXML
    private ComboBox<Contact> addAptContactCombo;

    @FXML
    private ComboBox<String> addAptStartField;

    @FXML
    private ComboBox<String> addAptEndField;

    @FXML
    private ComboBox<Customer> addAptCustomerIDCombo;

    @FXML
    private ComboBox<User> addAptUserIDCombo;

    @FXML
    private ComboBox<String> addAptTypeCombo;


    Stage stage;
    Parent scene;

    private final ObservableList<Contact> contact = ContactDB.getAllContacts();
    private final ObservableList<Customer> customer = CustomerDB.getAllCustomers();
    private final ObservableList<User> user = UserDB.getAllUsers();
    private final ObservableList<String> timeList = AppointmentDB.comboTimes();
    private final ObservableList<Appointment> appointmentList = AppointmentDB.getAllAppointments();
    private final ObservableList<String> aptTypeList = AppointmentDB.getAptTypeList();
    private final ObservableList<String> aptDescList = AppointmentDB.getAptDescList();

    @Override
    /**Initialize for add appointment screen*/
    public void initialize(URL url, ResourceBundle rb) {
        fillAddAptContactCombo();
        fillAddAptCustomerID();
        fillAddAptUserID();
        fillTimesCombo();
        fillTypeCombo();
        fillDescCombo();
    }

    /**Scene change to Appointment Mains screen*/
    public void handleBackButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, " Any unsaved changes will be erased. Do you want to continue? ");
        alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
            ;
            scene.setStyle("-fx-font-family: 'Times New Roman';");
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**Gets texts and values from fields and combo boxes.Then adds to the database*/
    public void handleSaveButton(ActionEvent event) throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

        try {
            //gets info from fields and boxes
            String aptTitle = addAptTitleField.getText();
            String aptDescription = addAptDescField.getValue();
            String aptLocation = addAptLocationField.getText();
            String aptType = addAptTypeCombo.getValue();
            int aptContactID = addAptContactCombo.getValue().getContactID();
            LocalDate aptDate = addAptDatePicker.getValue();
            LocalTime aptStart = LocalTime.parse(addAptStartField.getValue(), dtf);
            LocalTime aptEnd = LocalTime.parse(addAptEndField.getValue(), dtf);
            int aptCustomerId = addAptCustomerIDCombo.getValue().getCustomerId();
            int aptUserId = addAptUserIDCombo.getValue().getUserID();
            Timestamp aptCreateDate = Timestamp.valueOf(LocalDateTime.now());
            String aptCreatedBy = UserDB.getCurrentUser().getUsername();
            Timestamp aptLastUpdate = Timestamp.valueOf(LocalDateTime.now());
            String aptLastUpdatedBy = UserDB.getCurrentUser().getUsername();
            String aptDateStart = LocalDateTime.of(aptDate, aptStart).format(dtf2);
            String aptDateEnd = LocalDateTime.of(aptDate, aptEnd).format(dtf2);
            // Used to get CustomerID from Table
            AppointmentDB.getSelectedComboCustomerID(aptCustomerId);

            Timestamp timestampAptStart = Timestamp.valueOf(aptDateStart);
            Timestamp timestampAptEnd = Timestamp.valueOf(aptDateEnd);

            fieldValidation();
            if(!fieldValidation()){
                Appointment newApt = new Appointment(aptTitle, aptDescription, aptLocation, aptType, aptContactID, aptDate, aptCustomerId, aptUserId,
                        aptCreateDate, aptCreatedBy, aptLastUpdate, aptLastUpdatedBy, timestampAptStart, timestampAptEnd);
                //check to see if there are any appointments to check for overlap. If no appointment, it gets added
                if (appointmentList.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "A new appointment will be added. Do you want to continue? ");
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        AppointmentDB.saveAppointment(aptUserId, aptType, aptTitle, aptDescription, aptLocation, aptCreateDate,
                                aptLastUpdate, aptCustomerId, aptContactID, aptDateStart, aptDateEnd, aptCreatedBy, aptLastUpdatedBy);

                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
                        scene.setStyle("-fx-font-family: 'Times New Roman';");
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
                } else {
                    //Gets list of appointments based on Customer ID to check for overlap
                    ObservableList<Appointment> overlapList = AppointmentDB.getAptOverlapList();
                    if (overlapList.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "A new appointment will be added. Do you want to continue? ");
                        alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.get() == ButtonType.OK) {
                            AppointmentDB.saveAppointment(aptUserId, aptType, aptTitle, aptDescription, aptLocation, aptCreateDate,
                                    aptLastUpdate, aptCustomerId, aptContactID, aptDateStart, aptDateEnd, aptCreatedBy, aptLastUpdatedBy);

                            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
                            scene.setStyle("-fx-font-family: 'Times New Roman';");
                            stage.setScene(new Scene(scene));
                            stage.show();
                        }
                    } else {
                        int counter = 0;
                        for (Appointment a : overlapList) {
                            Timestamp ns = newApt.getAptStartTime();
                            Timestamp ne = newApt.getAptEndTime();
                            Timestamp as = a.getAptStartTime();
                            Timestamp ae = a.getAptEndTime();
                            counter++;
                            //Overlapping rules
                            if (newApt.getAptCustomerID() != a.getAptCustomerID()) {
                                break;
                            } else if ((ns.after(as) || ns.equals(as)) && (ns.before(ae))) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "You have an overlapping appointment. Please change the Date or Times selected.");
                                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                                alert.showAndWait();
                                break;


                            } else if ((ne.after(as)) && (ne.before(ae) || ne.equals(ae))) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "You have an overlapping appointment. Please change the Date or Times selected.");
                                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                                alert.showAndWait();
                                break;

                            } else if (((ns.before(as)) || (ns.equals(as))) && (((ne.after(ae)) || (ne.equals(ae))))) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "You have an overlapping appointment. Please change the Date or Times selected.");
                                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                                alert.showAndWait();
                                break;

                            } else if (counter == overlapList.size()) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "A new appointment will be added. Do you want to continue? ");
                                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

                                Optional<ButtonType> result = alert.showAndWait();

                                if (result.get() == ButtonType.OK) {
                                    AppointmentDB.saveAppointment(aptUserId, aptType, aptTitle, aptDescription, aptLocation, aptCreateDate,
                                            aptLastUpdate, aptCustomerId, aptContactID, aptDateStart, aptDateEnd, aptCreatedBy, aptLastUpdatedBy);

                                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                                    scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
                                    scene.setStyle("-fx-font-family: 'Times New Roman';");
                                    stage.setScene(new Scene(scene));
                                    stage.show();
                                }
                            }
                        }
                    }
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, " Make sure information is correct");
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.showAndWait();
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Make sure information is correct");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.showAndWait();
        }


    }

    /**Sets contact combo from contact list*/
    public void fillAddAptContactCombo() {
        addAptContactCombo.setItems(contact);
    }

    /**Sets customerID combo from customer list*/
    public void fillAddAptCustomerID() {
        addAptCustomerIDCombo.setItems(customer);
    }

    /**Sets user combo box with from user list*/
    public void fillAddAptUserID() {
        addAptUserIDCombo.setItems(user);
    }

    /**Sets time combo boxes from timeList list*/
    public void fillTimesCombo() {
        addAptStartField.setItems(timeList);
        addAptEndField.setItems(timeList);
    }

    /**Sets type combo box from aptTypeList*/
    public void fillTypeCombo() {
        addAptTypeCombo.setItems(aptTypeList);
    }

    public void fillDescCombo() {
        addAptDescField.setItems(aptDescList);
    }

    /**Validates if user has entered information in all fields and boxes*/
    public boolean fieldValidation() {
        return addAptTitleField.getText().isEmpty() || addAptDescField.getValue().isEmpty() || addAptLocationField.getText().isEmpty();
    }

}
