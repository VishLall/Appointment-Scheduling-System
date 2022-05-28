
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DBAcess.CustomerDB;
import DBAcess.UserDB;

/**This class is for the add customer screen*/
public class CustomerAddController implements Initializable {
    @FXML
    public Button saveButton;

    @FXML
    public Button backButton;

    @FXML
    private TextField name;

    @FXML
    private TextField address;

    @FXML
    private TextField zip;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> firstLevelCombo;

    @FXML
    public ComboBox<String> countryCombo;


    Stage stage;

    private final ObservableList<String> countries = CustomerDB.fillCountryCombo();

    /**Initialize for add customer add screen*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillCountryCombo();
    }

    /**Sets country combo box from countries list*/
    public void fillCountryCombo() {
        countryCombo.setItems(countries);
    }

    /**Sets first level combo box from firstLevels list */
    public void fillFirstLevel() {
        ObservableList<String> firstLevels = CustomerDB.firstLevelFilter();
        firstLevelCombo.setItems(firstLevels);
    }

    /**Scene change to Customer Mains screen*/
    public void handleBackButton(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, " Any unsaved changes will be erased. Do you want to continue? ");
        alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()&&result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerMain.fxml"));
            Parent scene = loader.load();
            scene.setStyle("-fx-font-family: 'Times New Roman';");
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**Gets texts and values from fields and combo boxes.Then adds to the database*/
    public void handleSaveButton(ActionEvent event) throws IOException {
        try{
            //gets info from fields and boxes
            String customerName = name.getText();
            String customerAddress = address.getText();
            String customerZip = zip.getText();
            String customerPhone = phone.getText();
            Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());
            String createdBy = UserDB.getCurrentUser().getUsername();
            LocalDateTime lastUpdate = LocalDateTime.now();
            String lastUpdatedBy = UserDB.getCurrentUser().getUsername();
            String customerDivision = firstLevelCombo.getSelectionModel().getSelectedItem();
            int actualCustomerDivision = CustomerDB.getFirstLevelDivision(customerDivision);

            fieldValidation();
            if(!fieldValidation()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, " A new customer will be added. Do you want to continue? ");
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent()&&result.get() == ButtonType.OK) {
                    CustomerDB.saveCustomer(customerName, customerAddress, customerZip, customerPhone, createDate, createdBy, lastUpdate, lastUpdatedBy, actualCustomerDivision);
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerMain.fxml"));
                    Parent scene = loader.load();
                    scene.setStyle("-fx-font-family: 'Times New Roman';");
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, " Make sure all fields are filled correctly");
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.showAndWait();
            }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, " Make sure Country and State/Province is correct");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.showAndWait();
        }
    }


    public void setFirstLevelCombo() {
    }

    /**Gets country from user select then fills State/Province combo box with correct first level division from database */
    public void setCountryCombo() {
        String selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
        CustomerDB.getSelectedCountry(selectedCountry);
        fillFirstLevel();
    }

    /**Validates if user has entered information in all fields and boxes*/
    public boolean fieldValidation() {
        return name.getText().isEmpty() || address.getText().isEmpty() || zip.getText().isEmpty() || phone.getText().isEmpty() || firstLevelCombo.getValue().isEmpty();
    }
}

