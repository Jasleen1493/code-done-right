package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.model.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class FileServiceTest {
    @Autowired
    private FileService fileService;

    @Test
    public void testFileWithValidName() throws IOException {
        File file = new File("reference0001.txt", "data/new/reference0001.txt", "new");
        Assertions.assertTrue(fileService.hasValidFileName(file));
    }

    @Test
    public void testFileWithInvalidName() throws IOException {
        File file = new File("transactions1006.txt", "data/new/transactions1006.txt", "new");
        Assertions.assertFalse(fileService.hasValidFileName(file));
    }

    @Test
    public void testReadFileFromPath() throws IOException {
        File file = fileService.getFileUsingFileName("data/new/transaction1006.txt");
        Assertions.assertNotNull(file);
        Assertions.assertEquals("transaction1006.txt", file.getName());
        Assertions.assertEquals("new", file.getFolder());
    }

    @Test
    public void testGetAllNewFiles() throws IOException {
        Assertions.assertEquals(11, fileService.getAllFilesFromPath(Constant.NEW_FOLDER_PATH).size());
    }

    @Test
    public void testGetAllProcessingFilesInSortedOrder() throws IOException {
        Assertions.assertEquals("reference0001.txt", fileService.getAllFilesFromPath(Constant.PROCESSING_FOLDER_PATH).get(0).getName());
    }

    @Test
    public void testFilesMovedToProcessing() throws IOException {
        List<File> files = fileService.getAllFilesFromPath(Constant.NEW_FOLDER_PATH);
        Assertions.assertEquals(11, fileService.countMovedFiles(files, Constant.PROCESSING_FOLDER_PATH, Constant.PROCESSING));
    }
}
