package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class FileServiceTest {
    @Autowired
    private FileService fileService;

    @Test
    public void testFileWithValidName() {
        Assertions.assertTrue(fileService.hasValidFileName("reference0001.txt"));
    }

    @Test
    public void testGetFileNamesFromPath() throws IOException {
        Assertions.assertEquals("reference0001.txt", fileService.getAllFilesFromPath(Constant.DONE_FOLDER_PATH).get(0));
    }

   @Test
    public void testFilesSentToGarbage() throws IOException {
        Assertions.assertEquals(10, fileService.getFilesInGarbage().size());
    }

    @Test
    public void testFileNameInGarbage() throws IOException {
        Assertions.assertEquals("transactions1000.txt", fileService.getFilesInGarbage().get(0));
    }

    @Test
    public void testFilesProcessed() throws IOException {
        Assertions.assertEquals(1, fileService.getProcessedFiles().size());
    }

    @Test
    public void testFilesNotProcessed() throws IOException {
        Assertions.assertEquals(0, fileService.getFilesWithErrors().size());
    }

}
