package com.ctrlaltelite.copshop.persistence.models;

import com.ctrlaltelite.copshop.logic.interfaces.ISellerModel;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.interfaces.IDatabase;

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

        newRow.put("username", newAccount.getUsername());
        newRow.put("password", newAccount.getPassword());
        newRow.put("email", newAccount.getEmail());

        return this.database.insertRow(TABLE_NAME, newRow);
    }

    @Override
    public boolean update(String id, SellerAccountObject updatedAccount) {
        boolean success = true;
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "username", updatedAccount.getUsername()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "password", updatedAccount.getPassword()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "email", updatedAccount.getEmail()));
        return success;
    }

    @Override
    public SellerAccountObject fetch(String id) {
        return new SellerAccountObject(
                id,
                this.database.fetchColumn(TABLE_NAME, id, "username"),
                this.database.fetchColumn(TABLE_NAME, id, "password"),
                this.database.fetchColumn(TABLE_NAME, id, "email")
        );
    }

    @Override
    public SellerAccountObject findByUsername(String username) {
        ArrayList<String> users = this.database.findByColumnValue(TABLE_NAME, "username", username);

        // We should only get back one user max, ignore extras
        if (!users.isEmpty()) {
            String id = users.get(0);
            return this.fetch(id);
        }

        return null;
    }

    @Override
    public boolean checkUsernamePasswordMatch(String username, String inputPassword) {
        ArrayList<String> users = this.database.findByColumnValue(TABLE_NAME, "username", username);

        // We should only get back one user max, ignore extras
        if (!users.isEmpty()) {
            String id = users.get(0);
            String password = this.database.fetchColumn(TABLE_NAME, id, "password");
            return password.equals(inputPassword);
        }

        return false;
    }
}
