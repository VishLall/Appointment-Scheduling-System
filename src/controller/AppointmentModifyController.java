package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
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

/**Initialize for modify appointment screen*/
public class AppointmentModifyController implements Initializable {

    @FXML
    private TextField modifyAptTitleField;

    @FXML
    private ComboBox<String> modifyAptDescField;

    @FXML
    private TextField modifyAptLocationField;

    @FXML
    private DatePicker modifyAptDatePicker;

    @FXML
    private ComboBox<Contact> modifyAptContactCombo;

    @FXML
    private ComboBox<String> modifyAptStartField;

    @FXML
    private ComboBox<String> modifyAptEndField;

    @FXML
    private Label modifyAptCustomerIdLabel;

    @FXML
    private ComboBox<User> modifyAptUserIDCombo;

    @FXML
    private ComboBox<String> modifyAptTypeCombo;


    Stage stage;
    Parent scene;

    private static int selectedAppointmentID;
    public static int selectModCustomerID;

    /**Gets selectModCustomerID from AppointmentMainController*/
    public static void getSelectModCustomerID(int customerIDModOverLap){
        selectModCustomerID = customerIDModOverLap;
    }

    /**Gets selectedAppointmentID from AppointmentMainController*/
    public static void getSelectedAppointment(int appointmentIDToModify) {
        selectedAppointmentID = appointmentIDToModify;
    }


    //Various Lists to populate text fields and all combo boxes.
    private final ObservableList<Appointment> appointmentInfo = AppointmentDB.selectedAppointmentInfo(selectedAppointmentID);
    private final ObservableList<Contact> contact = ContactDB.getAllContacts();
    private final ObservableList<Contact>modContact = ContactDB.populateModifyContact();
    private final ObservableList<Customer> customer = CustomerDB.getAllCustomers();
    private final ObservableList<Customer>modCustomer = CustomerDB.populateModifyCustomer();
    private final ObservableList<User> user = UserDB.getAllUsers();
    private final ObservableList<User> modUser = UserDB.populateModifyUser();
    private final ObservableList<String> timeList = AppointmentDB.comboTimes();
    private final ObservableList<String> aptTypeList = AppointmentDB.getAptTypeList();
    private final ObservableList<String> aptDescList = AppointmentDB.getAptDescList();

    /**Initialize for add modift appointment screen*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillModifyAppointmentFields();
        fillModifyAptContactCombo();
        fillModifyAptUserID();
        fillTimesCombo();
        fillTypeCombo();
        fillDescCombo();
    }

    /**Sets text field with info from apoointmentInfo list*/
    public void fillModifyAppointmentFields() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");

        modifyAptTitleField.setText(appointmentInfo.get(0).getAptTitle());
        modifyAptDescField.setValue(appointmentInfo.get(0).getAptDescription());
        modifyAptLocationField.setText(appointmentInfo.get(0).getAptLocation());
        modifyAptTypeCombo.setValue(appointmentInfo.get(0).getAptType());
        modifyAptStartField.setValue(appointmentInfo.get(0).getAptStartTime().toLocalDateTime().format(dtf));
        modifyAptEndField.setValue(appointmentInfo.get(0).getAptEndTime().toLocalDateTime().format(dtf));
        modifyAptDatePicker.setValue(appointmentInfo.get(0).getAptStartTime().toLocalDateTime().toLocalDate());
        modifyAptContactCombo.setValue(modContact.get(0));
        modifyAptCustomerIdLabel.setText(String.valueOf(modCustomer.get(0)));
        modifyAptUserIDCombo.setValue(modUser.get(0));
    }

    /**Scene change to Appointment Mains screen*/
    public void handleBackButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, " Any unsaved changes will be erased. Do you want to continue? ");
        alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
            scene.setStyle("-fx-font-family: 'Times New Roman';");
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**Gets texts and values from fields and combo boxes.Then updates the database*/
    public void handleSaveButton(ActionEvent event) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

            //gets info from fields and boxes
            String modifyAptTitle = modifyAptTitleField.getText();
            String modifyAptDescription = modifyAptDescField.getValue();
            String modifyAptLocation = modifyAptLocationField.getText();
            String modifyAptType = modifyAptTypeCombo.getValue();
            int modifyAptContactID = modifyAptContactCombo.getValue().getContactID();
            LocalDate modifyAptDate = modifyAptDatePicker.getValue();
            LocalTime modifyAptStart = LocalTime.parse(modifyAptStartField.getValue(), dtf);
            LocalTime modifyAptEnd = LocalTime.parse(modifyAptEndField.getValue(), dtf);
            int modifyAptUserID = modifyAptUserIDCombo.getValue().getUserID();
            Timestamp modifyAptLastUpdate = Timestamp.valueOf(LocalDateTime.now());
            String modifyAptLastUpdatedBy = UserDB.getCurrentUser().getUsername();

            String modifyAptDateStart = LocalDateTime.of(modifyAptDate, modifyAptStart).format(dtf2);
            String modifyAptDateEnd = LocalDateTime.of(modifyAptDate, modifyAptEnd).format(dtf2);

            Timestamp timestampModAptStart = Timestamp.valueOf(modifyAptDateStart);
            Timestamp timestampModAptEnd = Timestamp.valueOf(modifyAptDateEnd);

            fieldValidation();
            if(!fieldValidation()) {

                Appointment modApt = new Appointment(modifyAptTitle, modifyAptDescription, modifyAptLocation, modifyAptType, modifyAptContactID, modifyAptDate, modifyAptUserID,
                        modifyAptLastUpdate, modifyAptLastUpdatedBy, timestampModAptStart, timestampModAptEnd);
                //Gets list of appointments based on Customer ID to check for overlap
                ObservableList<Appointment> modOverlapList = AppointmentDB.getModAptOverlapList(selectModCustomerID);
                if (modOverlapList.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your appointment will be updated. Do you want to continue? ");
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {

                        AppointmentDB.updateAppointment(modifyAptTitle, modifyAptDescription, modifyAptLocation, modifyAptContactID, modifyAptType,
                                modifyAptDateStart, modifyAptDateEnd, modifyAptLastUpdate, modifyAptLastUpdatedBy, modifyAptUserID);

                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
                        scene.setStyle("-fx-font-family: 'Times New Roman';");
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
                } else {
                    int counter = 0;
                    for (Appointment a : modOverlapList) {
                        Timestamp ns = modApt.getAptStartTime();
                        Timestamp ne = modApt.getAptEndTime();
                        Timestamp as = a.getAptStartTime();
                        Timestamp ae = a.getAptEndTime();
                        counter++;
                        //Overlapping rules
                        if (selectModCustomerID != a.getAptCustomerID()) {
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

                        } else if (counter == modOverlapList.size()) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your appointment will be updated. Do you want to continue? ");
                            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.get() == ButtonType.OK) {

                                AppointmentDB.updateAppointment(modifyAptTitle, modifyAptDescription, modifyAptLocation, modifyAptContactID, modifyAptType,
                                        modifyAptDateStart, modifyAptDateEnd, modifyAptLastUpdate, modifyAptLastUpdatedBy, modifyAptUserID);

                                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                                scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
                                scene.setStyle("-fx-font-family: 'Times New Roman';");
                                stage.setScene(new Scene(scene));
                                stage.show();
                            }
                        }
                    }
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, " Make sure information is correct");
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.showAndWait();
            }
    }

    /**Sets contact combo from contact list*/
    public void fillModifyAptContactCombo() {
        modifyAptContactCombo.setItems(contact);
    }

    /**Sets user combo box with from user list*/
    public void fillModifyAptUserID() {
        modifyAptUserIDCombo.setItems(user);
    }

    /**Sets time combo boxes from timeList list*/
    public void fillTimesCombo() {
        modifyAptStartField.setItems(timeList);
        modifyAptEndField.setItems(timeList);
    }

    /**Sets type combo box from aptTypeList*/
    public void fillTypeCombo(){
        modifyAptTypeCombo.setItems(aptTypeList);
    }

    public void fillDescCombo(){
        modifyAptDescField.setItems(aptDescList);
    }

    /**Validates if user has entered information in all fields and boxes*/
    public boolean fieldValidation() {
        return modifyAptTitleField.getText().isEmpty() || modifyAptDescField.getValue().isEmpty() || modifyAptLocationField.getText().isEmpty();
    }
}

