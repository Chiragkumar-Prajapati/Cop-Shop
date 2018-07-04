package com.ctrlaltelite.copshop.persistence.stubs;

import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;

import java.util.Hashtable;
import java.util.List;

public class SellerModel implements ISellerModel {
    private static String TABLE_NAME = "Sellers";
    private IDatabase database;

    public SellerModel(IDatabase database) {
        this.database = database;

        // Initialize mock data
    }

    @Override
    public String createNew(SellerAccountObject newAccount) {
        Hashtable<String, String> newRow = new Hashtable<>();

        newRow.put("password", newAccount.getPassword());
        newRow.put("email", newAccount.getEmail());
        newRow.put("name", newAccount.getOrganizationName());
        newRow.put("streetAddress", newAccount.getStreetAddress());
        newRow.put("postalCode", newAccount.getPostalCode());
        newRow.put("province", newAccount.getProvince());


        return this.database.insertRow(TABLE_NAME, newRow);
    }

    @Override
    public boolean update(String id, SellerAccountObject updatedAccount) {
        boolean success;

        success = (null != this.database.updateColumn(TABLE_NAME, id, "password", updatedAccount.getPassword()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "email", updatedAccount.getEmail()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "name", updatedAccount.getOrganizationName()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "streetAddress", updatedAccount.getStreetAddress()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "postalCode", updatedAccount.getPostalCode()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "province", updatedAccount.getProvince()));

        return success;
    }

    @Override
    public SellerAccountObject fetch(String id) {
        return new SellerAccountObject(
                id,
                this.database.fetchColumn(TABLE_NAME, id, "name"),
                this.database.fetchColumn(TABLE_NAME, id, "streetAddress"),
                this.database.fetchColumn(TABLE_NAME, id, "postalCode"),
                this.database.fetchColumn(TABLE_NAME, id, "province"),
                this.database.fetchColumn(TABLE_NAME, id, "email"),
                this.database.fetchColumn(TABLE_NAME, id, "password")
        );
    }

    @Override
    public SellerAccountObject findByEmail(String email) {
        List<String> users = this.database.findByColumnValue(TABLE_NAME, "email", email);

        // We should only get back one user max, ignore extras
        if (!users.isEmpty()) {
            String id = users.get(0);
            return this.fetch(id);
        }

        return null;
    }

    @Override
    public boolean checkEmailPasswordMatch(String email, String inputPassword) {
        List<String> users = this.database.findByColumnValue(TABLE_NAME, "email", email);

        // We should only get back one user max, ignore extras
        if (!users.isEmpty()) {
            String id = users.get(0);
            String password = this.database.fetchColumn(TABLE_NAME, id, "password");
            return password.equals(inputPassword);
        }

        return false;
    }

    @Override
    public String getIdFromName(String sellerName) {
        String id = "";
        boolean idFound = false;

        // get all seller table rows
        List<Hashtable<String, String>> allRows = this.database.getAllRows(TABLE_NAME);

        for (int i = 0; !idFound && i < allRows.size(); i++) {
            if((allRows.get(i)).get("name").compareToIgnoreCase(sellerName) == 0) {
                id += (i+1);
                idFound = true;
            }
        }

        return  id;
    }

    @Override
    public int getNumSellers() {
        List<Hashtable<String, String>> allRows = this.database.getAllRows(TABLE_NAME);
        return  allRows.size();
    }

    @Override
    public String[] getAllSellerNames() {
        String[] locations = new String[getNumSellers()+1];

        // get all seller table rows
        List<Hashtable<String, String>> allRows = this.database.getAllRows(TABLE_NAME);

        locations[0] = "";

        // populate array with the locations
        for (int i = 0; i < allRows.size(); i++) {
            locations[i+1] = (allRows.get(i)).get("name");
        }

        return locations;
    }
}