package com.ctrlaltelite.copshop.persistence.database.interfaces;

import java.util.ArrayList;
import java.util.Hashtable;

public interface IDatabase {

    /**
     * Returns whether the row at the primary key exists
     * @param tableName Name of table
     * @param primaryKey Primary key of row to check
     * @return Boolean indicating existence of row
     */
    boolean rowExists(String tableName, String primaryKey);

    /**
     * Inserts a new row in the table with an incremented primary key
     * @param tableName Name of table
     * @param row Hash table to initialize row with
     * @return The primary key of the new inserted row
     */
    String insertRow(String tableName, Hashtable<String, String> row);

    /**
     * Inserts a new row in the table with an incremented primary key
     * Creates the row Hashtable
     * @param tableName Name of table
     * @param columnName Column name to initialize
     * @param value Value to initialize column with
     * @return The primary key of the new inserted row
     */
    String insertRow(String tableName, String columnName, String value);

    /**
     * Updates a column's value in a row in the given table, overwriting it
     * pairs provided in 'row'
     * @param tableName Name of table
     * @param primaryKey Primary key of row to update
     * @param columnName Column name to update
     * @param value The value to update the column to
     * @return String with the previous value, or null if there was no value
     */
    String updateColumn(String tableName, String primaryKey, String columnName, String value);

    /**
     * Merges columns from the provided row into the row at primaryKey
     * @param tableName Name of table
     * @param primaryKey Primary key of row to update
     * @param row Hash table to merge with row
     * @return Boolean indicating success
     */
    boolean mergeColumns(String tableName, String primaryKey, Hashtable<String, String> row);

    /**
     * Returns string value associated with table, at primary key, for the given column
     * @param tableName Name of table
     * @param primaryKey Primary key of row to fetch from
     * @param columnName Column name to fetch value from
     * @return Value contained in row's column, or null
     */
    String fetchColumn(String tableName, String primaryKey, String columnName);

    /**
     *
     * @param tableName Name of table
     * @param columnName Column name to search under
     * @param searchValue Value to search for in column
     * @return An ArrayList of all primary keys with the given column value
     */
    ArrayList<String> findByColumnValue(String tableName, String columnName, String searchValue);

    /**
     * Get an ArrayList of every row in the table
     * @param tableName The name of the table
     * @return ArrayList containing all rows in table
     */
    ArrayList<Hashtable<String, String>> getAllRows(String tableName);

    /**
     * Removes the row with the given primaryKey from the table
     * @param tableName Name of table
     * @param primaryKey Primary key of row to delete
     * @return The deleted row, or null
     */
    Hashtable<String, String> deleteRow(String tableName, String primaryKey);

    /**
     * Create a new table in the database
     * @param tableName Name of the table to create
     */
    void makeTable(String tableName);
}
