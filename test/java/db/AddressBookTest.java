package db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
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

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchContact(){
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018,1,1);
        LocalDate endDate = LocalDate.now();
        List<Contact> contactList = addressBookService.readAddressBookDataForDateRange(AddressBookService.IOService.DB_IO,startDate,endDate);
        Assertions.assertEquals(3,contactList.size());
    }

    @Test
    public void givenAddressBook_WhenRetrievedByCityOrState_ShouldMatchContact(){
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        String city = "Jaipur";
        String state = "Uttrakhand";
        List<Contact> contactList = addressBookService.readAddressBookDataForCityOrState(AddressBookService.IOService.DB_IO,city,state);
        Assertions.assertEquals(3,contactList.size());
    }

    @Test
    public void givenNewContact_WhenAdded_ShouldSyncWithDB(){
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        addressBookService.addContactToBook("Amit","Kumar","Subhash Nagar", "Lucknow", "UP",456556, 569845125,"amitK@gmail.com",LocalDate.now());
        boolean result = addressBookService.checkAddressBookInSyncWithDB("Amit");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenEmployees_WhenAdded_ShouldMatchEmployeeEntries(){
        Contact[] arrayOfEmp = {
                new Contact("Jeff", "Bezos","New Nagar", "Amritsar","Punjab",365214,45632178,"bezos@gmail.com",LocalDate.now()),
                new Contact("Bill", "Gates", "villa","Allahabad","MP",124859,941562221,"biili@gmail.com",LocalDate.now()),
                new Contact("Mark", "Zuckerberg","chowk","New Delhi","Delhi",456987,556665478,"mark@gmail.com", LocalDate.now()),
                new Contact("Sunder","Pichai","Bangaluru","Bangalore","Karnataka",458741,548712639,"Psundar@gmail.com", LocalDate.now()),
                new Contact("Mukesh","Ambani","Bandra","Mumbai","Maharashtra",452698,54871259,"ambani@gmail.com", LocalDate.now()),
                new Contact("Anil","Kumar","New Colony","Haridwar","Uttrakhand",254879,465445654,"kumaranil@gmail.com", LocalDate.now()),
        };
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        Instant threadStart = Instant.now();
        addressBookService.addContactToBookWithThreads(Arrays.asList(arrayOfEmp));
        Instant threadEnd = Instant.now();
        System.out.println("Duration with Thread: "+Duration.between(threadStart, threadEnd));
        Assertions.assertEquals(9,addressBookService.countEntries(AddressBookService.IOService.DB_IO));
    }
}
