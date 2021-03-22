package db;

import java.time.LocalDate;
import java.util.Objects;

public class Contact {
    public int id;
    public String firstName;
    public String lastName;
    public String address;
    public String city;
    public String state;
    public int zip;
    public int phoneNumber;
    public String email;
    public LocalDate startDate;

    Contact(String firstName, String lastName, String address, String city, String state,int zip, int phoneNumber, String email ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Contact(String firstName, String lastName, String address, String city, String state, int zip, int phoneNumber, String email, LocalDate startDate) {
        this(firstName,lastName,address,city,state,zip,phoneNumber,email);
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "first='" + firstName + '\'' +
                ", last='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email=" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return zip == contact.zip && phoneNumber == contact.phoneNumber &&
                Objects.equals(firstName, contact.firstName) && Objects.equals(lastName, contact.lastName) &&
                Objects.equals(address, contact.address) && Objects.equals(city, contact.city) &&
                Objects.equals(state, contact.state) && Objects.equals(email, contact.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, address, city, state, zip, phoneNumber, email, startDate);
    }
}
