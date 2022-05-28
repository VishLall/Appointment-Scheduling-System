package model;

public class User {
    private String username;
    private int userID;

    public User(String username, int userID) {
        this.username = username;
        this.userID = userID;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    /**Override toString to display as "ID: User ID - User Name"*/
    @Override
    public  String toString(){
        return ("ID: "+ Integer.toString(userID)+ " - "+ username.toUpperCase() );
    }
}
