package com.credit.card.processor.validation;

import com.credit.card.processor.model.File;
import com.credit.card.processor.service.FileService;
import com.credit.card.processor.validation.impl.FileValidationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FileValidationTest {
    @Test
    public void testValidatorForFileNameAndFolder() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("transaction1000.csv", "data/new/transaction1000.txt", "new");
        FileService fileService = new FileService(strategies);
        Assertions.assertTrue(fileService.isValid(file));
    }

    @Test
    public void testValidatorForNewFileFolder() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("transactions1000.csv", "data/new/transactions1000.txt", "new");
        FileService fileService = new FileService(strategies);
        Assertions.assertTrue(fileService.isValid(file));
    }

    @Test
    public void testValidatorForNewFileName() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        File file = new File("transaction1001.txt", "data/new/transactions1001.txt", "new");
        FileService fileService = new FileService(strategies);
        Assertions.assertTrue(fileService.isValid(file));
    }

    @Test
    public void testInvalidFileValidationTypeName() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("transactions1005.txt", "data/new/transactions1005.txt", "new");
        FileService fileService = new FileService(strategies);
        List<FileValidation> fileValidationTypes = fileService.getInvalidFileValidationTypes(file);
        Assertions.assertEquals(1, fileValidationTypes.size());
        Assertions.assertEquals(FileValidationImpl.NAME, fileValidationTypes.get(0));
    }
    @Test
    public void testInvalidFileValidationTypeFolder() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("reference0001.txt", "data/new/reference0001.txt", "new1");
        FileService fileService = new FileService(strategies);
        List<FileValidation> fileValidationTypes = fileService.getInvalidFileValidationTypes(file);
        Assertions.assertEquals(1, fileValidationTypes.size());
        Assertions.assertEquals(FileValidationImpl.FOLDER, fileValidationTypes.get(0));
    }
    @Test
    public void testAllInvalidFileValidationTypes() throws IOException {
        Set<FileValidation> strategies = new LinkedHashSet<FileValidation>();
        strategies.add(FileValidationImpl.NAME);
        strategies.add(FileValidationImpl.FOLDER);
        File file = new File("transactions1005.txt", "data/new/transactions1005.txt", "new1");
        FileService fileService = new FileService(strategies);
        List<FileValidation> fileValidationTypes = fileService.getInvalidFileValidationTypes(file);
        Assertions.assertEquals(2, fileValidationTypes.size());
        Assertions.assertEquals(FileValidationImpl.NAME, fileValidationTypes.get(0));
        Assertions.assertEquals(FileValidationImpl.FOLDER, fileValidationTypes.get(1));
    }
}
