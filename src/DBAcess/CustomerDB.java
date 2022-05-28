package DBAcess;

import java.sql.*;
import java.time.LocalDateTime;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

/**This class is for all Customer Database retrieval.*/
public class CustomerDB {

    public static int selectedAptCustomerID;
    private static int selectedCustomer;
    public static String selectedCountry;

    /**Gets selectedAptCustomerID from AppointmentMainController*/
    public static void getSelectedAptCustomerID(int modifyCustomerID){
        selectedAptCustomerID = modifyCustomerID;
    }

    /**Gets selectedCountry from AppointmentMainController*/
    public static void getSelectedCountry(String country){
        selectedCountry = country;
    }

    /**Gets selectedCustomer from CustomerMainController */
    public static void getSelectedCustomer(int customerToModify) {
        selectedCustomer = customerToModify;
    }

    /**Retrieves all customer info from database*/
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomersList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM client_schedule.customers";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerZip = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                String lastUpdate = rs.getString("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int customerDivisionId = rs.getInt("Division_ID");

                Customer c = new Customer(customerId, customerName, customerAddress, customerZip, customerPhone,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, customerDivisionId);

                allCustomersList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomersList;
    }

    /**Saves new customer from all user entered customer info into database*/
    public static void saveCustomer(String customerName, String customerAddress, String customerPhone, String customerZip,
                                    Timestamp createDate, String createdBy, LocalDateTime LocalDateTime, String lastUpdatedBy, int customerDivision) {

        try {
            String sql = "INSERT INTO client_schedule.customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone," +
                    " Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPhone);
            ps.setString(4, customerZip);
            ps.setTimestamp(5, createDate.valueOf(LocalDateTime.now()));
            ps.setString(6, createdBy);
            ps.setString(7, LocalDateTime.now().toString());
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, customerDivision);

            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**Updates selected customer with user entered info*/
    public static void updateCustomer(int customerID, String customerName, String customerAddress, String customerPhone, String customerZip,
                        LocalDateTime LocalDateTime, String lastUpdatedBy, int customerDivision) {
        try {
            String sql = "Update client_schedule.customers Set Customer_Name= "+'?'+" ,Address= "+'?'+",Postal_Code= "+'?'+" ,Phone= "+'?'+" ,Last_Update= "+'?'+" ,Last_Updated_By= "+'?'+" ,Division_ID= "+'?'+" Where Customer_ID= "+customerID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPhone);
            ps.setString(4, customerZip);
            ps.setString(5, LocalDateTime.now().toString());
            ps.setString(6, lastUpdatedBy);
            ps.setInt(7, customerDivision);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**Get First level division from database */
    public static int getFirstLevelDivision(String customerDivision){
        int actualCustomerDivision = 0;

        try{
            String sql = "Select * from client_schedule.first_level_divisions where Division = '"+ customerDivision +"'";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                actualCustomerDivision = rs.getInt("Division_ID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return actualCustomerDivision;
    }
    /**Deletes customer from database*/
    public static void  deleteCustomer(int selectedCustomer){
        String sql = " Delete from client_schedule.customers where Customer_ID = '" + selectedCustomer + "'";

        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**List used to populate fields and boxes in customer modify*/
    public static ObservableList<Customer>populateModifyCustomer(){
        ObservableList<Customer>customer = FXCollections.observableArrayList();

        try {
            String sql = "Select * from client_schedule.customers where Customer_ID= "+selectedAptCustomerID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");

                Customer c = new Customer(customerID, customerName);
                customer.add(c);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return customer;
    }

    /**List used to populate country combo box*/
    public static ObservableList<String>fillCountryCombo(){
        ObservableList<String>countries = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM client_schedule.countries;";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                countries.add(rs.getString(2));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }

    /**Used to filter first level divisions when country is selected*/
    public static ObservableList<String> firstLevelFilter(){
        ObservableList<String> firstLevels = FXCollections.observableArrayList();

        if(selectedCountry.equals("U.S")){
            firstLevels.clear();
            try{
                String sql = "SELECT * FROM client_schedule.first_level_divisions where Country_ID = 1;";

                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    firstLevels.add(rs.getString(2));
                }

            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(selectedCountry.equals("UK")){
            firstLevels.clear();
            try{
                String sql = "SELECT * FROM client_schedule.first_level_divisions where Country_ID = 2;";

                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    firstLevels.add(rs.getString(2));
                }

            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            firstLevels.clear();
            try{
                String sql = "SELECT * FROM client_schedule.first_level_divisions where Country_ID = 3;";

                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    firstLevels.add(rs.getString(2));
                }

            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return firstLevels;
    }

    /**Used to populate first level combo in customer modify*/
    public static ObservableList<String> populateModFirstLevelCombo(){
        ObservableList<String> division = FXCollections.observableArrayList();
        try{
            String sql =  "Select c.Customer_ID, c.Division_ID, f.Division, f.Country_ID, d.Country\n" +
                    "From client_schedule.customers AS c\n" +
                    "Join client_schedule.first_level_divisions as f\n" +
                    "ON c.Division_ID = f.Division_ID\n" +
                    "Join client_schedule.countries as d\n" +
                    "ON f.Country_ID = d.Country_ID where Customer_ID ="+ selectedCustomer;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                division.add(rs.getString("Division"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return division;
    }

    /**Used to populate country combo in customer modify*/
    public static ObservableList<String> populateModCountryCombo(){
        ObservableList<String> countryCombo = FXCollections.observableArrayList();
        try{
            String sql =  "Select c.Customer_ID, c.Division_ID, f.Division, f.Country_ID, d.Country\n" +
                    "From client_schedule.customers AS c\n" +
                    "Join client_schedule.first_level_divisions as f\n" +
                    "ON c.Division_ID = f.Division_ID\n" +
                    "Join client_schedule.countries as d\n" +
                    "ON f.Country_ID = d.Country_ID where Customer_ID ="+ selectedCustomer;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                countryCombo.add(rs.getString("Country"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
            return countryCombo;
    }

    /**Used to populate customer fields*/
    public static ObservableList<Customer> populateCustomerModFields(){
        ObservableList<Customer> selectedCustomerModInfo = FXCollections.observableArrayList();
        try {
            String sql = "Select * from client_schedule.customers Where Customer_ID = " + selectedCustomer;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerZip = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");

                Customer c = new Customer(customerName, customerAddress, customerZip, customerPhone);
                selectedCustomerModInfo.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedCustomerModInfo;
    }

}
