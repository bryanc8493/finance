package persistence.address;

import beans.Address;
import beans.UpdatedRecord;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import utilities.exceptions.AppException;

import java.sql.*;
import java.util.List;

public class AddressData {

    private static final Logger logger = Logger.getLogger(AddressData.class);

    public static Object[][] getAddresses() {
        logger.debug("Getting addresses...");

        Object[][] records = new Object[getAddressCount()][7];
        String SQL_TEXT = "SELECT * FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                + Tables.ADDRESSES + " ORDER BY LAST_NAME ASC";
        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            int recordCount = 0;
            while (rs.next()) {
                for(int i=0; i<7; i++) {
                    records[recordCount][i] = rs.getString(i+1);
                }
                recordCount++;
            }
        } catch (SQLException e1) {
            throw new AppException(e1);
        }

        return records;
    }

    private static int getAddressCount() {
        final Connection con = Connect.getConnection();
        String SQL_TEXT = "SELECT COUNT(*) FROM " +
                Databases.ACCOUNTS + ApplicationLiterals.DOT + Tables.ADDRESSES;
        Statement statement;
        ResultSet rs;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static int newAddress(Address address) {
        logger.debug("Adding new address");
        final Connection con = Connect.getConnection();
        String lastName = address.getLastName();
        String firstName = address.getFirstName();
        String addr = address.getAddress();
        String city = address.getCity();
        String state = address.getState();
        String zip = address.getZipcode();

        String SQL_TEXT = "INSERT INTO " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                + Tables.ADDRESSES + " (LAST_NAME, FIRST_NAMES, ADDRESS, CITY, STATE, ZIP) "
                + "VALUES('"
                + lastName
                + "', '"
                + firstName
                + "', '"
                + addr
                + "', '" + city + "', '" + state + "', '" + zip + "')";
        PreparedStatement ps;

        try {
            ps = con.prepareStatement(SQL_TEXT);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static Address getSpecifiedAddress(String lastName, String firstName) {
        logger.debug("Getting Address record for " + lastName + " - "
                + firstName + "...");
        final Connection con = Connect.getConnection();
        String SQL_TEXT = "SELECT LAST_NAME, FIRST_NAMES, ADDRESS, CITY, STATE, ZIP "
                + "FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                + Tables.ADDRESSES + " WHERE LAST_NAME = '"
                + lastName
                + "' " + "AND FIRST_NAMES = '" + firstName + "'";
        Statement statement;
        ResultSet rs;
        Address address = new Address();

        try {
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);
            while (rs.next()) {
                address.setLastName(rs.getString(1));
                address.setFirstName(rs.getString(2));
                address.setAddress(rs.getString(3));
                address.setCity(rs.getString(4));
                address.setState(rs.getString(5));
                address.setZipcode(rs.getString(6));
            }
            con.close();
        } catch (Exception e) {
            throw new AppException(e);
        }
        return address;
    }

    public static void changeAddresses(List<UpdatedRecord> updates) {
        logger.debug("Updating address changes..");
        final Connection con = Connect.getConnection();
        for (UpdatedRecord a : updates) {
            String query = "UPDATE " + Databases.ACCOUNTS
                    + ApplicationLiterals.DOT + Tables.ADDRESSES
                    + " set attr = ? where ID = ?";
            query = query.replace("attr", a.getAttribute());
            try {
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.setString(1, a.getData());
                preparedStmt.setString(2, a.getID());
                preparedStmt.executeUpdate();
            } catch (SQLException e) {
                throw new AppException(e);
            }
        }
    }

    public static void deleteAddress(String ID) {
        logger.debug("Deleting Address...");
        final Connection con = Connect.getConnection();
        String query = "DELETE from " + Databases.ACCOUNTS + ApplicationLiterals.DOT +
                Tables.ADDRESSES + " WHERE ID = " + ID;
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}
