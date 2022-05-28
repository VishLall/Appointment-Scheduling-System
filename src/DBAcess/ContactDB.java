package DBAcess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This class is for all Contact Database retrieval.*/
public class ContactDB {

    public static int selectedAppointmentContactID;
    /**Gets selectedAppointmentContactID from AppointmentMainController*/
    public static void getSelectedAppointmentContactID(int modifyContactID){
        selectedAppointmentContactID = modifyContactID;
    }

    /**Retrieves all Contact info from database*/
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try {
            String sql = "Select * from client_schedule.contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                Contact c = new Contact(contactID, contactName);
                contactList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    /**List used populate contact modify screen*/
    public static ObservableList<Contact>populateModifyContact(){
        ObservableList<Contact>contact = FXCollections.observableArrayList();
        try {
            String sql = "Select * from client_schedule.contacts Where Contact_ID= "+selectedAppointmentContactID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                Contact c = new Contact( contactID, contactName);
                contact.add(c);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return contact;
    }

}
