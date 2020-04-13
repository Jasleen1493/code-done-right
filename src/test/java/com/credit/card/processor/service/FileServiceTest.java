package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.impl.FileValidationImpl;
import com.credit.card.processor.validation.FileValidation;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileServiceTest {
    @Autowired
    private FileService fileService;

    @Test
    @Order(1)
    public void testFilesWithValidStatus() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("reference0001.txt","data/new/reference0001.txt","new",strategies);
        List<File> files = new ArrayList<>();
        files.add(file);
        Assertions.assertEquals(1,fileService.getFilesWithValidStatus(files).get(true).size());
    }

    @Test
    @Order(2)
    public void testFilesWithInValidStatus() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("transactions1006.txt","data/new/transactions1006.txt","new",strategies);
        List<File> files = new ArrayList<>();
        files.add(file);
        Assertions.assertEquals(1,fileService.getFilesWithValidStatus(files).get(false).size());
    }

    @Test
    @Order(3)
    public void testReadFileFromPath() throws IOException {
        File file = fileService.getFileFromFileName("data/new/transaction1006.txt");
        Assertions.assertNotNull(file);
        Assertions.assertEquals("transaction1006.txt",file.getName());
        Assertions.assertEquals("new",file.getFolder());
    }

    @Test
    @Order(4)
    public void testGetAllFiles() throws IOException {
        Assertions.assertEquals(11,fileService.getAllFiles(Constant.NEW_FOLDER_PATH).size());
    }

    @Test
    @Order(5)
    public void testGetAllFilesInSortedOrder() throws IOException {
        Assertions.assertEquals("reference0001.txt",fileService.getAllFiles(Constant.NEW_FOLDER_PATH).get(0).getName());
    }

    @Test
    @Order(6)
    public void testValidFile() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("reference0001.txt","data/new/reference0001.txt","new",strategies);
        Assertions.assertTrue(fileService.isValid(file));
    }

    @Test
    @Order(7)
    public void testInvalidFile() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("transactions1006.txt","data/new/transactions1006.txt","new",strategies);
        Assertions.assertFalse(fileService.isValid(file));
    }

    @Test
    @Order(8)
    public void testMovedFilesToGarbageFolder() throws IOException {
        List<File> files = fileService.getAllFiles(Constant.NEW_FOLDER_PATH);
        Assertions.assertEquals(11,fileService.getMovedFiles(files,Constant.PROCESSING_FOLDER_PATH,Constant.PROCESSING));
    }
}
