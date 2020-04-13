package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.impl.FileValidationImpl;
import com.credit.card.processor.validation.FileValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FileServiceTest {
    @Test
    public void testReadFileFromPath() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        FileService fileService = new FileService(strategies);
        File file = fileService.getFileFromFileName("data/new/transaction1006.txt");
        Assertions.assertNotNull(file);
        Assertions.assertEquals("transaction1006.txt",file.getName());
        Assertions.assertEquals("new",file.getFolder());
    }

    @Test
    public void testGetAllFiles() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        FileService fileService = new FileService(strategies);
        Assertions.assertEquals(11,fileService.getAllFiles(Constant.PROCESSING_FOLDER_PATH).size());
    }

    @Test
    public void testGetAllFilesInSortedOrder() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        FileService fileService = new FileService(strategies);
        Assertions.assertEquals("reference0001.txt",fileService.getAllFiles(Constant.PROCESSING_FOLDER_PATH).get(0).getName());
    }

    @Test
    public void testFilesWithValidStatus() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        FileService fileService = new FileService(strategies);
        List<File> files = fileService.getAllFiles(Constant.NEW_FOLDER_PATH);
        Assertions.assertEquals(1,fileService.getFilesWithValidStatus(files).get(true).size());
    }

    @Test
    public void testFilesWithInValidStatus() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        FileService fileService = new FileService(strategies);
        List<File> files = fileService.getAllFiles(Constant.NEW_FOLDER_PATH);
        Assertions.assertEquals(10,fileService.getFilesWithValidStatus(files).get(false).size());
    }

    @Test
    public void testValidFile() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        FileService fileService = new FileService(strategies);
        File file = new File("reference0001.txt","data/new/reference0001.txt","new");
        Assertions.assertTrue(fileService.isValid(file));
    }

    @Test
    public void testInvalidFile() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        FileService fileService = new FileService(strategies);
        File file = new File("transactions1006.txt","data/new/transactions1006.txt","new");
        Assertions.assertFalse(fileService.isValid(file));
    }

    @Test
    public void testMovedFilesToGarbageFolder() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        FileService fileService = new FileService(strategies);
        List<File> files = fileService.getAllFiles(Constant.NEW_FOLDER_PATH);
        Assertions.assertEquals(11,fileService.getMovedFiles(files,Constant.GARBAGE_FOLDER_PATH,Constant.GARBAGE));
    }
}
