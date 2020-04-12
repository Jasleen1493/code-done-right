package com.credit.card.processor.schedulers;

import com.credit.card.processor.tasks.MasterThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileProcessorScheduler {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessorScheduler.class);

    public void setTime() throws InterruptedException{
        logger.info("Inside scheduler");
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        logger.info("Scheduler activated");
        executorService.scheduleAtFixedRate(MasterThread::processThreads, 0, 2, TimeUnit.HOURS);
    }
}
