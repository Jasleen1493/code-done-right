package com.credit.card.processor;

import com.credit.card.processor.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
class ProcessorApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testApplication() throws IOException {
        FileService fileService = new FileService();
        Assertions.assertEquals(1, fileService.getProcessedFiles().size());
    }
}
