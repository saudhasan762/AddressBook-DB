package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
    private static AddressBookDBService addressBookDBService;
    private AddressBookDBService(){}

    public static AddressBookDBService getInstance(){
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
        String userName = "root";
        String password = "upes@123";
        Connection connection;
        System.out.println("Connecting to database:"+jdbcURL);
        connection = DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println("Connection is successful!"+connection);
        return connection;
    }

    public List<Contact> readData() {
        String sql = "SELECT * FROM address_book";
        return this.getAddressBookDataUsingDB(sql);
    }

    private List<Contact> getAddressBookDataUsingDB(String sql) {
        List<Contact> contactList = new ArrayList<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            contactList = this.getAddressBookData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    private List<Contact> getAddressBookData(ResultSet resultSet) {
        List<Contact> contactList = new ArrayList<>();
        try {
            while (resultSet.next()){
                String first = resultSet.getString("first_name");
                String last = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                int zip = resultSet.getInt("zip");
                int phone = resultSet.getInt("phone_number");
                String email = resultSet.getString("email");
                contactList.add(new Contact(first,last,address,city,state,zip,phone,email));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return contactList;
    }
}
