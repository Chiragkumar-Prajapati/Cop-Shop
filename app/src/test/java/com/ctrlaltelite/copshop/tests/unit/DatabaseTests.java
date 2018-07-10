package com.ctrlaltelite.copshop.tests.unit;

import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import org.junit.Test;
import java.util.List;
import java.util.Hashtable;
import static org.junit.Assert.*;

public class DatabaseTests {

    @Test
    public void rowExists_findsRow() {
        IDatabase database = new MockDatabaseStub();

        // Setup table
        database.makeTable("TestTable");

        // Insert rows and get PKs
        String PK1 = database.insertRow("TestTable", "TestColumn", "Test1");
        String PK2 = database.insertRow("TestTable", "TestColumn", "Test2");
        String PK3 = database.insertRow("TestTable", "TestColumn", "Test3");

        // Test rows exist
        assertTrue("Row was not found", database.rowExists("TestTable", PK1));
        assertTrue("Row was not found", database.rowExists("TestTable", PK2));
        assertTrue("Row was not found", database.rowExists("TestTable", PK3));

        // Test rows that don't exist

        assertFalse("Nonexistent row was found", database.rowExists("TestTable", "5"));
        assertFalse("Nonexistent row was found", database.rowExists("TestTable", "4"));
    }

    @Test
    public void insertRow_returnsSequentialKeys() {
        IDatabase database = new MockDatabaseStub();

        // Setup row and table
        Hashtable<String, String> row = new Hashtable<>();
        row.put("TestColumn", "TestValue");
        database.makeTable("TestTable");

        // Insert rows and test returned PKs
        assertEquals("Returned PK from insert not expected", database.insertRow("TestTable", row), "0" );
        assertEquals("Returned PK from insert not expected", database.insertRow("TestTable", row), "1" );
        assertEquals("Returned PK from insert not expected", database.insertRow("TestTable", row), "2" );
        assertEquals("Returned PK from insert not expected", database.insertRow("TestTable", row), "3" );
        assertEquals("Returned PK from insert not expected", database.insertRow("TestTable", row), "4" );
    }

    @Test
    public void insertRow_returnsSequentialKeys_withDeletedRows() {
        IDatabase database = new MockDatabaseStub();

        // Setup rows and table
        Hashtable<String, String> row1 = new Hashtable<>();
        row1.put("TestColumn", "Test1");
        Hashtable<String, String> row2 = new Hashtable<>();
        row2.put("TestColumn", "Test2");
        Hashtable<String, String> row3 = new Hashtable<>();
        row3.put("TestColumn", "Test3");
        Hashtable<String, String> row4 = new Hashtable<>();
        row4.put("TestColumn", "Test4");
        database.makeTable("TestTable");

        // Insert rows and get PKs
        String PK1 = database.insertRow("TestTable", row1);
        String PK2 = database.insertRow("TestTable", row2);
        String PK3 = database.insertRow("TestTable", row3);

        // Verify PKs are sequential
        assertEquals("PK is not expected value", "0", PK1);
        assertEquals("PK is not expected value", "1", PK2);
        assertEquals("PK is not expected value", "2", PK3);

        // Delete Rows
        assertEquals("Did not get back correct previous row", row3, database.deleteRow("TestTable", PK3));
        assertEquals("Did not get back correct previous row", row2, database.deleteRow("TestTable", PK2));

        // Insert another row and verify sequential PK
        String PK4 = database.insertRow("TestTable", row4);
        assertEquals("PK is not expected value", "3", PK4);
    }

    @Test
    public void insertRow_createsNewRows() {
        IDatabase database = new MockDatabaseStub();

        // Setup table
        database.makeTable("TestTable");

        // Insert rows and get PKs
        String PK1 = database.insertRow("TestTable", "TestColumn", "Test1");
        String PK2 = database.insertRow("TestTable", "TestColumn", "Test2");
        String PK3 = database.insertRow("TestTable", "TestColumn", "Test3");

        // Verify PKs are sequential
        assertEquals("PK is not expected value", "0", PK1);
        assertEquals("PK is not expected value", "1", PK2);
        assertEquals("PK is not expected value", "2", PK3);

        // Test rows exist
        assertEquals("Row was not created", database.fetchColumn("TestTable", PK1,"TestColumn"), "Test1");
        assertEquals("Row was not created", database.fetchColumn("TestTable", PK2,"TestColumn"), "Test2");
        assertEquals("Row was not created", database.fetchColumn("TestTable", PK3,"TestColumn"), "Test3");
    }

    @Test
    public void updateColumn_updatesColumn() {
        IDatabase database = new MockDatabaseStub();

        // Setup row and table
        Hashtable<String, String> row = new Hashtable<>();
        row.put("TestColumn", "TestValue");
        database.makeTable("TestTable");

        // Insert row and get PK
        String PK = database.insertRow("TestTable", row);

        // Update row at PK and test value
        assertEquals("Did not get back correct previous value", "TestValue", database.updateColumn("TestTable", PK,"TestColumn", "UpdatedTestValue"));
        assertEquals("Column did not update as expected", database.fetchColumn("TestTable", PK,"TestColumn"), "UpdatedTestValue");
    }

    @Test
    public void updateRow_updatesRow() {
        IDatabase database = new MockDatabaseStub();

        // Setup rows and table
        Hashtable<String, String> initRow = new Hashtable<>();
        initRow.put("TestColumn", "InitTestValue");
        Hashtable<String, String> updatedRow = new Hashtable<>();
        updatedRow.put("TestColumn", "UpdatedTestValue");
        database.makeTable("TestTable");

        // Insert row and get PK
        String PK = database.insertRow("TestTable", initRow);

        // Update row at PK and test value
        assertTrue("Did not get success boolean", database.mergeColumns("TestTable", PK, updatedRow));
        assertEquals("Row did not update as expected", database.fetchColumn("TestTable", PK,"TestColumn"), "UpdatedTestValue");
    }

    @Test
    public void fetchColumn_fetchesCorrectColumn() {
        IDatabase database = new MockDatabaseStub();

        // Setup rows and table
        Hashtable<String, String> row1 = new Hashtable<>();
        row1.put("TestColumn", "Test1");
        Hashtable<String, String> row2 = new Hashtable<>();
        row2.put("TestColumn", "Test2");
        Hashtable<String, String> row3 = new Hashtable<>();
        row3.put("TestColumn", "Test3");
        database.makeTable("TestTable");

        // Insert rows and get PKs
        String PK1 = database.insertRow("TestTable", row1);
        String PK2 = database.insertRow("TestTable", row2);
        String PK3 = database.insertRow("TestTable", row3);

        // Test fetched values
        assertEquals("Did not fetch correct value", database.fetchColumn("TestTable", PK2,"TestColumn"), "Test2");
        assertEquals("Did not fetch correct value", database.fetchColumn("TestTable", PK1,"TestColumn"), "Test1");
        assertEquals("Did not fetch correct value", database.fetchColumn("TestTable", PK3,"TestColumn"), "Test3");
    }

    @Test
    public void findByColumnValue_findsCorrectRows() {
        IDatabase database = new MockDatabaseStub();

        // Setup rows and table
        Hashtable<String, String> row1 = new Hashtable<>();
        row1.put("TestA", "1");
        row1.put("TestB", "Alpha");
        Hashtable<String, String> row2 = new Hashtable<>();
        row2.put("TestA", "2");
        row2.put("TestB", "Beta");
        Hashtable<String, String> row3 = new Hashtable<>();
        row3.put("TestA", "3");
        row3.put("TestB", "Alpha");
        database.makeTable("TestTable");

        // Insert rows and get PKs
        String PK1 = database.insertRow("TestTable", row1);
        String PK2 = database.insertRow("TestTable", row2);
        String PK3 = database.insertRow("TestTable", row3);

        // Get all PKs that have Alpha in column TestB
        List results = database.findByColumnValue("TestTable", "TestB", "Alpha");

        // Verify the right ones have been found
        assertTrue("Did not find correct PK", results.contains(PK1));
        assertTrue("Did not find correct PK", results.contains(PK3));
        assertFalse("Found incorrect PK", results.contains(PK2));
    }

    @Test
    public void deleteRow_deletesRow() {
        IDatabase database = new MockDatabaseStub();

        // Setup rows and table
        Hashtable<String, String> row1 = new Hashtable<>();
        row1.put("TestColumn", "Test1");
        Hashtable<String, String> row2 = new Hashtable<>();
        row2.put("TestColumn", "Test2");
        Hashtable<String, String> row3 = new Hashtable<>();
        row3.put("TestColumn", "Test3");
        database.makeTable("TestTable");

        // Insert rows and get PKs
        String PK1 = database.insertRow("TestTable", row1);
        String PK2 = database.insertRow("TestTable", row2);
        String PK3 = database.insertRow("TestTable", row3);

        // Delete a row and test that it can no longer be fetched
        assertEquals("Did not get back correct previous row", row2, database.deleteRow("TestTable", PK2));
        assertEquals("Fetched deleted row", database.fetchColumn("TestTable", PK2,"TestColumn"), null);

        // Verify other rows can still be fetched
        assertEquals("Could not fetch non-deleted row", database.fetchColumn("TestTable", PK1,"TestColumn"), "Test1");
        assertEquals("Could not fetch non-deleted row", database.fetchColumn("TestTable", PK3,"TestColumn"), "Test3");
    }

    @Test
    public void getAllRows_getsAllRows() {
        IDatabase database = new MockDatabaseStub();

        // Setup rows and table
        Hashtable<String, String> row1 = new Hashtable<>();
        row1.put("TestColumn", "Test1");
        Hashtable<String, String> row2 = new Hashtable<>();
        row2.put("TestColumn", "Test2");
        Hashtable<String, String> row3 = new Hashtable<>();
        row3.put("TestColumn", "Test3");
        database.makeTable("TestTable");

        // Insert rows and get PKs
        String PK1 = database.insertRow("TestTable", row1);
        String PK2 = database.insertRow("TestTable", row2);
        String PK3 = database.insertRow("TestTable", row3);

        List<Hashtable<String, String>> list = database.getAllRows("TestTable");
        assertEquals("Fetched too many rows", 3, list.size());
        assertEquals("Wrong row fetched", "Test1", list.get(0).get("TestColumn"));
        assertEquals("Wrong row fetched", "Test2", list.get(1).get("TestColumn"));
        assertEquals("Wrong row fetched", "Test3", list.get(2).get("TestColumn"));

    }


}
