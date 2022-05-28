package controller;

import model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerAddControllerTest {
    Customer c = new Customer("John Doe", "1234 One Dr","123456", "123456789",20);


    @Test
    void nameFieldValidation() {
        assertFalse(c.getCustomerName().isEmpty());
    }

    @Test
    void addressFieldValidation(){
        assertFalse(c.getCustomerAddress().isEmpty());
    }

    @Test
    void zipFieldValidation(){
        assertFalse(c.getCustomerZip().isEmpty());
    }

    @Test
    void phoneFieldValidation(){
        assertFalse(c.getCustomerPhone().isEmpty());
    }

    @Test
    void divisionFieldValidation(){
        assertEquals(20, c.getCustomerDivisionId());
    }
}