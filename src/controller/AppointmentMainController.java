package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import DBAcess.AppointmentDB;
import DBAcess.ContactDB;
import DBAcess.CustomerDB;
import DBAcess.UserDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

/**This class is for the Appointment main controller*/
public class AppointmentMainController implements Initializable {

    @FXML
    private TableView<Appointment> allAptTable;

    @FXML
    private TableColumn<Appointment, Integer> allAptID;

    @FXML
    private TableColumn<Appointment, Integer> allAptUserID;

    @FXML
    private TableColumn<Appointment, String> allAptTitle;

    @FXML
    private TableColumn<Appointment, String> allAptDesc;

    @FXML
    private TableColumn<Appointment, String> allAptLocation;

    @FXML
    private TableColumn<Appointment, String> allAptType;

    @FXML
    private TableColumn<Appointment, String> allAptStart;

    @FXML
    private TableColumn<Appointment, String> allAptEnd;

    @FXML
    private TableColumn<Appointment, String> allAptContact;

    @FXML
    private TableColumn<Appointment, String> allAptCustomerID;

    @FXML
    private TableView<Appointment> monthAptTable;

    @FXML
    private TableColumn<Appointment, Integer> monthAptID;

    @FXML
    private TableColumn<Appointment, Integer> monthAptUserID;

    @FXML
    private TableColumn<Appointment, String> monthAptTitle;

    @FXML
    private TableColumn<Appointment, String> monthAptDesc;

    @FXML
    private TableColumn<Appointment, String> monthAptLocation;

    @FXML
    private TableColumn<Appointment, String> monthAptType;

    @FXML
    private TableColumn<Appointment, String> monthAptStart;

    @FXML
    private TableColumn<Appointment, String> monthAptEnd;

    @FXML
    private TableColumn<Appointment, String> monthAptContact;

    @FXML
    private TableColumn<Appointment, Integer> monthAptCustomerID;

    @FXML
    private TableView<Appointment> weekAptTable;

    @FXML
    private TableColumn<Appointment, Integer> weekAptID;

    @FXML
    private TableColumn<Appointment, Integer> weekAptUserID;

    @FXML
    private TableColumn<Appointment, String> weekAptTitle;

    @FXML
    private TableColumn<Appointment, String> weekAptDesc;

    @FXML
    private TableColumn<Appointment, String> weekAptLocation;

    @FXML
    private TableColumn<Appointment, String> weekAptType;

    @FXML
    private TableColumn<Appointment, String> weekAptStart;

    @FXML
    private TableColumn<Appointment, String> weekAptEnd;

    @FXML
    private TableColumn<Appointment, String> weekAptContact;

    @FXML
    private TableColumn<Appointment, Integer> weekAptCustomerID;

    @FXML
    private Tab allTab;

    @FXML
    private Tab monthlyTab;

    @FXML
    private Tab weeklyTab;

    @FXML
    private TextField searchBar;


    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label currentDateLabel;

    private int selectedAppointmentID;
    private int selectedAppointmentContactID;
    private int selectAptCustomerID;
    private int selectedAptUserID;

    Stage stage;
    Parent scene;

    private ObservableList<Appointment> appointmentList = AppointmentDB.getAllAppointments();
    LocalTime currentTime = LocalTime.now();
    LocalDate currentDate = LocalDate.now();

    /**Initialize for main appointment screen*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAllAptTable();
        setWeekAptTable();
        setMonthAptTable();
        setDateAndTime();
        searchFunction();
    }

    /**Scene change to appointment add screen*/
    @FXML
    public void handleAddButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentAdd.fxml"));
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**Scene change to appointment modify screen*/
    @FXML
    public void handleModifyButton(ActionEvent event) throws IOException {
        try{
            whichTableSelect();

            AppointmentModifyController.getSelectedAppointment(selectedAppointmentID);
            ContactDB.getSelectedAppointmentContactID(selectedAppointmentContactID);
            CustomerDB.getSelectedAptCustomerID(selectAptCustomerID);
            UserDB.getSelectedAptUserID(selectedAptUserID);
            AppointmentDB.getSelectedAppointmentID(selectedAppointmentID);
            AppointmentModifyController.getSelectModCustomerID(selectAptCustomerID);


            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppointmentModify.fxml"));
            Parent scene = loader.load();
            scene.setStyle("-fx-font-family: 'Times New Roman';");
            stage.setScene(new Scene(scene));
            stage.show();
        }catch (NullPointerException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, " Please select an appointment");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.showAndWait();
        }

    }

    /**Scene change to main screen*/
    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Deletes selected appointment from table*/
    @FXML
    public void handleDeleteButton(){
        try{
            whichTableSelect();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This Appointment will be deleted.");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family:'Times New Roman';");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                AppointmentDB.deleteAppointment(selectedAppointmentID);
            }
            if(weeklyTab.isSelected()){
                allAptTable.setItems(AppointmentDB.getAllAppointments());
                tableWeekFilter();
            }if(monthlyTab.isSelected()) {
                allAptTable.setItems(AppointmentDB.getAllAppointments());
                tableMonthFilter();
            }else if(allTab.isSelected()){
                allAptTable.setItems(AppointmentDB.getAllAppointments());
            }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, " Please select an appointment");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.showAndWait();
        }
    }


    /**Filter and sets items in table*/
    public void setAllAptTable() {
        allAptID.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        allAptUserID.setCellValueFactory(new PropertyValueFactory<>("aptUserID"));
        allAptTitle.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
        allAptDesc.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
        allAptLocation.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
        allAptType.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        allAptStart.setCellValueFactory(new PropertyValueFactory<>("tableAptStartTime"));
        allAptEnd.setCellValueFactory(new PropertyValueFactory<>("tableAptEndTime"));
        allAptContact.setCellValueFactory(new PropertyValueFactory<>("aptContactID"));
        allAptCustomerID.setCellValueFactory(new PropertyValueFactory<>("aptCustomerID"));

        allAptTable.setItems(AppointmentDB.getAllAppointments());

    }

    /**Filter and sets items in table*/
    public void setWeekAptTable() {
        weekAptID.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        weekAptUserID.setCellValueFactory(new PropertyValueFactory<>("aptUserID"));
        weekAptTitle.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
        weekAptDesc.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
        weekAptLocation.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
        weekAptType.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        weekAptStart.setCellValueFactory(new PropertyValueFactory<>("tableAptStartTime"));
        weekAptEnd.setCellValueFactory(new PropertyValueFactory<>("tableAptEndTime"));
        weekAptContact.setCellValueFactory(new PropertyValueFactory<>("aptContactID"));
        weekAptCustomerID.setCellValueFactory(new PropertyValueFactory<>("aptCustomerID"));

        tableWeekFilter();
    }

    /**Filter and sets items in table*/
    public void setMonthAptTable() {
        monthAptID.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        monthAptUserID.setCellValueFactory(new PropertyValueFactory<>("aptUserID"));
        monthAptTitle.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
        monthAptDesc.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
        monthAptLocation.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
        monthAptType.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        monthAptStart.setCellValueFactory(new PropertyValueFactory<>("tableAptStartTime"));
        monthAptEnd.setCellValueFactory(new PropertyValueFactory<>("tableAptEndTime"));
        monthAptContact.setCellValueFactory(new PropertyValueFactory<>("aptContactID"));
        monthAptCustomerID.setCellValueFactory(new PropertyValueFactory<>("aptCustomerID"));

        tableMonthFilter();
    }

    /**Shows date and time on appointment main screen*/
    public void setDateAndTime() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("EEE, LLL-dd-yyyy");

        currentTimeLabel.setText(currentTime.format(dtf));
        currentDateLabel.setText(currentDate.format(dtf2));
    }

    /**Determine which table items are being selected from*/
    public void whichTableSelect() {
        if (allTab.isSelected()) {
            selectedAppointmentID = allAptTable.getSelectionModel().getSelectedItem().getAptID();
            selectedAppointmentContactID = allAptTable.getSelectionModel().getSelectedItem().getAptContactID();
            selectAptCustomerID = allAptTable.getSelectionModel().getSelectedItem().getAptCustomerID();
            selectedAptUserID = allAptTable.getSelectionModel().getSelectedItem().getAptUserID();
        } else if (monthlyTab.isSelected()) {
            selectedAppointmentID = monthAptTable.getSelectionModel().getSelectedItem().getAptID();
            selectedAppointmentContactID = monthAptTable.getSelectionModel().getSelectedItem().getAptContactID();
            selectAptCustomerID = monthAptTable.getSelectionModel().getSelectedItem().getAptCustomerID();
            selectedAptUserID = monthAptTable.getSelectionModel().getSelectedItem().getAptUserID();
        } else if (weeklyTab.isSelected()) {
            selectedAppointmentID = weekAptTable.getSelectionModel().getSelectedItem().getAptID();
            selectedAppointmentContactID = weekAptTable.getSelectionModel().getSelectedItem().getAptContactID();
            selectAptCustomerID = weekAptTable.getSelectionModel().getSelectedItem().getAptCustomerID();
            selectedAptUserID = weekAptTable.getSelectionModel().getSelectedItem().getAptUserID();
        }
    }

    /**Filter for week table*/
    public void tableWeekFilter(){
        ObservableList<Appointment> appointmentList = AppointmentDB.getAllAppointments();

        LocalDate weekAfterCurrent = currentDate.plusWeeks(1);
        ObservableList<Appointment> weekFilter = FXCollections.observableArrayList();

        for (Appointment a : appointmentList) {
            LocalDate aptDate = a.getAptStartTime().toLocalDateTime().toLocalDate();
            if (aptDate.isBefore(weekAfterCurrent)) {
                weekFilter.add(a);
            }
            if (aptDate.isBefore(currentDate)) {
                weekFilter.remove(a);
            }
            weekAptTable.setItems(weekFilter);
        }
    }

    /**Filter for Month table*/
    public void tableMonthFilter(){
        ObservableList<Appointment> appointmentList = AppointmentDB.getAllAppointments();

        LocalDate monthAfterCurrent = currentDate.plusMonths(1);
        ObservableList<Appointment> monthFilter = FXCollections.observableArrayList();

        for (Appointment a : appointmentList) {
            LocalDate aptDate = a.getAptStartTime().toLocalDateTime().toLocalDate();
            if (aptDate.isBefore(monthAfterCurrent)) {
                monthFilter.add(a);
            }
            if (aptDate.isBefore(currentDate)) {
                monthFilter.remove(a);
            }
            monthAptTable.setItems(monthFilter);
        }
    }

    /**Filter and sets items in table*/
    public void weeklyTabSelect(Event event) {
        tableWeekFilter();
    }

    /**Filter and sets items in table*/
    public void monthlyTabSelect(Event event) {
        tableMonthFilter();
    }

    /**Set items in allAptTable*/
    public void allTabSelect(Event event) {
        allAptTable.setItems(AppointmentDB.getAllAppointments());
    }

    public void triggerTimeUpdate(MouseEvent mouseEvent) {
    }

    public void searchFunction(){
        FilteredList<Appointment> filterData = new FilteredList<>(AppointmentDB.getAllAppointments(), b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue)-> {
            filterData.setPredicate(Appointment -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                String aptID = Integer.toString(Appointment.getAptID());
                String userID = Integer.toString(Appointment.getAptUserID());
                String contact = Integer.toString(Appointment.getAptContactID());
                String customerID = Integer.toString(Appointment.getAptCustomerID());


                if(aptID.toLowerCase().indexOf(searchKeyword)> -1){
                    return true;
                }else if (userID.toLowerCase().indexOf(searchKeyword)> -1){
                    return true;
                }else if (Appointment.getAptTitle().toLowerCase().indexOf(searchKeyword)> -1){
                    return true;
                }else if (Appointment.getAptDescription().toLowerCase().indexOf(searchKeyword)> -1){
                    return true;
                }else if (Appointment.getAptLocation().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                }else if (Appointment.getAptType().toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                }else if (contact.toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                }else if (customerID.toLowerCase().indexOf(searchKeyword)> -1) {
                    return true;
                }else
                    return false;
            });
        });

        SortedList<Appointment> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(allAptTable.comparatorProperty());

        allAptTable.setItems(sortedData);
    }
}

