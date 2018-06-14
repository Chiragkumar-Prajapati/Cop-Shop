package com.ctrlaltelite.copshop.persistence.stubs;

import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;

import java.util.ArrayList;
import java.util.Hashtable;

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

        return this.database.insertRow(TABLE_NAME, newRow);
    }

    @Override
    public boolean update(String id, SellerAccountObject updatedAccount) {
        boolean success;
        success = (null != this.database.updateColumn(TABLE_NAME, id, "password", updatedAccount.getPassword()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "email", updatedAccount.getEmail()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "name", updatedAccount.getOrganizationName()));
        return success;
    }

    @Override
    public SellerAccountObject fetch(String id) {
        return new SellerAccountObject(
                id,
                this.database.fetchColumn(TABLE_NAME, id, "password"),
                this.database.fetchColumn(TABLE_NAME, id, "email"),
                this.database.fetchColumn(TABLE_NAME, id, "name")
        );
    }

    @Override
    public SellerAccountObject findByEmail(String email) {
        ArrayList<String> users = this.database.findByColumnValue(TABLE_NAME, "email", email);

        // We should only get back one user max, ignore extras
        if (!users.isEmpty()) {
            String id = users.get(0);
            return this.fetch(id);
        }

        return null;
    }

    @Override
    public boolean checkEmailPasswordMatch(String email, String inputPassword) {
        ArrayList<String> users = this.database.findByColumnValue(TABLE_NAME, "email", email);

        // We should only get back one user max, ignore extras
        if (!users.isEmpty()) {
            String id = users.get(0);
            String password = this.database.fetchColumn(TABLE_NAME, id, "password");
            return password.equals(inputPassword);
        }

        return false;
    }
}
