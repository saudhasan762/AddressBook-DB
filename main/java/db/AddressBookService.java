package db;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookService {

    private List<Contact> contactList;
    private final AddressBookDBService addressBookDBService;

    public boolean checkAddressBookInSyncWithDB(String name) {
        List<Contact> contactList = addressBookDBService.getAddressBookData(name);
        return contactList.get(0).equals(getAddressBookData(name));
    }

    public List<Contact> readAddressBookDataForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if(ioService.equals(IOService.DB_IO))
            this.contactList = addressBookDBService.getContactForDateRange(startDate,endDate);
        return this.contactList;
    }

    public List<Contact> readAddressBookDataForCityOrState(IOService ioService, String city, String state) {
        if(ioService.equals(IOService.DB_IO))
            this.contactList = addressBookDBService.getContactForCityOrState(city,state);
        return this.contactList;
    }

    public void addContactToBook(String firstName, String lastName, String address, String city, String state, int zip, int phoneNumber, String email, LocalDate startDate) {
        contactList.add(addressBookDBService.addContactToBook(firstName,lastName,address,city,state,zip,phoneNumber,email,startDate));
    }
    
    public void addContactToBookWithThreads(List<Contact> contactList) {
        Map<Integer, Boolean> integerBooleanMap = new HashMap<>();
        contactList.forEach(contact -> {
            Runnable task = () -> {
                integerBooleanMap.put(contact.hashCode(), false);
                System.out.println("Contact Being Added: "+Thread.currentThread().getName());
                this.addContactToBook(contact.first, contact.last, contact.address, contact.city, contact.state,
                        contact.zip, contact.phoneNumber, contact.email, contact.startDate);
                integerBooleanMap.put(contact.hashCode(), true);
                System.out.println("Contact Added: "+Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, contact.first);
            thread.start();
        });
        while (integerBooleanMap.containsValue(false)){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
        System.out.println(contactList);
    }

    public enum IOService{CONSOLE_IO,FILE_IO,DB_IO,REST_IO}

    public AddressBookService(List<Contact> contactList){
        this();
        this.contactList = contactList;
    }

    public AddressBookService() {
        addressBookDBService = AddressBookDBService.getInstance();
    }

    public List<Contact> readAddressBookData(IOService ioService){
        if(ioService.equals(IOService.DB_IO))
            this.contactList = addressBookDBService.readData();
        return this.contactList;
    }

    public void updateContactAddress(String firstname, String address) {
        int result = addressBookDBService.updateAddressBookData(firstname,address);
        if (result == 0) return;
        Contact contact = this.getAddressBookData(firstname);
        if (contact != null) contact.address = address;
    }

    private Contact getAddressBookData(String name) {
        return this.contactList.stream()
                .filter(addressBookDataItem -> addressBookDataItem.first.equals(name) )
                .findFirst()
                .orElse(null);
    }
    public long countEntries(IOService ioService) {
        return contactList.size();
    }


}
