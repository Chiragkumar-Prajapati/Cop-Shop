package com.ctrlaltelite.copshop.persistence.interfaces;

import java.util.Hashtable;

public interface IDatabase {

    /**
     * Inserts or updates a row in the table at the given primary key, overwriting the key-value
     * pairs provided in 'row'
     * @param tableName Name of table
     * @param primaryKey Primary key of row to add to table or modify in table
     * @param row Hash table to initialize row with
     */
    void insert(String tableName, String primaryKey, Hashtable row);

    /**
     * Returns string value associated with table, at primary key, for the given column
     * @param tableName Name of table
     * @param primaryKey Primary key of row to fetch from
     * @param columnName Column name to fetch value from
     * @return Value contained in row's column
     */
    String fetch(String tableName, String primaryKey, String columnName);

    /**
     * Removes the row with the given primaryKey from the table
     * @param tableName Name of table
     * @param primaryKey Primary key of row to delete
     */
    void delete(String tableName, String primaryKey);
}
