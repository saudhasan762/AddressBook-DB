package db;
import java.util.List;

public class AddressBookService {

    private List<Contact> contactList;
    private final AddressBookDBService addressBookDBService;

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
}
