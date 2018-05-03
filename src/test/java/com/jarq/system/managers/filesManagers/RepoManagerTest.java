package com.jarq.system.managers.filesManagers;

import com.jarq.AbstractTest;
import com.jarq.system.enums.RepositoriesPath;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.deleteIfExists;
import static org.junit.Assert.*;

public class RepoManagerTest extends AbstractTest {

    private IRepositoryManager pathManager;

    @Before
    public void setup() {
//        pathManager = RepositoryManager.getInstance();
    }

    @Test
    public void getInstance() {
        assertNotNull(pathManager);
        assertTrue(pathManager instanceof RepositoryManager);
    }

    @Test
    public void inspect() {
        String notExistingPath = "/1/1/1/nothing.md";
        String existingPath = RepositoriesPath.MANAGER_PATH_HAS_FILE_TEST.getPath();

        assertFalse(pathManager.hasFile(notExistingPath));
        assertTrue(pathManager.hasFile(existingPath));
    }

    @Test
    public void create() throws IOException {

        String path = RepositoriesPath.MANAGER_PATH_CREATION_TEST.getPath();

//        boolean isCreatedFirst = pathManager.create(path); // should be false

        removePath(path);
//
//        boolean isCreatedSecond = pathManager.create(path);  // should be true;
//
//        assertTrue(! isCreatedFirst && isCreatedSecond);

    }

    @Test
    public void demolish() {
    }


    private void removePath(String filepath) throws IOException {
        Path path = Paths.get(filepath);
        deleteIfExists(path);
    }


}