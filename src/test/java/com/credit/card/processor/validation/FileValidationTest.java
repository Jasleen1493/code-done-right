package com.credit.card.processor.validation;

import com.credit.card.processor.constants.FileValidationType;
import com.credit.card.processor.model.File;
import com.credit.card.processor.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FileValidationTest {
    @Test
    public void testValidatorForFileNameAndFolder() throws IOException {
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        strategies.add(FileValidationStrategy.FOLDER);
        File file = new File("transaction1000.csv", "data/new/transaction1000.txt", "new");
        FileService fileService = new FileService(strategies);
        Assertions.assertTrue(fileService.isValid(file));
    }

    @Test
    public void testValidatorForNewFileFolder() throws IOException {
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.FOLDER);
        File file = new File("transactions1000.csv", "data/new/transactions1000.txt", "new");
        FileService fileService = new FileService(strategies);
        Assertions.assertTrue(fileService.isValid(file));
    }

    @Test
    public void testValidatorForNewFileName() throws IOException {
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        File file = new File("transaction1001.txt", "data/new/transactions1001.txt", "new");
        FileService fileService = new FileService(strategies);
        Assertions.assertTrue(fileService.isValid(file));
    }

    @Test
    public void testInvalidFileValidationTypes() throws IOException {
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        strategies.add(FileValidationStrategy.FOLDER);
        File file = new File("transactions1000.txt", "data/new/transactions1000.txt", "new");
        FileService fileService = new FileService(strategies);
        List<FileValidationType> fileValidationTypes = fileService.getInvalidFileValidationTypes(file);
        Assertions.assertEquals(1, fileValidationTypes.size());
        Assertions.assertEquals(FileValidationType.NAME, fileValidationTypes.get(0));
    }

    @Test
    public void testCopyFileToProcessingFolder() throws IOException {
        File file = new File("transaction1004.csv", "data/new/transaction1004.txt", "new");
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        strategies.add(FileValidationStrategy.FOLDER);
        FileService fileService = new FileService(strategies);
        fileService.copyFileToFolder(file);
        Assertions.assertNotNull(Files.exists(Paths.get(file.getPath())));
    }

    @Test
    public void testCopyfileToGarbageFolder() throws IOException {
        File file = new File("transactions1005.csv", "data/new/transactions1005.txt", "new");
        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
        strategies.add(FileValidationStrategy.NAME);
        strategies.add(FileValidationStrategy.FOLDER);
        FileService fileService = new FileService(strategies);
        fileService.copyFileToFolder(file);
        Assertions.assertNotNull(Files.exists(Paths.get(file.getPath())));
    }
}
