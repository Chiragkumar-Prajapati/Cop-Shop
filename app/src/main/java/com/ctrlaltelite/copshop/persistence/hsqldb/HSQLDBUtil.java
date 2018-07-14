package com.ctrlaltelite.copshop.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HSQLDBUtil {

    public static Connection getConnection(String dbPath) {
        try {
            String props = ";shutdown=true";
            String path = "jdbc:hsqldb:file:" + dbPath + props;
            return DriverManager.getConnection(path, "SA", "");
        } catch (final SQLException e) {
            throw new PersistenceRuntimeException(e);
        }
    }

    public static void closeConnection(Connection dbConn) {
        try {
            if (null != dbConn) {
                dbConn.close();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getStringFromResultSet(ResultSet rs, String column) throws SQLException {
        String result = rs.getString(column);
        if (rs.wasNull()) {
            result = "NULL";
        }
        return result;
    }

    public static String getIntAsStringFromResultSet(ResultSet rs, String column) throws SQLException {
        return String.valueOf(rs.getInt(column));
    }

    public static void quietlyClose(AutoCloseable closeable) {
        if (null != closeable) {
            try {closeable.close();} catch(Exception e) {}
        }
    }
}
