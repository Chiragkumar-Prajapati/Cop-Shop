package com.ctrlaltelite.copshop.persistence.stubs.hsqldb;

import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SellerModelHSQLDB implements ISellerModel {
    private static String TABLE_NAME = "SELLERS";
    private Connection dbConn;

    public SellerModelHSQLDB(final String dbPath) {
        this.dbConn = HSQLDBUtil.getConnection(dbPath);
    }

    @Override
    public void finalize() {
        HSQLDBUtil.closeConnection(dbConn);
    }

    @Override
    public String createNew(SellerAccountObject newAccount) {
        if (null == newAccount) { throw new IllegalArgumentException("newAccount cannot be null"); }

        PreparedStatement st = null;
        ResultSet generatedKeys = null;

        try {
            st = dbConn.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " " +
                            "(name,email,address,postalcode,province,password) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS);
            st.setString(1, newAccount.getOrganizationName());
            st.setString(2, newAccount.getStreetAddress());
            st.setString(3, newAccount.getPostalCode());
            st.setString(4, newAccount.getProvince());
            st.setString(5, newAccount.getEmail());
            st.setString(6, newAccount.getPassword());
            int updated = st.executeUpdate();

            if (updated >= 1) {
                generatedKeys = st.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    return String.valueOf(newId);
                }
            }

            return null;
        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(generatedKeys);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public boolean update(String id, SellerAccountObject updatedAccount) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }
        if (null == updatedAccount) { throw new IllegalArgumentException("updatedListing cannot be null"); }

        PreparedStatement st = null;

        try {
            st = dbConn.prepareStatement(
                    "UPDATE " + TABLE_NAME + " SET " +
                        "name=?, " +
                        "address=?, " +
                        "postalcode=?, " +
                        "province=?, " +
                        "email=?, " +
                        "password=? " +
                        "WHERE id = ?");
            st.setString(1, updatedAccount.getOrganizationName());
            st.setString(2, updatedAccount.getStreetAddress());
            st.setString(3, updatedAccount.getPostalCode());
            st.setString(4, updatedAccount.getProvince());
            st.setString(5, updatedAccount.getEmail());
            st.setString(6, updatedAccount.getPassword());
            st.setInt(7, Integer.parseInt(id));
            st.executeUpdate();
            return true;

        } catch (final SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public SellerAccountObject fetch(String id) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE id=?");
            st.setInt(1, Integer.parseInt(id));
            rs = st.executeQuery();

            SellerAccountObject sellerAccount = null;
            if (rs.next()) {
                sellerAccount = fromResultSet(rs);
            }
            return sellerAccount;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public SellerAccountObject findByEmail(String email) {
        if (null == email) { throw new IllegalArgumentException("email cannot be null"); }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE email=?");
            st.setString(1, email);
            rs = st.executeQuery();

            SellerAccountObject sellerAccount = null;
            if (rs.next()) {
                sellerAccount = fromResultSet(rs);
            }
            return sellerAccount;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public boolean checkEmailPasswordMatch(String email, String inputPassword) {
        if (null == email) { throw new IllegalArgumentException("email cannot be null"); }
        if (null == inputPassword) { throw new IllegalArgumentException("inputPassword cannot be null"); }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE email=? and password=?");
            st.setString(1, email);
            st.setString(2, inputPassword);
            rs = st.executeQuery();

            if (rs.next()) {
                return true;
            }
            return false;

        } catch (final SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public String getIdFromName(String sellerName) {
        String id = "";
        boolean idFound = false;

        PreparedStatement st = null;
        ResultSet rs = null;

        // get all seller table rows
        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
            rs = st.executeQuery();

            int i = 0;
            while (!idFound && rs.next()) {
                // populate array with the locations
                if (sellerName.equalsIgnoreCase(HSQLDBUtil.getStringFromResultSet(rs, "name"))) {
                    id = HSQLDBUtil.getIntAsStringFromResultSet(rs, "id");
                    idFound = true;
                }
                i++;
            }

            return id;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public int getNumSellers() {

        int numSellers = 0;

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT COUNT (DISTINCT name) AS NumSellers FROM " + TABLE_NAME);
            rs = st.executeQuery();

            if (rs.next()) {
                numSellers = Integer.parseInt(HSQLDBUtil.getStringFromResultSet(rs, "NumSellers"));
            }
            return numSellers;

        } catch (final SQLException e) {
            e.printStackTrace();
            return -1;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public String[] getAllSellerNames() {
        String[] locations = new String[getNumSellers()+1];

        PreparedStatement st = null;
        ResultSet rs = null;

        // get all seller table rows
        try {
            st = dbConn.prepareStatement("SELECT DISTINCT name FROM " + TABLE_NAME);
            rs = st.executeQuery();

            locations[0] = "";
            int i = 1;
            while (rs.next()) {
                // populate array with the locations
                locations[i] = HSQLDBUtil.getStringFromResultSet(rs, "name");
                i++;
            }

            return locations;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    private SellerAccountObject fromResultSet(final ResultSet rs) throws SQLException {
        if (null == rs) { throw new IllegalArgumentException("resultSet cannot be null"); }

        final String name = HSQLDBUtil.getStringFromResultSet(rs,"name");
        final String address = HSQLDBUtil.getStringFromResultSet(rs,"address");
        final String postalCode = HSQLDBUtil.getStringFromResultSet(rs,"postalcode");
        final String province = HSQLDBUtil.getStringFromResultSet(rs,"province");
        final String email = HSQLDBUtil.getStringFromResultSet(rs,"email");
        final String password = HSQLDBUtil.getStringFromResultSet(rs,"password");
        final String id = HSQLDBUtil.getStringFromResultSet(rs,"id");

        return new SellerAccountObject(id, name, address, postalCode, province, email, password);
    }
}
