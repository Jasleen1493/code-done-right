package com.credit.card.processor.constants;

public interface Constant {
    String TRANSACTION = "transaction";
    String REFERENCE = "reference";
    String NEW = "new";
    String PROCESSING = "processing";
    String GARBAGE = "garbage";
    String DONE = "done";
    String ERROR = "error";
    String NEW_FOLDER_PATH = "data/new/";
    String PROCESSING_FOLDER_PATH = "data/processing/";
    String GARBAGE_FOLDER_PATH = "data/garbage/";
    String DONE_FOLDER_PATH = "data/done/";
    String ERROR_FOLDER_PATH = "data/error/";
    String TRANSACTION_FILE_NAME_REGEX = "^transaction[0-9]{4}.*";
    String REFERENCE_FILE_NAME_REGEX = "^reference[0-9]{4}.*";
    String APPLICATION_LOG_FILE_PATH = "src/main/resources/logs/AppLog.txt";
    String FILE_EXTENSION = ".txt";
}
