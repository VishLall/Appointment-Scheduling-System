package model;

/**This class if for Contact model*/
public class Contact {
    private int contactID;
    private String contactName;

    /**Constructor for ContactDB*/
    public Contact(int contactID, String contactName) {
        this.contactID = contactID;
        this.contactName = contactName;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    /**Override toString to display as "ID:"Contact ID - Contact Name"*/
    public  String toString(){
        return ("ID: "+ Integer.toString(contactID)+ " - "+ contactName );
    }
}
