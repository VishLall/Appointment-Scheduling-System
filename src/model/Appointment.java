package model;

import java.sql.Timestamp;
import java.time.LocalDate;

/**This class is for Appointment model.*/
public class Appointment {

    private int aptID;
    private int aptUserID;
    private String aptType;
    private Timestamp aptStartTime;
    private Timestamp aptEndTime;
    private String aptTitle;
    private String aptDescription;
    private String aptLocation;
    private Timestamp aptCreateDate;
    private String aptCreatedBy;
    private Timestamp aptLastUpdate;
    private String aptLastUpdatedBy;
    private int aptCustomerID;
    private int aptContactID;
    private String tableAptStartTime;
    private String tableAptEndTime;
    private LocalDate aptDate;


    /**Constructor for AppointmentDB.getAllAppointments()*/
    public Appointment(int aptID, int aptUserID, String aptType, Timestamp aptStartTime, Timestamp aptEndTime, String aptTitle, String aptDescription,
                       String aptLocation, Timestamp aptCreateDate, String aptCreatedBy, Timestamp aptLastUpdate,
                       String aptLastUpdatedBy, int aptCustomerID, int aptContactID, String tableAptStartTime, String tableAptEndTime) {
        this.aptID = aptID;
        this.aptUserID = aptUserID;
        this.aptType = aptType;
        this.aptStartTime = aptStartTime;
        this.aptEndTime = aptEndTime;
        this.aptTitle = aptTitle;
        this.aptDescription = aptDescription;
        this.aptLocation = aptLocation;
        this.aptCreateDate = aptCreateDate;
        this.aptCreatedBy = aptCreatedBy;
        this.aptLastUpdate = aptLastUpdate;
        this.aptLastUpdatedBy = aptLastUpdatedBy;
        this.aptCustomerID = aptCustomerID;
        this.aptContactID = aptContactID;
        this.tableAptStartTime = tableAptStartTime;
        this.tableAptEndTime = tableAptEndTime;

    }
    /**Constructor for AppointmentDB.selectedAppointmentInfo().*/
    public Appointment(int aptID, int aptUserID, String aptType, Timestamp aptStart, Timestamp aptEnd, String aptTitle, String aptDescription,
                       String aptLocation, Timestamp aptCreateDate, String aptCreatedBy, Timestamp aptLastUpdate,
                       String aptLastUpdatedBy, int aptCustomerID, int aptContactID) {
        this.aptID = aptID;
        this.aptUserID = aptUserID;
        this.aptType = aptType;
        this.aptStartTime = aptStart;
        this.aptEndTime = aptEnd;
        this.aptTitle = aptTitle;
        this.aptDescription = aptDescription;
        this.aptLocation = aptLocation;
        this.aptCreateDate = aptCreateDate;
        this.aptCreatedBy = aptCreatedBy;
        this.aptLastUpdate = aptLastUpdate;
        this.aptLastUpdatedBy = aptLastUpdatedBy;
        this.aptCustomerID = aptCustomerID;
        this.aptContactID = aptContactID;
    }
    /**Constructor for AppointmentAddController*/
    public Appointment(String aptTitle, String aptDescription, String aptLocation, String aptType, int aptContactID, LocalDate aptDate,
                       int aptCustomerId, int aptUserId, Timestamp aptCreateDate, String aptCreatedBy, Timestamp aptLastUpdate, String aptLastUpdatedBy, Timestamp timestampAptStart, Timestamp timestampAptEnd) {
        this.aptTitle = aptTitle;
        this.aptDescription = aptDescription;
        this.aptLocation = aptLocation;
        this.aptType = aptType;
        this.aptContactID = aptContactID;
        this.aptDate = aptDate;
        this.aptCustomerID = aptCustomerId;
        this.aptUserID = aptUserId;
        this.aptCreateDate = aptCreateDate;
        this.aptCreatedBy = aptCreatedBy;
        this.aptLastUpdate = aptLastUpdate;
        this.aptLastUpdatedBy =aptLastUpdatedBy;
        this.aptStartTime = timestampAptStart;
        this.aptEndTime = timestampAptEnd;
    }
    /**Constructor for AppointmentModifyController.*/
    public Appointment(String modifyAptTitle, String modifyAptDescription, String modifyAptLocation, String modifyAptType, int modifyAptContactID,
                       LocalDate modifyAptDate, int modifyAptUserID, Timestamp modifyAptLastUpdate, String modifyAptLastUpdatedBy, Timestamp timestampModAptStart, Timestamp timestampModAptEnd) {
        this.aptTitle = modifyAptTitle;
        this.aptDescription = modifyAptDescription;
        this.aptLocation = modifyAptLocation;
        this.aptType = modifyAptType;
        this.aptContactID = modifyAptContactID;
        this.aptDate = modifyAptDate;
        this.aptUserID = modifyAptUserID;
        this.aptLastUpdate = modifyAptLastUpdate;
        this.aptLastUpdatedBy = modifyAptLastUpdatedBy;
        this.aptStartTime = timestampModAptStart;
        this.aptEndTime = timestampModAptEnd;
    }


    public Timestamp getAptCreateDate() {
        return aptCreateDate;
    }

    public void setAptCreateDate(Timestamp aptCreateDate) {
        this.aptCreateDate = aptCreateDate;
    }

    public String getAptCreatedBy() {
        return aptCreatedBy;
    }

    public void setAptCreatedBy(String aptCreatedBy) {
        this.aptCreatedBy = aptCreatedBy;
    }

    public Timestamp getAptLastUpdate() {
        return aptLastUpdate;
    }

    public void setAptLastUpdate(Timestamp aptLastUpdate) {
        this.aptLastUpdate = aptLastUpdate;
    }

    public String getAptLastUpdatedBy() {
        return aptLastUpdatedBy;
    }

    public void setAptLastUpdatedBy(String aptLastUpdatedBy) {
        this.aptLastUpdatedBy = aptLastUpdatedBy;
    }

    public int getAptCustomerID() {
        return aptCustomerID;
    }

    public void setAptCustomerID(int aptCustomerID) {
        this.aptCustomerID = aptCustomerID;
    }

    public int getAptContactID() {
        return aptContactID;
    }

    public void setAptContactID(int aptContactID) {
        this.aptContactID = aptContactID;
    }

    public int getAptUserID() {
        return aptUserID;
    }

    public void setAptUserID(int aptUserID) {
        this.aptUserID = aptUserID;
    }

    public String getAptType() {
        return aptType;
    }

    public void setAptType(String aptType) {
        this.aptType = aptType;
    }

    public int getAptID() {
        return aptID;
    }

    public void setAptID(int aptID) {
        this.aptID = aptID;
    }

    public Timestamp getAptStartTime() {
        return aptStartTime;
    }

    public void setAptStartTime(Timestamp aptStartTime) {
        this.aptStartTime = aptStartTime;
    }

    public Timestamp getAptEndTime() {
        return aptEndTime;
    }

    public void setAptEndTime(Timestamp aptEndTime) {
        this.aptEndTime = aptEndTime;
    }

    public String getAptTitle() {
        return aptTitle;
    }

    public void setAptTitle(String aptTitle) {
        this.aptTitle = aptTitle;
    }

    public String getAptDescription() {
        return aptDescription;
    }

    public void setAptDescription(String aptDescription) {
        this.aptDescription = aptDescription;
    }

    public String getAptLocation() {
        return aptLocation;
    }

    public void setAptLocation(String aptLocation) {
        this.aptLocation = aptLocation;
    }

    public String getTableAptStartTime() {
        return tableAptStartTime;
    }

    public void setTableAptStartTime(String tableAptStartTime) {
        this.tableAptStartTime = tableAptStartTime;
    }

    public String getTableAptEndTime() {
        return tableAptEndTime;
    }

    public void setTableAptEndTime(String tableAptEndTime) {
        this.tableAptEndTime = tableAptEndTime;
    }










}
