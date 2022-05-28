package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import DBAcess.CustomerDB;

/**This class is for the customer main controller*/
public class CustomerMainController implements Initializable {

    @FXML
    private TableView<Customer> customerTable;
    
    @FXML
    private TableColumn<Customer, Integer> customerId;
    
    @FXML
    private TableColumn<Customer, String> customerName;

    Stage stage;

    /**Initialize for main customer screen*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerTable.setItems(CustomerDB.getAllCustomers());
    }

    /**Scene change to main screen*/
    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
        Parent scene = loader.load();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Scene change to customer add screen*/
    @FXML
    void handleAddButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerAdd.fxml"));
        Parent scene = loader.load();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Deletes selected customer from table*/
    @FXML
    void handleDeleteButton() {
        try{
            int selectedCustomerID =  customerTable.getSelectionModel().getSelectedItem().getCustomerId();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This Customer will be deleted.");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family:'Times New Roman';");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()&&result.get() == ButtonType.OK){
                CustomerDB.deleteCustomer(selectedCustomerID);
            }
            customerTable.setItems(CustomerDB.getAllCustomers());
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, " Please select a Customer");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.showAndWait();
        }

    }

    /**Scene change to customer modify screen*/
    @FXML
    void handleModifyButton(ActionEvent event) throws IOException {
        try{
            int selectedCustomer = customerTable.getSelectionModel().getSelectedItem().getCustomerId();
            CustomerModifyController.getSelectedCustomer(selectedCustomer);
            CustomerDB.getSelectedCustomer(selectedCustomer);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerModify.fxml"));
            Parent scene = loader.load();
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene.setStyle("-fx-font-family: 'Times New Roman';");
            stage.setScene(new Scene(scene));
            stage.show();
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, " Please select a Customer");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.showAndWait();
        }




    }

}
