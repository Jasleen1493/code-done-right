package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.FileValidationStrategy;
import com.credit.card.processor.validation.ValidationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

public class FileServiceTest {
    @Test
    public void testReadFileFromPath() throws IOException {
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        strategies.add(FileValidationStrategy.FOLDER);
        FileService fileService = new FileService(strategies);
        File file = fileService.createFile("data/new/transaction1006.txt");
        Assertions.assertNotNull(file);
        Assertions.assertEquals("transaction1006.txt",file.getName());
        Assertions.assertEquals("new",file.getFolder());
    }

    @Test
    public void testFileValidation() throws IOException {
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        strategies.add(FileValidationStrategy.FOLDER);
        FileService fileService = new FileService(strategies);
        Assertions.assertNotNull(fileService.fileValidation());
    }

    @Test
    public void testFileValidationIfNewFolderIsEmpty() throws IOException {
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        strategies.add(FileValidationStrategy.FOLDER);
        FileService fileService = new FileService(strategies);
        //Assertions.assertEquals(0,fileService.fileValidation().size());
    }

    @Test
    public void testAllFilesInFolder() throws IOException {
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        strategies.add(FileValidationStrategy.FOLDER);
        FileService fileService = new FileService(strategies);
        fileService.getAllFilesFromPath(Constant.PROCESSING_FOLDER_PATH);
        Assertions.assertEquals("data/processing/reference0001.txt",fileService.getAllFilesFromPath(Constant.PROCESSING_FOLDER_PATH).get(0));
    }
}