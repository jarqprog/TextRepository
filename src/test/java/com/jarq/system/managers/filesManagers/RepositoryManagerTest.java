package com.jarq.system.managers.filesManagers;

import com.jarq.system.FileRelatedTest;
import com.jarq.system.enums.RepositoriesPath;
import com.jarq.system.helpers.repositoryPath.IRepositoryPath;
import com.jarq.system.helpers.repositoryPath.RepositoryPath;
import com.jarq.system.models.content.Content;
import com.jarq.system.models.content.IContent;
import com.jarq.system.models.repository.IRepository;
import com.jarq.system.models.repository.Repository;
import com.jarq.system.models.text.IText;
import com.jarq.system.models.text.Text;
import com.jarq.system.models.user.IUser;
import com.jarq.system.models.user.User;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.deleteIfExists;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RepositoryManagerTest extends FileRelatedTest {

    private IRepositoryManager repositoryManager;
    private IContent content;

    private IRepositoryPath repositoryPath;

    @Before
    public void setUp() throws Exception {

        content = mock(Content.class);
        repositoryPath = mock(RepositoryPath.class);
        repositoryManager = RepositoryManager.getInstance(repositoryPath);
    }

    @Test
    public void getInstance() {
        assertNotNull(repositoryManager);
        assertTrue(repositoryManager instanceof RepositoryManager);
    }

    @Test
    public void hasFile_should_be_true() {

        String filepath = RepositoriesPath.MANAGER_PATH_HAS_FILE_TEST.getPath();

        when(content.getFilepath()).thenReturn(filepath);

        assertTrue(repositoryManager.hasFile(content));

    }

    @Test
    public void hasFile_should_be_false() {

        String fakePath = "123";

        when(content.getFilepath()).thenReturn(fakePath);

        assertFalse(repositoryManager.hasFile(content));
    }

    @Test
    public void hasFile1_should_be_true() {

        String filepath = RepositoriesPath.MANAGER_PATH_HAS_FILE_TEST.getPath();

        assertTrue(repositoryManager.hasFile(filepath));

    }

    @Test
    public void hasFile1_should_be_false() {

        String fakePath = RepositoriesPath.MANAGER_PATH_HAS_FILE_TEST.getPath()+"1a";

        assertFalse(repositoryManager.hasFile(fakePath));
    }

    @Test
    public void hasDir_should_be_true() {

        String dirPath = RepositoriesPath.MANAGER_PATH_HAS_DIR_TEST.getPath();
        createDirs(dirPath);

        assertTrue(repositoryManager.hasDir(dirPath));

    }

    @Test
    public void hasDir_should_be_false() {

        String fakeDirPath = RepositoriesPath.MANAGER_PATH_HAS_DIR_TEST.getPath()+"123";

        assertFalse(repositoryManager.hasDir(fakeDirPath));
    }

    @Test
    public void createFile() throws Exception {

        String filepath = RepositoriesPath.MANAGER_PATH_CREATION_TEST.getPath();
        removePath(filepath);

        when(content.getFilepath()).thenReturn(filepath);

        if(checkIfFileExists(filepath)) {
            throw new Exception("Can't continue test, test file wasn't removed!");
        }

        assertTrue(repositoryManager.createFile(content));
        assertTrue(checkIfFileExists(filepath));

    }

    @Test
    public void createUserDir() throws Exception {

        String path = RepositoriesPath.MANAGER_USER_PATH_CREATION_TEST.getPath();
        removePath(path);

        IUser user = mock(User.class);

        when(repositoryPath.userDir(user)).thenReturn(path);

        if(checkIfDirectoryExists(path)) {
            throw new Exception("Can't continue test, test dir wasn't removed!");
        }

        assertTrue(repositoryManager.createDir(user));
        assertTrue(checkIfDirectoryExists(path));
    }

    @Test
    public void createRepoDir() throws Exception {

        String path = RepositoriesPath.MANAGER_REPOSITORY_PATH_CREATION_TEST.getPath();
        removePath(path);

        IRepository repository = mock(Repository.class);

        when(repositoryPath.repositoryDir(repository)).thenReturn(path);

        if(checkIfDirectoryExists(path)) {
            throw new Exception("Can't continue test, test dir wasn't removed!");
        }

        assertTrue(repositoryManager.createDir(repository));
        assertTrue(checkIfDirectoryExists(path));
    }

    @Test
    public void createTextDir() throws Exception {

        String path = RepositoriesPath.MANAGER_TEXT_PATH_CREATION_TEST.getPath();
        removePath(path);

        IText text = mock(Text.class);

        when(repositoryPath.textDir(text)).thenReturn(path);

        if(checkIfDirectoryExists(path)) {
            throw new Exception("Can't continue test, test dir wasn't removed!");
        }

        assertTrue(repositoryManager.createDir(text));
        assertTrue(checkIfDirectoryExists(path));
    }

    @Test
    public void removeFile() throws Exception {

        String pathToRemove = RepositoriesPath.MANAGER_PATH_REMOVE_FILE_TEST.getPath();

        prepareFilepathForTest(pathToRemove);

        when(content.getFilepath()).thenReturn(pathToRemove);

        boolean isRemoved = repositoryManager.removeFile(content);
        boolean secondCheck = ! checkIfFileExists(pathToRemove);

        assertTrue(isRemoved && secondCheck);
    }

    @Test
    public void removeTextDirectory() throws Exception {

        String pathToRemove = RepositoriesPath.MANAGER_PATH_REMOVE_TEXT_DIRECTORY_TEST.getPath();

        prepareDirectoryPathForTest(pathToRemove);

        IText text = mock(Text.class);

        when(repositoryPath.textDir(text)).thenReturn(pathToRemove);

        boolean isRemoved = repositoryManager.removeTextDirectory(text);
        boolean secondCheck = ! checkIfDirectoryExists(pathToRemove);

        assertTrue(isRemoved && secondCheck);
    }

    @Test
    public void removeTextDirectory_when_dir_contains_files() throws Exception {

        String pathToRemove = RepositoriesPath.MANAGER_PATH_REMOVE_FILE_TEST.getPath();

        prepareFilepathForTest(pathToRemove);

        IText text = mock(Text.class);

        when(repositoryPath.textDir(text)).thenReturn(pathToRemove);

        assertTrue(repositoryManager
                .removeTextDirectory(text) && ! checkIfFileExists(pathToRemove));
    }


    @Test
    public void removeRepository() throws Exception {

        String pathToRemove = RepositoriesPath.MANAGER_PATH_REMOVE_REPOSITORY_TEST.getPath();

        prepareDirectoryPathForTest(pathToRemove);

        prepareFilepathForTest(pathToRemove+"1.txt");

        IRepository repository = mock(Repository.class);

        when(repositoryPath.repositoryDir(repository)).thenReturn(pathToRemove);

        boolean result = repositoryManager.removeRepository(repository);
        boolean secondCheck = ! checkIfDirectoryExists(pathToRemove);

        assertTrue(result && secondCheck);
    }

    @Test
    public void removeUserRepositories() throws Exception {

        String pathToRemove = RepositoriesPath.MANAGER_PATH_REMOVE_USER_REPOSITORIES_TEST.getPath();

        prepareDirectoryPathForTest(pathToRemove);

        IUser user = mock(User.class);

        when(repositoryPath.userDir(user)).thenReturn(pathToRemove);

        boolean result = repositoryManager.removeUserRepositories(user);
        boolean secondCheck = ! checkIfDirectoryExists(pathToRemove);

        assertTrue(result && secondCheck);
    }

    @Test(expected = IOException.class)
    public void removePath_security_check() throws Exception {

        // in case of invalid path - removing shouldn't goes behind 'border'

        String pathToRemove = RepositoriesPath.MANAGER_PATH_REMOVE_SECURITY_ALERT_TEST.getPath();

        prepareDirectoryPathForTest(pathToRemove);

        IUser user = mock(User.class);

        when(repositoryPath.userDir(user)).thenReturn(pathToRemove);

        repositoryManager.removeUserRepositories(user);  // can be called on any method, I use user

    }
}