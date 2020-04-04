package com.credit.card.processor.service;

import com.credit.card.processor.model.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileServiceTest {

    File file = new File("transactions1000.csv", "src/test/resources/data/new/transactions1000.txt", "new");
    String path = "src/test/resources/data/new/transactions1000.txt";

    @Test
    public void testFileNotNull() {
        Assertions.assertNotNull(file);
    }

    @Test
    public void testFileNameNotNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new File(null, "src/test/resources/data/new/transactions1000.txt", "new");
        });
    }

    @Test
    public void testFilePathNotNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new File("transactions1000.csv", null, "new");
        });
    }

    @Test
    public void testFileFolderNotNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new File("transactions1000.csv", "src/test/resources/data/new/transactions1000.txt", null);
        });
    }

    @Test
    public void testFilePresentAtPathIsNotNull() throws IOException {
        String expectedData = "10001,5456789123498765,10000012,ONLINE";
        List<String> list = Files.lines(Paths.get(path)).collect(Collectors.toList());
        Assertions.assertEquals(expectedData, list.get(0));
    }

    @Test
    public void testTransactionFileNameSyntax() {
        Assertions.assertEquals("transactions", file.getName().substring(0, 12));
    }

    @Test
    public void testReferenceFileNameSyntax() {
        File file = new File("reference0001.csv", "src/test/resources/data/new/reference0001.txt", "new");
        Assertions.assertEquals("reference", file.getName().substring(0, 9));
    }

    @Test
    public void testNewFileFolderLocation() {
        Assertions.assertEquals("new", path.substring(24, 27));
    }

    @Test
    public void testIncorrectNewFileFolderLocation() {
        File file = new File("transactions1000.csv", "src/test/resources/data/processing/transactions1000.txt", "processing");
        Assertions.assertNotEquals("new", file.getFolder());
    }

    @Test
    public void testFileFolderLocationForCorrectNewFileInNewFolder() {
        File file = new File("transactions1000.csv", "src/test/resources/data/new/transactions1000.txt", "new");
        file.setFolder(file.getFolder().equals("new") ? "processing" : "garbage");
        Assertions.assertEquals("processing",file.getFolder());
    }

    @Test
    public void testFileFolderLocationForIncorrectNewFileInFolder() {
        File file = new File("transactions1000.csv", "src/test/resources/data/processing/transactions1000.txt", "processing");
        file.setFolder(file.getFolder().equals("new") ? "processing" : "garbage");
        Assertions.assertEquals("garbage",file.getFolder());
    }
}
