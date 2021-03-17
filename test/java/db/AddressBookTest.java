package db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

public class AddressBookTest {

    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchEmployeeCount(){
        AddressBookService addressBookService = new AddressBookService();
        List<Contact> contactList = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        Assertions.assertEquals(3, contactList.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB(){
        AddressBookService addressBookService = new AddressBookService();
        List<Contact> contactList = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        addressBookService.updateContactAddress("Saud","Clement Town");
        boolean result = addressBookService.checkAddressBookInSyncWithDB("Saud");
        Assertions.assertTrue(result);
    }
}
