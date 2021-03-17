package db;

public class Contact {
    private String first;
    private String last;
    private String address;
    private String city;
    private String state;
    private int zip;
    private int phoneNumber;
    private String email;


    Contact(String first, String last, String address, String city, String state,int zip, int phoneNumber, String email ){
        this.first = first;
        this.last = last;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "first='" + first + '\'' +
                ", last='" + last + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email=" + email + '\'' +
                '}';
    }
}
