package com.jarq.system.dao;

import com.jarq.system.exceptions.DaoFailure;
import com.jarq.system.managers.databaseManagers.JDBCProcessManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SqlDao implements Dao {

    private Connection connection;
    private JDBCProcessManager processManager;

    public SqlDao(Connection connection, JDBCProcessManager processManager) {
        this.connection = connection;
        this.processManager = processManager;
    }

    protected Connection getConnection() {
        return connection;
    }

    protected JDBCProcessManager getProcessManager() {
        return processManager;
    }

    protected int[] gatherIdFromNestedList(List<String[]> nestedList)
            throws DaoFailure {
        int ID_INDEX = 0;
        int[] ids = new int[nestedList.size()];
        int counter = 0;
        try {
            for(String[] table : nestedList) {
                ids[counter] = Integer.parseInt(table[ID_INDEX]);
                counter++;
            }
            return ids;
        } catch (Exception ex) {
            throw new DaoFailure(ex.getMessage());
        }
    }

    protected int getLowestFreeIdFromGivenTable(String databaseTable) throws DaoFailure {
        // to avoid relying on database autoincrement mechanic
        final String ID_LABEL = "id";
        final int MINIMUM_ID_VALUE = 1;
        String EXCEPTION_INFO = "Couldn't gather lowest free id, exception occurred. ";

        String query = String.format("SELECT %s FROM %s", ID_LABEL, databaseTable);

        try ( PreparedStatement preparedStatement = connection.prepareStatement(query);
              ResultSet resultSet = preparedStatement.executeQuery() ) {

            List<Integer> idCollection = new ArrayList<>();
            while( resultSet.next() ) {
                idCollection.add(resultSet.getInt(ID_LABEL));
            }

            int idCollectionLength = idCollection.size();

            if(idCollectionLength == 0) {
                return MINIMUM_ID_VALUE;
            }

            Collections.sort(idCollection);

            for(int i=MINIMUM_ID_VALUE; i<=idCollectionLength+1; i++) {
                if(! idCollection.contains(i) ) {
                    return i;  // return first not occupied lowest value
                }
            }
            throw new DaoFailure(EXCEPTION_INFO);
        } catch (SQLException ex) {
            throw new DaoFailure(EXCEPTION_INFO + ex.getMessage());
        }
    }
}
