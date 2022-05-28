package DBAcess;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import model.Appointment;

/**This class is for all Appointment Database retrieval.*/
public class AppointmentDB {

    public static int selectedAppointmentID;
    public static int selectedComboCustomerID;
    public static int currentUserID;

    /**Gets selectedAppointmentID from AppointmentMainController*/
    public static void getSelectedAppointmentID(int appointmentToModify) {
        selectedAppointmentID = appointmentToModify;
    }

    /**Gets selectedComboCustomerID from AppointmentAddController.handleSaveButton*/
    public static void getSelectedComboCustomerID(int customerIdOverLap) {
        selectedComboCustomerID = customerIdOverLap;
    }

    /**Gets currentUserID from Login Screen*/
    public static void getCurrentUserID(int currentUserId) {
        currentUserID = currentUserId;
    }

    /**Retrieves all appointment data from database */
    public static ObservableList<Appointment> getAllAppointments() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy hh:mm a");

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);


        try {
            String sql = "Select * from client_schedule.appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int aptID = rs.getInt("Appointment_ID");
                String aptTitle = rs.getString("Title");
                String aptDescription = rs.getString("Description");
                String aptLocation = rs.getString("Location");
                String aptType = rs.getString("Type");
                Timestamp aptStartTime = rs.getTimestamp("Start");
                Timestamp aptEndTime = rs.getTimestamp("End");
                Timestamp aptCreateDate = rs.getTimestamp("Create_Date");
                String aptCreatedBy = rs.getString("Created_By");
                Timestamp aptLastUpdate = rs.getTimestamp("Last_Update");
                String aptLastUpdatedBy = rs.getString("Last_Updated_By");
                int aptCustomerID = rs.getInt("Customer_ID");
                int aptUserID = rs.getInt("User_ID");
                int aptContactID = rs.getInt("Contact_ID");

                String tableAptStartTime = aptStartTime.toLocalDateTime().format(dtf);
                String tableAptEndTime = aptEndTime.toLocalDateTime().format(dtf);


                Appointment apt = new Appointment(aptID, aptUserID, aptType, aptStartTime, aptEndTime, aptTitle, aptDescription,
                        aptLocation, aptCreateDate, aptCreatedBy, aptLastUpdate, aptLastUpdatedBy, aptCustomerID, aptContactID, tableAptStartTime, tableAptEndTime);

                allAppointments.add(apt);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return allAppointments;
    }

    /**Saves new appointment from all user entered appointment info into database*/
    public static void saveAppointment(int aptUserID, String aptType, String aptTitle, String aptDescription,
                                       String aptLocation, Timestamp aptCreateDate, Timestamp aptLastUpdate,
                                       int aptCustomerID, int aptContactID, String aptStartTime, String aptEndTime, String aptCreatedBy, String aptLastUpdatedBy) {
        try {
            String sql = "Insert into client_schedule.appointments(Appointment_ID, Title, Description, Location, Type," +
                    "  Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) Values(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, aptTitle);
            ps.setString(2, aptDescription);
            ps.setString(3, aptLocation);
            ps.setString(4, aptType);
            ps.setTimestamp(5, Timestamp.valueOf((aptStartTime)));
            ps.setTimestamp(6, Timestamp.valueOf((aptEndTime)));
            ps.setTimestamp(7, aptCreateDate);
            ps.setString(8, aptCreatedBy);
            ps.setTimestamp(9, aptLastUpdate);
            ps.setString(10, aptLastUpdatedBy);
            ps.setInt(11, aptCustomerID);
            ps.setInt(12, aptUserID);
            ps.setInt(13, aptContactID);


            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**Updates selected appointment with user entered info*/
    public static void updateAppointment(String aptTitle, String aptDescription, String aptLocation, int contactID, String aptType, String aptStartTime,
                                         String aptEndTime, Timestamp aptLastUpdate, String aptLastUpdatedBy, int aptUserID) {
        try {
            String sql = "Update client_schedule.appointments Set Title=" + '?' + " ,Description=" + '?' + " ,Location=" + '?' + " ,Type=" + '?' + "," +
                    "Start=" + '?' + " ,End=" + '?' + " ,Last_Update=" + '?' + " ,Last_Updated_By=" + '?' + " ," +
                    "User_ID=" + '?' + " ,Contact_ID=" + '?' + " Where Appointment_ID =" + selectedAppointmentID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, aptTitle);
            ps.setString(2, aptDescription);
            ps.setString(3, aptLocation);
            ps.setString(4, aptType);
            ps.setTimestamp(5, Timestamp.valueOf(aptStartTime));
            ps.setTimestamp(6, Timestamp.valueOf(aptEndTime));
            ps.setTimestamp(7, aptLastUpdate);
            ps.setString(8, aptLastUpdatedBy);
            ps.setInt(9, aptUserID);
            ps.setInt(10, contactID);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**Deleted selected appointment from database*/
    public static void deleteAppointment(int selectedAppointment) {
        try {
            String sql = "Delete from client_schedule.appointments where Appointment_ID= " + selectedAppointment;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**Used to get times for combo boxes*/
    public static ObservableList<String> comboTimes() {
        ObservableList<String> times = FXCollections.observableArrayList();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");

        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        String formatStart;

        while (start.isBefore(end)) {
            start = start.plusMinutes(30);
            formatStart = start.format(dtf);
            times.add(formatStart);
        }
        return times;
    }

    /**Used to fill fields and boxes on appointment modify*/
    public static ObservableList<Appointment> selectedAppointmentInfo(int appointmentID) {

        ObservableList<Appointment> appointmentInfoList = FXCollections.observableArrayList();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");
        try {
            String sql = "Select * from client_schedule.appointments Where Appointment_ID= " + appointmentID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int aptID = rs.getInt("Appointment_ID");
                String aptTitle = rs.getString("Title");
                String aptDescription = rs.getString("Description");
                String aptLocation = rs.getString("Location");
                String aptType = rs.getString("Type");
                Timestamp aptStart = rs.getTimestamp("Start");
                Timestamp aptEnd = rs.getTimestamp("End");
                Timestamp aptCreateDate = rs.getTimestamp("Create_Date");
                String aptCreatedBy = rs.getString("Created_By");
                Timestamp aptLastUpdate = rs.getTimestamp("Last_Update");
                String aptLastUpdatedBy = rs.getString("Last_Updated_By");
                int aptCustomerID = rs.getInt("Customer_ID");
                int aptUserID = rs.getInt("User_ID");
                int aptContactID = rs.getInt("Contact_ID");

                Appointment aptInfo = new Appointment(aptID, aptUserID, aptType, aptStart, aptEnd, aptTitle, aptDescription,
                        aptLocation, aptCreateDate, aptCreatedBy, aptLastUpdate, aptLastUpdatedBy, aptCustomerID, aptContactID);
                appointmentInfoList.add(aptInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentInfoList;
    }

    /**List by customer id to compare overlaps*/
    public static ObservableList<Appointment> getAptOverlapList() {
        ObservableList<Appointment> aptOverlapList = FXCollections.observableArrayList();
        try {
            String sql = "Select * from client_schedule.appointments where Customer_ID =" + selectedComboCustomerID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int aptID = rs.getInt("Appointment_ID");
                String aptTitle = rs.getString("Title");
                String aptDescription = rs.getString("Description");
                String aptLocation = rs.getString("Location");
                String aptType = rs.getString("Type");
                Timestamp aptStart = rs.getTimestamp("Start");
                Timestamp aptEnd = rs.getTimestamp("End");
                Timestamp aptCreateDate = rs.getTimestamp("Create_Date");
                String aptCreatedBy = rs.getString("Created_By");
                Timestamp aptLastUpdate = rs.getTimestamp("Last_Update");
                String aptLastUpdatedBy = rs.getString("Last_Updated_By");
                int aptCustomerID = rs.getInt("Customer_ID");
                int aptUserID = rs.getInt("User_ID");
                int aptContactID = rs.getInt("Contact_ID");

                Appointment a = new Appointment(aptID, aptUserID, aptType, aptStart, aptEnd, aptTitle, aptDescription,
                        aptLocation, aptCreateDate, aptCreatedBy, aptLastUpdate, aptLastUpdatedBy, aptCustomerID, aptContactID);
                aptOverlapList.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aptOverlapList;
    }

    /**List by customer id to compare overlaps on modify*/
    public static ObservableList<Appointment> getModAptOverlapList(int selectModCustomerID) {
        ObservableList<Appointment> modAptOverlapList = FXCollections.observableArrayList();
        try {
            String sql = "Select * from client_schedule.appointments where Appointment_ID !=" + selectedAppointmentID + " AND Customer_ID =" + selectModCustomerID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int aptID = rs.getInt("Appointment_ID");
                String aptTitle = rs.getString("Title");
                String aptDescription = rs.getString("Description");
                String aptLocation = rs.getString("Location");
                String aptType = rs.getString("Type");
                Timestamp aptStart = rs.getTimestamp("Start");
                Timestamp aptEnd = rs.getTimestamp("End");
                Timestamp aptCreateDate = rs.getTimestamp("Create_Date");
                String aptCreatedBy = rs.getString("Created_By");
                Timestamp aptLastUpdate = rs.getTimestamp("Last_Update");
                String aptLastUpdatedBy = rs.getString("Last_Updated_By");
                int aptCustomerID = rs.getInt("Customer_ID");
                int aptUserID = rs.getInt("User_ID");
                int aptContactID = rs.getInt("Contact_ID");

                Appointment a = new Appointment(aptID, aptUserID, aptType, aptStart, aptEnd, aptTitle, aptDescription,
                        aptLocation, aptCreateDate, aptCreatedBy, aptLastUpdate, aptLastUpdatedBy, aptCustomerID, aptContactID);
                modAptOverlapList.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modAptOverlapList;
    }

    /**Appointment list by user id*/
    public static ObservableList<Appointment> getUserAptList() {
        ObservableList<Appointment> userAptList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM client_schedule.appointments where User_ID=" + currentUserID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int aptID = rs.getInt("Appointment_ID");
                String aptTitle = rs.getString("Title");
                String aptDescription = rs.getString("Description");
                String aptLocation = rs.getString("Location");
                String aptType = rs.getString("Type");
                Timestamp aptStart = rs.getTimestamp("Start");
                Timestamp aptEnd = rs.getTimestamp("End");
                Timestamp aptCreateDate = rs.getTimestamp("Create_Date");
                String aptCreatedBy = rs.getString("Created_By");
                Timestamp aptLastUpdate = rs.getTimestamp("Last_Update");
                String aptLastUpdatedBy = rs.getString("Last_Updated_By");
                int aptCustomerID = rs.getInt("Customer_ID");
                int aptUserID = rs.getInt("User_ID");
                int aptContactID = rs.getInt("Contact_ID");

                Appointment a = new Appointment(aptID, aptUserID, aptType, aptStart, aptEnd, aptTitle, aptDescription,
                        aptLocation, aptCreateDate, aptCreatedBy, aptLastUpdate, aptLastUpdatedBy, aptCustomerID, aptContactID);
                userAptList.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userAptList;
    }

    /**Appointment types*/
    public static ObservableList<String> getAptTypeList() {
        ObservableList<String> aptTypeList = FXCollections.observableArrayList();
        aptTypeList.add("Oil Change");
        aptTypeList.add("Alignment");
        aptTypeList.add("General Repair");
        aptTypeList.add("Maintenance");
        aptTypeList.add("Tires");
        return aptTypeList;
    }
    public static ObservableList<String> getAptDescList() {
        ObservableList<String> aptDescList = FXCollections.observableArrayList();
        aptDescList.add("Acura");
        aptDescList.add("Audi");
        aptDescList.add("BMW ");
        aptDescList.add("Cadillac");
        aptDescList.add("Chevrolet");
        aptDescList.add("Chrysler");
        aptDescList.add("Dodge");
        aptDescList.add("Ford");
        aptDescList.add("GMC");
        aptDescList.add("Honda");
        aptDescList.add("Hyundai");
        aptDescList.add("Mercedes-Benz");
        aptDescList.add("Toyota");
        aptDescList.add("Volkswagen");
        return aptDescList;
    }

    /**Used for month by type report*/
    public static ObservableList<Appointment> getReportTypeList(String month, String type) {
        ObservableList<Appointment> reportTypeList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM client_schedule.appointments where monthname(Start)="+'?'+"And Type="+'?'+"";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, month);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int aptID = rs.getInt("Appointment_ID");
                String aptTitle = rs.getString("Title");
                String aptDescription = rs.getString("Description");
                String aptLocation = rs.getString("Location");
                String aptType = rs.getString("Type");
                Timestamp aptStart = rs.getTimestamp("Start");
                Timestamp aptEnd = rs.getTimestamp("End");
                Timestamp aptCreateDate = rs.getTimestamp("Create_Date");
                String aptCreatedBy = rs.getString("Created_By");
                Timestamp aptLastUpdate = rs.getTimestamp("Last_Update");
                String aptLastUpdatedBy = rs.getString("Last_Updated_By");
                int aptCustomerID = rs.getInt("Customer_ID");
                int aptUserID = rs.getInt("User_ID");
                int aptContactID = rs.getInt("Contact_ID");

                Appointment a = new Appointment(aptID, aptUserID, aptType, aptStart, aptEnd, aptTitle, aptDescription,
                        aptLocation, aptCreateDate, aptCreatedBy, aptLastUpdate, aptLastUpdatedBy, aptCustomerID, aptContactID);
                reportTypeList.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportTypeList;
    }

    /**Used for contact schedule table*/
    public static ObservableList<Appointment> getContactAptList(int contactID) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy hh:mm a");
        try {
            String sql = "SELECT * FROM client_schedule.appointments where Contact_ID="+contactID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int aptID = rs.getInt("Appointment_ID");
                String aptTitle = rs.getString("Title");
                String aptDescription = rs.getString("Description");
                String aptLocation = rs.getString("Location");
                String aptType = rs.getString("Type");
                Timestamp aptStartTime = rs.getTimestamp("Start");
                Timestamp aptEndTime = rs.getTimestamp("End");
                Timestamp aptCreateDate = rs.getTimestamp("Create_Date");
                String aptCreatedBy = rs.getString("Created_By");
                Timestamp aptLastUpdate = rs.getTimestamp("Last_Update");
                String aptLastUpdatedBy = rs.getString("Last_Updated_By");
                int aptCustomerID = rs.getInt("Customer_ID");
                int aptUserID = rs.getInt("User_ID");
                int aptContactID = rs.getInt("Contact_ID");

                String tableAptStartTime = aptStartTime.toLocalDateTime().format(dtf);
                String tableAptEndTime = aptEndTime.toLocalDateTime().format(dtf);

                Appointment a = new Appointment(aptID, aptUserID, aptType, aptStartTime, aptEndTime, aptTitle, aptDescription,
                        aptLocation, aptCreateDate, aptCreatedBy, aptLastUpdate, aptLastUpdatedBy, aptCustomerID, aptContactID, tableAptStartTime, tableAptEndTime);
                appointments.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

}




