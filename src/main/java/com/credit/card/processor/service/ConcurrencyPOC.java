package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ConcurrencyPOC implements Runnable {

    private static final int CONSUMER_COUNT = 5;
    private final static BlockingQueue<String> linesReadQueue = new ArrayBlockingQueue<String>(30);

    private volatile boolean isConsumer = false;
    private static boolean producerIsDone = false;

    public ConcurrencyPOC(boolean consumer) {
        this.isConsumer = consumer;
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        producerPool.execute(new ConcurrencyPOC(false));
        TimeUnit.SECONDS.sleep(5);
        ExecutorService consumerPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumerPool.execute(new ConcurrencyPOC(true));
        }
        TimeUnit.SECONDS.sleep(5);
        producerPool.shutdown();
        consumerPool.shutdown();

        while (!producerPool.isTerminated() && !consumerPool.isTerminated()) {
        }

        long endTime = System.nanoTime();
        long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
        System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");

    }

    private void readFile() throws IOException {
        System.out.println("inside readFile ");
        List<String> filesToBeProcessed = new FileService(new HashSet<>()).getAllFileNamesFromPath(Constant.PROCESSING_FOLDER_PATH);
        System.out.println("filesToBeProcessed.size() = " + filesToBeProcessed.size());
        try {
            for (String file : filesToBeProcessed) {
                System.out.println("file = " + file);
                Stream<String> lines = Files.lines(Paths.get(file), StandardCharsets.UTF_8);
                for (String line : (Iterable<String>) lines::iterator) {
                    System.out.println("read=" + line);
                    linesReadQueue.put(line);
                    System.out.println(Thread.currentThread().getName() + ":: producer count = " + linesReadQueue.size());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        producerIsDone = true;
        System.out.println(Thread.currentThread().getName() + " producer is done");
    }

    @Override
    public void run() {
        System.out.println("run() called " + isConsumer);
        if (isConsumer) {
            System.out.println("inside consumer" + isConsumer);
            consume();
        } else {
            try {
                System.out.println("inside producer " + isConsumer);
                readFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void consume() {
        try {
            while (!producerIsDone || (producerIsDone && !linesReadQueue.isEmpty())) {
                String lineToProcess = linesReadQueue.take();
                processCpuDummy();
                System.out.println("procesed:" + lineToProcess);
                System.out.println(Thread.currentThread().getName() + ":: consumer count:" + linesReadQueue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " consumer is done");
    }

    public void processCpuDummy() {
        for (long i = 0; i < 100000000l; i++) {
            i = i + 1;
        }
    }
}
