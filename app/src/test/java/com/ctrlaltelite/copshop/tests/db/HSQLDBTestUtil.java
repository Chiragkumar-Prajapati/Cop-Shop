package com.ctrlaltelite.copshop.tests.db;

import com.ctrlaltelite.copshop.persistence.hsqldb.HSQLDBUtil;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HSQLDBTestUtil {
    private static final String DB_NAME = "testdb";
    private static final String DIR = "./src/test/java/com/ctrlaltelite/copshop/tests/db";
    private static final String TEST_DB_INIT_SCRIPT_PATH = DIR + "/testinit.script";
    private static final String TEST_DB_DIR_PATH = DIR + "/testdb";
    private static final String TEST_DB_PATH = DIR + "/testdb/" + DB_NAME;
    private static final File TEST_DB_INIT_SCRIPT = new File(TEST_DB_INIT_SCRIPT_PATH);
    private Connection dbConn;

    public HSQLDBTestUtil() {
        setup();
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not set database driver: " + e);
        }
        dbConn = HSQLDBUtil.getConnection(TEST_DB_PATH);
    }

    public void finalize() {
        HSQLDBUtil.closeConnection(dbConn);
    }

    public void reset() {
        PreparedStatement st = null;
        try {
            st = dbConn.prepareStatement("DELETE FROM BIDS");
            st.executeUpdate();
            HSQLDBUtil.quietlyClose(st);
            st = dbConn.prepareStatement("DELETE FROM LISTINGS");
            st.executeUpdate();
            HSQLDBUtil.quietlyClose(st);
            st = dbConn.prepareStatement("DELETE FROM SELLERS");
            st.executeUpdate();
            HSQLDBUtil.quietlyClose(st);
            st = dbConn.prepareStatement("DELETE FROM BUYERS");
            st.executeUpdate();
            HSQLDBUtil.quietlyClose(st);
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            HSQLDBUtil.quietlyClose(st);
        }

        try {
            dbConn.commit();
        } catch (SQLException e) {
            System.out.println("Failed to commit reset to db " + e);
        }
    }

    public void setup() {
        try {
            final File target = new File(TEST_DB_DIR_PATH + "/testdb.script");
            Files.copy(TEST_DB_INIT_SCRIPT, target);
        } catch (final IOException ioe) {
            System.out.println("Unable to copy test setup script: " + ioe.getMessage());
        }
    }

    public String getTestDbPath() {
        return TEST_DB_PATH;
    }
}
