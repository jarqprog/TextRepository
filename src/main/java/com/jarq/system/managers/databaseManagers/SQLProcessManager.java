package com.jarq.system.managers.databaseManagers;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class SQLProcessManager implements JDBCProcessManager {

    public static JDBCProcessManager getInstance() {
        return new SQLProcessManager();
    }

    private SQLProcessManager() {}

    public String[] getObjectData(PreparedStatement preparedStatement) throws SQLException {
        // every array keeps data from a single record
        String[] objectData = new String[0];
        try ( ResultSet resultSet = preparedStatement.executeQuery() ) {

            if (resultSet.isBeforeFirst()) {
                ResultSetMetaData meta = resultSet.getMetaData();
                int colCounter = meta.getColumnCount();
                while (resultSet.next()) {
                    List<String> columnList = new ArrayList<>();
                    for (int column = 1; column <= colCounter; ++column) {
                        Object value = resultSet.getObject(column);
                        columnList.add(String.valueOf(value));
                    }
                    objectData = columnList.toArray(objectData);
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            closeResources(preparedStatement);
        }

        return objectData;
    }

    public List<String[]> getObjectsDataCollection(PreparedStatement preparedStatement) throws SQLException {
        // every nested array in list keeps data from a single record
        List<String[]> objectsDataCollection = new ArrayList<>();

        try ( ResultSet resultSet = preparedStatement.executeQuery() ) {

            if (resultSet.isBeforeFirst()) {
                ResultSetMetaData meta = resultSet.getMetaData();
                int colCounter = meta.getColumnCount();
                while (resultSet.next()) {
                    List<String> columnList = new ArrayList<>();
                    for (int column = 1; column <= colCounter; ++column) {
                        Object value = resultSet.getObject(column);
                        columnList.add(String.valueOf(value));
                    }
                    String[] columnArray = new String[columnList.size()];
                    columnArray = columnList.toArray(columnArray);
                    objectsDataCollection.add(columnArray);
                }
            }
        } catch(Exception e){
            throw new SQLException(e);
        } finally {
            closeResources(preparedStatement);
        }
        return objectsDataCollection;
    }

    public boolean executeBatch(PreparedStatement preparedStatement, Connection connection) throws SQLException {
        try {
            connection.setAutoCommit(false);
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources(preparedStatement);
        }
        return true;
    }

    public boolean executeStatement(PreparedStatement preparedStatement) throws SQLException {
        try {
            return preparedStatement.executeUpdate() != 0;

        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeResources(preparedStatement);
        }
    }

    private <T extends AutoCloseable> void closeResources(T resources) {
        try {
            resources.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
