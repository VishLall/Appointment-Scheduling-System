package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import DBAcess.AppointmentDB;
import model.Contact;
import DBAcess.ContactDB;
import model.MonthReportInterface;
import model.ReportInterface;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ResourceBundle;


/**This class is  for Reports main screen*/
public class ReportsMainController implements Initializable {


    @FXML
    private ComboBox<String> monthCombo;

    @FXML
    private ComboBox<String> aptCombo;

    @FXML
    private TableView<Appointment> reportTable;

    @FXML
    private TableColumn<Appointment, Integer> reportAptID;

    @FXML
    private TableColumn<Appointment, String> reportAptTitle;

    @FXML
    private TableColumn<Appointment, String> reportAptType;

    @FXML
    private TableColumn<Appointment, String> reportAptDesc;

    @FXML
    private TableColumn<Appointment, String> reportAptStart;

    @FXML
    private TableColumn<Appointment, String> reportAptEnd;

    @FXML
    private TableColumn<Appointment, Integer> reportAptCustomerID;

    @FXML
    private TextArea reportThree;

    @FXML
    private ComboBox<Contact> contactCombo;

    @FXML
    private Label reportTypeLabel;

    @FXML
    private Label totCustLabel;

    Stage stage;
    Parent scene;

    /**Initialize for report screen*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillMonthCombo();
        fillAptCombo();
        fillContactCombo();
        setTotCustLabel();
    }

    /**Sets table for selected contact*/
    @FXML
    void handleContactGoButton(ActionEvent event) {
        int selectedContact = contactCombo.getValue().getContactID();
        setReportTable(selectedContact);
    }

    /**Goes back to the main screen*/
    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));;
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Lambda Expression: Shows results for what user selects from MonthReportInterface */
    @FXML
    void handleMonthGoButton(ActionEvent event) {

        String selectedMonth = monthCombo.getValue();
        String selectedType = aptCombo.getValue();
        ObservableList<Appointment> reportTypeList = AppointmentDB.getReportTypeList(selectedMonth,selectedType);

        //Multiple Parameter Lambda expression to set Label
        MonthReportInterface report = (m, t, s) -> reportTypeLabel.setText(m+" - "+t+" - "+s+" appointment(s)");
        report.monthReport(selectedMonth, selectedType, reportTypeList.size());
    }

    /**List of months to fill month combo box*/
    public void fillMonthCombo(){
        ObservableList<String> comboMonths = FXCollections.observableArrayList();
        String [] months = new DateFormatSymbols().getMonths();
        for(String month : months){
            comboMonths.add(month);
            if(comboMonths.size()==12){
                break;
            }
        }
        monthCombo.setItems(comboMonths);
    }

    /**List for appointment type combo box*/
    public void fillAptCombo(){
        ObservableList<String> aptType = AppointmentDB.getAptTypeList();
        aptCombo.setItems(aptType);

    }

    /**List for contact combo box*/
    public void fillContactCombo(){
        ObservableList<Contact> contacts = ContactDB.getAllContacts();
        contactCombo.setItems(contacts);
    }

    /**Set report table and columns*/
    public void setReportTable(int selectedContact){
        ObservableList<Appointment> appointments = AppointmentDB.getContactAptList(selectedContact);
        reportAptID.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        reportAptTitle.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
        reportAptDesc.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
        reportAptType.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        reportAptStart.setCellValueFactory(new PropertyValueFactory<>("tableAptStartTime"));
        reportAptEnd.setCellValueFactory(new PropertyValueFactory<>("tableAptEndTime"));
        reportAptCustomerID.setCellValueFactory(new PropertyValueFactory<>("aptCustomerID"));

        reportTable.setItems(appointments);
    }

    /**Lambda Expression: Sets text in label for Total Customer Appointments tab using ReportInterface*/
    public void setTotCustLabel(){
        ObservableList<Appointment> appointments = AppointmentDB.getAllAppointments();
        int totalApts = appointments.size();
        if(totalApts > 1){
            //Single Parameter Lambda expression
            ReportInterface report = t -> totCustLabel.setText("There are "+t+" appointments scheduled.");
            report.totalAppointments(totalApts);
        }else if((totalApts == 1)){
            //Single Parameter Lambda expression
            ReportInterface report = t -> totCustLabel.setText("There is "+t+" appointment scheduled.");
            report.totalAppointments(totalApts);
        }else{
            totCustLabel.setText("No appointments scheduled.");
        }
    }


}