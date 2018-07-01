package com.ctrlaltelite.copshop.persistence.database.stubs;

import com.ctrlaltelite.copshop.persistence.database.IDatabase;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

public class MockDatabaseStub implements IDatabase {
    private Hashtable<String, Hashtable<String, Hashtable<String, String>>> database;
    private Hashtable<String, String> dbTablesHighestPrimaryKey;

    public MockDatabaseStub() {
        this.database = new Hashtable<>();
        this.dbTablesHighestPrimaryKey = new Hashtable<>();

        // Initialize database tables
        this.makeTable("Buyers");
        this.makeTable("Sellers");
        this.makeTable("Listings");
    }

    @Override
    public boolean rowExists(String tableName, String primaryKey) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }
        if (null == primaryKey) { throw new IllegalArgumentException("primaryKey cannot be null"); }

        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);

        if (null != table) {
            return table.containsKey(primaryKey);
        }

        return false;
    }

    @Override
    public String insertRow(String tableName, Hashtable<String, String> row) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }
        if (null == row) { throw new IllegalArgumentException("row HashTable cannot be null"); }

        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);
        String newPrimaryKey = getNextPrimaryKey(tableName);

        if (null != table && null != newPrimaryKey) {
            table.put(newPrimaryKey, row);
            this.dbTablesHighestPrimaryKey.put(tableName, newPrimaryKey);
        }

        return newPrimaryKey;
    }

    @Override
    public String insertRow(String tableName, String columnName, String value) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }
        if (null == columnName) { throw new IllegalArgumentException("columnName cannot be null"); }

        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);
        String newPrimaryKey = getNextPrimaryKey(tableName);

        if (null != table && null != newPrimaryKey) {
            Hashtable<String, String> row = new Hashtable<>(10);
            row.put(columnName, value);
            table.put(newPrimaryKey, row);
            this.dbTablesHighestPrimaryKey.put(tableName, newPrimaryKey);
        }

        return newPrimaryKey;
    }

    @Override
    public String updateColumn(String tableName, String primaryKey, String columnName, String value) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }
        if (null == primaryKey) { throw new IllegalArgumentException("primaryKey cannot be null"); }
        if (null == columnName) { throw new IllegalArgumentException("columnName cannot be null"); }

        String previous = null;
        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);

        if (null != table) {
            Hashtable<String, String> row = table.get(primaryKey);
            if (null != row) {
                previous = row.put(columnName, value);
            }
        }

        return previous;
    }

    @Override
    public boolean mergeColumns(String tableName, String primaryKey, Hashtable<String, String> mergeRow) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }
        if (null == primaryKey) { throw new IllegalArgumentException("primaryKey cannot be null"); }
        if (null == mergeRow) { throw new IllegalArgumentException("row HashTable to merge cannot be null"); }

        boolean result = false;
        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);

        if (null != table) {
            Hashtable<String, String> row = table.get(primaryKey);
            if (null != row) {
                row.putAll(mergeRow);
                result = true;
            }
        }

        return result;
    }

    @Override
    public String fetchColumn(String tableName, String primaryKey, String columnName) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }
        if (null == primaryKey) { throw new IllegalArgumentException("primaryKey cannot be null"); }
        if (null == columnName) { throw new IllegalArgumentException("columnName cannot be null"); }

        String value = null;
        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);

        if (null != table) {
            Hashtable<String, String> row = table.get(primaryKey);
            if (null != row) {
                value = row.get(columnName);
            }
        }

        return value;
    }

    @Override
    public List<String> findByColumnValue(String tableName, String columnName, String searchValue) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }
        if (null == columnName) { throw new IllegalArgumentException("columnName cannot be null"); }

        List<String> found = new ArrayList<>();
        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);

        if (null != table) {
            Enumeration<String> rows = table.keys(); // Get all keys in table
            String rowPK;
            // Iterate through all keys and check the given column's value
            while (rows.hasMoreElements()) {
                rowPK = rows.nextElement();
                Hashtable<String, String> row = table.get(rowPK);

                if (null != row) {
                    String value = row.get(columnName);
                    // Compare the row's value with the search value
                    if (searchValue.equals(value)) {
                        found.add(rowPK); // Record the primary key of this row
                    }
                }
            }

        }

        return found;
    }

    @Override
    public List<Hashtable<String, String>> getAllRows(String tableName) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }

        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);
        List<Hashtable<String, String>> list = new ArrayList<>();

        int currKey = 0;
        while (currKey <= Integer.parseInt(this.dbTablesHighestPrimaryKey.get(tableName))) {
            list.add(table.get(Integer.toString(currKey)));
            ++currKey;
        }

        return list;
    }

    @Override
    public Hashtable<String, String> deleteRow(String tableName, String primaryKey) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }
        if (null == primaryKey) { throw new IllegalArgumentException("primaryKey cannot be null"); }

        Hashtable<String, Hashtable<String, String>> table = database.get(tableName);

        if (null != table) {
            return table.remove(primaryKey);
        }

        return null;
    }

    public void makeTable(String tableName) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }

        Hashtable<String, Hashtable<String, String>> newTable = new Hashtable<>(100);
        this.database.put(tableName, newTable);
        this.dbTablesHighestPrimaryKey.put(tableName, "-1");
    }

    /**
     *
     * @param tableName Name of the table to get the next primary key for
     * @return Next primary key
     */
    private String getNextPrimaryKey(String tableName) {
        if (null == tableName) { throw new IllegalArgumentException("tableName cannot be null"); }

        String highestPK = this.dbTablesHighestPrimaryKey.get(tableName);

        if (null != highestPK) {
            return Integer.toString(Integer.parseInt(highestPK) + 1);
        }

        return null;
    }
}
