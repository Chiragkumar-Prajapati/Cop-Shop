package com.ctrlaltelite.copshop.persistence.hsqldb;

import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class BuyerModelHSQLDB implements IBuyerModel {
    private static String TABLE_NAME = "BUYERS";
    private Connection dbConn;

    public BuyerModelHSQLDB(final String dbPath) {
        this.dbConn = HSQLDBUtil.getConnection(dbPath);
    }

    @Override
    public void finalize() {
        HSQLDBUtil.closeConnection(dbConn);
    }

    @Override
    public String createNew(BuyerAccountObject newAccount) {
        if (null == newAccount) { throw new IllegalArgumentException("newAccount cannot be null"); }

        PreparedStatement st = null;
        ResultSet generatedKeys = null;

        try {
            st = dbConn.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " " +
                        "(firstname,lastname,address,postalcode,province,email,password) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS);
            st.setString(1, newAccount.getFirstName());
            st.setString(2, newAccount.getLastName());
            st.setString(3, newAccount.getStreetAddress());
            st.setString(4, newAccount.getPostalCode());
            st.setString(5, newAccount.getProvince());
            st.setString(6, newAccount.getEmail());
            st.setString(7, newAccount.getPassword());
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
    public boolean update(String id, BuyerAccountObject updatedAccount) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }
        if (null == updatedAccount) { throw new IllegalArgumentException("updatedListing cannot be null"); }

        PreparedStatement st = null;

        try {
            st = dbConn.prepareStatement(
                    "UPDATE " + TABLE_NAME + " SET " +
                        "firstname=?, " +
                        "lastname=?, " +
                        "address=?, " +
                        "postalcode=?, " +
                        "province=?, " +
                        "email=?, " +
                        "password=? " +
                        "WHERE id = ?");
            st.setString(1, updatedAccount.getFirstName());
            st.setString(2, updatedAccount.getLastName());
            st.setString(3, updatedAccount.getStreetAddress());
            st.setString(4, updatedAccount.getPostalCode());
            st.setString(5, updatedAccount.getProvince());
            st.setString(6, updatedAccount.getEmail());
            st.setString(7, updatedAccount.getPassword());
            st.setInt(8, Integer.parseInt(id));
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
    public BuyerAccountObject fetch(String id) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE id=?");
            st.setInt(1, Integer.parseInt(id));
            rs = st.executeQuery();

            BuyerAccountObject buyerAccount = null;
            if (rs.next()) {
                buyerAccount = fromResultSet(rs);
            }
            return buyerAccount;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public BuyerAccountObject findByEmail(String email) {
        if (null == email) { throw new IllegalArgumentException("email cannot be null"); }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE email=?");
            st.setString(1, email);
            rs = st.executeQuery();

            BuyerAccountObject buyerAccount = null;
            if (rs.next()) {
                buyerAccount = fromResultSet(rs);
            }
            return buyerAccount;

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

            if (rs.next()) { // Got back at least one result
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

    private BuyerAccountObject fromResultSet(final ResultSet rs) throws SQLException {
        if (null == rs) { throw new IllegalArgumentException("resultSet cannot be null"); }

        final String firstName = HSQLDBUtil.getStringFromResultSet(rs,"firstname");
        final String lastName = HSQLDBUtil.getStringFromResultSet(rs,"lastname");
        final String streetAddress = HSQLDBUtil.getStringFromResultSet(rs,"address");
        final String postalCode = HSQLDBUtil.getStringFromResultSet(rs,"postalcode");
        final String province = HSQLDBUtil.getStringFromResultSet(rs,"province");
        final String email = HSQLDBUtil.getStringFromResultSet(rs,"email");
        final String password = HSQLDBUtil.getStringFromResultSet(rs,"password");
        final String id = HSQLDBUtil.getStringFromResultSet(rs,"id");

        return new BuyerAccountObject(id, firstName, lastName, streetAddress, postalCode, province, email, password);
    }
}