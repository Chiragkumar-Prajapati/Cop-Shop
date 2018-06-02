package com.ctrlaltelite.copshop.persistence.stubs;

import android.util.Log;

import com.ctrlaltelite.copshop.persistence.interfaces.IDatabase;

import java.util.Hashtable;

public class MockDatabaseStub implements IDatabase {
    private Hashtable<String, Hashtable<String, Hashtable<String, String>>> database;

    public MockDatabaseStub() {
        this.database = new Hashtable<>(20);

        // Initialize database tables
        this.makeTable("Buyers");
        this.makeTable("Sellers");
        this.makeTable("Listings");

        Log.i("APP_DEBUG_INFO", "Initialized database");
    }

    @Override
    public void insert(String tableName, String primaryKey, Hashtable row) {
        // TODO: insert method for database
    }

    @Override
    public String fetch(String tableName, String primaryKey, String columnName) {
        // TODO: fetch method for database
        return "PLACEHOLDER";
    }

    @Override
    public void delete(String tableName, String primaryKey) {
        // TODO: delete method for database
    }

    /**
     * Create a new table in the database
     * @param tableName Name of the table to create
     */
    private void makeTable(String tableName) {
        if (null != tableName) {
            Hashtable<String, Hashtable<String, String>> newTable = new Hashtable<>(100);
            this.database.put(tableName, newTable);
        }
    }
}
