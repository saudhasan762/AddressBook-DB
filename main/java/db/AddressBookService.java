package db;
import java.time.LocalDate;
import java.util.List;

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


}
