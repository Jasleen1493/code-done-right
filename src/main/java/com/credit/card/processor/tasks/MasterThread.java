package com.credit.card.processor.tasks;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class MasterThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MasterThread.class);

    private static final int CONSUMER_COUNT = 5;
    private final static BlockingQueue<String> linesReadQueue = new ArrayBlockingQueue<String>(30);

    private volatile boolean isConsumer = false;
    private static boolean producerIsDone = false;

    public MasterThread(boolean consumer) {
        this.isConsumer = consumer;
    }

    public static void processThreads() {
        long startTime = System.nanoTime();
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        producerPool.submit(new MasterThread(false));
        ExecutorService consumerPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumerPool.submit(new MasterThread(true));
        }
        producerPool.shutdown();
        consumerPool.shutdown();

        while (!producerPool.isTerminated() && !consumerPool.isTerminated()) {
        }

        long endTime = System.nanoTime();
        long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
        logger.info("Total elapsed time: " + elapsedTimeInMillis + " ms");
    }

    private void readFile() throws IOException {
        logger.info("inside readFile ");
        List<String> filesToBeProcessed = new FileService(new HashSet<>()).getAllFileNamesFromPath(Constant.PROCESSING_FOLDER_PATH);
        logger.info("filesToBeProcessed.size() = " + filesToBeProcessed.size());
        try {
            for (String file : filesToBeProcessed) {
                logger.info("file = " + file);
                Stream<String> lines = Files.lines(Paths.get(file), StandardCharsets.UTF_8);
                for (String line : (Iterable<String>) lines::iterator) {
                    logger.info("read=" + line);
                    linesReadQueue.put(line);
                    logger.info(Thread.currentThread().getName() + ":: producer count = " + linesReadQueue.size());
                }

            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        producerIsDone = true;
        logger.info(Thread.currentThread().getName() + " producer is done");
    }

    @Override
    public void run() {
        logger.info("run() called " + isConsumer);
        if (isConsumer) {
            logger.info("inside consumer " + isConsumer);
            consume();
        } else {
            try {
                logger.info("inside producer " + isConsumer);
                readFile();
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
    }

    private void consume() {
        try {
            while (!producerIsDone || (producerIsDone && !linesReadQueue.isEmpty())) {
                String lineToProcess = linesReadQueue.take();
                logger.info("procesing:" + lineToProcess);
                logger.info(Thread.currentThread().getName() + ":: consumer count:" + linesReadQueue.size());
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        logger.info(Thread.currentThread().getName() + " consumer is done");
    }
}
