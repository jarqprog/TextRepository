package controllers;

import enums.DbTables;
import exceptions.DatabaseCreationFailure;
import factory.ModelFactoryManufacture;
import factory.IDaoFactory;
import factory.IModelFactoryManufacture;
import dao.DaoFactory;
import enums.DbDriver;
import enums.DbFilePath;
import enums.DbUrl;
import managers.databaseManagers.*;
import views.ILibraryView;
import views.LibraryView;
import views.RootView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Root {

    private final DatabaseConfig databaseConfig;
    private RootView view;
    private ILibraryController libraryController;
    private DatabaseManager databaseManager;

    private Root() {
        view = new RootView();
        databaseConfig = SQLiteConfig.createSQLiteConfiguration(
                            DbUrl.SQLITE,
                            DbDriver.SQLITE,
                            DbFilePath.SQLITE_DATABASE);
        databaseManager = createSQLiteManager();
        libraryController = createLibraryController();
    }

    public static Root getInstance() {
        return new Root();
    }

    public void runApp() {
        libraryController.runMenu();
    }

    private ILibraryController createLibraryController() {

        ILibraryView view = new LibraryView();
        IModelFactoryManufacture modelFactoryManufacture = new ModelFactoryManufacture();
        JDBCProcessManager processManager = SQLProcessManager.getInstance();
        IDaoFactory daoFactory = DaoFactory.getInstance(databaseManager, processManager);

        return LibraryController.getInstance(view, daoFactory, modelFactoryManufacture);
    }

    private DatabaseManager createSQLiteManager() {

        if (! isDatabaseValid() ) {
            try {
                executeSQLiteCreationProcess();
            } catch (DatabaseCreationFailure e) {
                e.printStackTrace();
                view.displayMessage(e.getMessage());
                view.displayMessage("Can't run application without database - closing the program..");
                System.exit(0);
            }
        }
        return SQLiteManager.getSQLiteManager(databaseConfig);
    }

    private boolean isDatabaseValid() {

        List<String> databaseTablesToCheck = Arrays
                .stream(DbTables.values())
                .map(DbTables::getTable)
                .collect(Collectors.toList());

        DatabaseValidator database = SQLiteValidator.getInstance(databaseConfig, databaseTablesToCheck);
        return database.isValid();
    }

    private void executeSQLiteCreationProcess() throws DatabaseCreationFailure {
        String userChoice = view
                .getUserInput("Database problem occurred, create new database? (type 'y' to approve) ")
                .toLowerCase();

        if (userChoice.equals("y") ) {
            SQLiteCreator.getInstance(databaseConfig, DbFilePath.DB_SETUP_SCRIPT).createDatabase();
            System.out.println("tworze baze danych");
        } else {
            throw new DatabaseCreationFailure();
        }
    }
}