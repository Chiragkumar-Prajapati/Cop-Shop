package com.ctrlaltelite.copshop.db;

import com.ctrlaltelite.copshop.persistence.hsqldb.HSQLDBUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HSQLDBSystemTestUtil {
    private final String testDbDir = "./src/androidTest/java/com/ctrlaltelite/copshop/db/testdb";
    private final String testScriptPath = "./src/androidTest/java/com/ctrlaltelite/copshop/db/testinit.script";
    private final String testDbPath = testDbDir + "/db";
    private Connection dbConn;

    public HSQLDBSystemTestUtil() {
        setup();

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not set database driver: " + e);
        }

        dbConn = HSQLDBUtil.getConnection(testDbPath);
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

    public String getTestDbPath() {
        return testDbPath;
    }

    public void setup() {
        // Setup script
        try {
            char[] buffer = new char[1024];
            int count;
            File outFile = new File(testDbDir + "/db.script");
            File script = new File(testScriptPath);

            if (script.exists()) {
                InputStreamReader in = new InputStreamReader(new FileInputStream(script));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            } else {
                System.out.println("Test script does not exist.");
            }
        } catch (final IOException ioe) {
            System.out.println("Unable to copy test setup script: " + ioe.getMessage());
        }
    }
}
