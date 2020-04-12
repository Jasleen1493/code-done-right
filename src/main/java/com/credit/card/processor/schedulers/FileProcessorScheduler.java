package com.credit.card.processor.schedulers;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FileProcessorScheduler {
    public void setTime() throws IOException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
       // executorService.scheduleAtFixedRate(, 0, 2, TimeUnit.HOURS);
    }
}
