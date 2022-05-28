package DBAcess;

import helper.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

/**This class is for all User Database retrieval.*/
public class UserDB {

    public static int selectAptUserID;

    /**Gets selectedAptUserID from AppointmentMainController*/
    public static void getSelectedAptUserID(int modifyUserID){
        selectAptUserID = modifyUserID;
    }

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    /**Used to verify if user is in database*/
    public static Boolean login(String username, String password){
        try{
            String sql = "Select * from client_schedule.users where User_Name= '"+ username+"' and Password= '"+password+"'";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                currentUser = new User();
                currentUser.setUsername(rs.getString("User_Name"));
                AppointmentDB.getCurrentUserID(rs.getInt("User_ID"));
                ps.close();
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**Retrieves all user info from database*/
    public static ObservableList<User>getAllUsers(){
        ObservableList<User>userList = FXCollections.observableArrayList();
        try{
            String sql = "Select * from client_schedule.users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userID = rs.getInt("User_ID");
                String username = rs.getString("User_Name");

                User u = new User(username, userID);
                userList.add(u);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userList;
    }

    /**User to populate user combo box in modify*/
    public static ObservableList<User>populateModifyUser(){
        ObservableList<User>user =  FXCollections.observableArrayList();
        try {
            String sql = "Select * from client_schedule.users where User_ID= "+selectAptUserID;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");

                User u = new User( userName, userID);
                user.add(u);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

}
