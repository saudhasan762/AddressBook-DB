package db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

public class AddressBookTest {

    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchEmployeeCount(){
        AddressBookService employeePayrollService = new AddressBookService();
        List<Contact> contactList = employeePayrollService.readAddressBookData(AddressBookService.IOService.DB_IO);
        Assertions.assertEquals(3, contactList.size());
    }
}
