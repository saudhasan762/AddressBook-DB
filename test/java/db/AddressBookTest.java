package db;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        Assert.assertEquals(3, contactList.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB(){
        AddressBookService addressBookService = new AddressBookService();
        List<Contact> contactList = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        addressBookService.updateContactAddress("Saud","Clement Town");
        boolean result = addressBookService.checkAddressBookInSyncWithDB("Saud");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchContact(){
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018,1,1);
        LocalDate endDate = LocalDate.now();
        List<Contact> contactList = addressBookService.readAddressBookDataForDateRange(AddressBookService.IOService.DB_IO,startDate,endDate);
        Assert.assertEquals(3,contactList.size());
    }

    @Test
    public void givenAddressBook_WhenRetrievedByCityOrState_ShouldMatchContact(){
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        String city = "Jaipur";
        String state = "Uttrakhand";
        List<Contact> contactList = addressBookService.readAddressBookDataForCityOrState(AddressBookService.IOService.DB_IO,city,state);
        Assert.assertEquals(3,contactList.size());
    }

    @Test
    public void givenNewContact_WhenAdded_ShouldSyncWithDB(){
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        addressBookService.addContactToBook("Amit","Kumar","Subhash Nagar", "Lucknow", "UP",456556, 569845125,"amitK@gmail.com",LocalDate.now());
        boolean result = addressBookService.checkAddressBookInSyncWithDB("Amit");
        Assert.assertTrue(result);
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
        Assert.assertEquals(9,addressBookService.countEntries(AddressBookService.IOService.DB_IO));
    }

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public Contact[] getContactList() {
        Response response = RestAssured.get("/address_book");
        System.out.println("CONTACT ENTRIES IN JSONServer:\n" + response.asString());
        Contact[] arrayOfContact = new Gson().fromJson(response.asString(), Contact[].class);
        return arrayOfContact;
    }

    public Response addContactToJsonServer(Contact contact) {
        String empJson = new Gson().toJson(contact);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(empJson);
        return request.post("/address_book");
    }

    @Test
    public void givenAddressBookInJsonServer_WhenRetrieved_ShouldMatchTheCount() {
        Contact[] arrayOfContact = getContactList();
        AddressBookService addressBookService;
        addressBookService = new AddressBookService(Arrays.asList(arrayOfContact));
        long entries = addressBookService.countEntries(AddressBookService.IOService.REST_IO);
        Assert.assertEquals(2, entries);
    }

    @Test
    public void givenListOfContact_WhenAdded_ShouldMatch201ResponseAndCount(){
        AddressBookService addressBookService;
        Contact[] arrayOfContact = getContactList();
        addressBookService = new AddressBookService(Arrays.asList(arrayOfContact));

        Contact[] arrayOfContactList = {
                new Contact("Utkarsh","Sharma","Jhalawar","Jaipur","Rajasthan", 632500, 45986217, "sharmaU@gmail.com"),
                new Contact("Rishav", "Raj", "Motihari", "Patna", "Bihar",452998, 45215689, "rishav@gmail.com"),
                new Contact("Amit","Kumar","Gandhi Nagar", "Lucknow","UP",584796,45631548,"kumarAmit@gmail.com")
        };
        for (Contact contact : arrayOfContactList){
            Response response = addContactToJsonServer(contact);
            int statusCode = response.getStatusCode();
            Assert.assertEquals(201, statusCode);

            contact = new Gson().fromJson(response.asString(), Contact.class);
            addressBookService.addContactToBook(contact, AddressBookService.IOService.REST_IO);
        }
        long entries = addressBookService.countEntries(AddressBookService.IOService.REST_IO);
        Assert.assertEquals(5,entries);
    }

}
