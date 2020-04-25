package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import static com.credit.card.processor.constants.Constant.*;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.stream.Collectors.toList;

@Service
@Data
@Slf4j
public class FileService {

    public Boolean hasValidFileName(String fileName) {
        return (Pattern.matches(Constant.TRANSACTION_FILE_NAME_REGEX, fileName) ||
                Pattern.matches(Constant.REFERENCE_FILE_NAME_REGEX, fileName));
    }

    @SneakyThrows
    private Path moveFiles(String srcPath, String destPath) {
        return Files.move(get(srcPath), get(destPath), REPLACE_EXISTING);
    }

    public List<String> getAllFilesFromPath(String path) throws IOException {
        return (Files.list(get(path)).filter(s -> s.toString().endsWith(FILE_EXTENSION))
                .sorted()
                .map(Path::toString))
                .map(s -> s.split("/"))
                .map(s -> s[2]).collect(toList());
    }

    public List<String> getFilesReadyForProcessing() throws IOException {
        List<String> files = getAllFilesFromPath(Constant.NEW_FOLDER_PATH);
        files.stream().filter(f -> (!hasValidFileName(f)))
                .map(s -> moveFiles(Constant.NEW_FOLDER_PATH + s, Constant.GARBAGE_FOLDER_PATH + s))
                .collect(toList());
        files.stream().filter(f -> hasValidFileName(f))
                .map(s -> moveFiles(Constant.NEW_FOLDER_PATH + s, Constant.PROCESSING_FOLDER_PATH + s))
                .collect(toList());
        return getAllFilesFromPath(Constant.PROCESSING_FOLDER_PATH);
    }

    public List<String> getProcessedFiles() throws IOException {
        List<String> filesReadyForProcessing = getFilesReadyForProcessing();
        filesReadyForProcessing.stream().filter(f -> (Files.isRegularFile(Paths.get(f))))
                .map(s -> moveFiles(Constant.PROCESSING_FOLDER_PATH + s, Constant.ERROR_FOLDER_PATH + s))
                .collect(toList());
        filesReadyForProcessing.stream().filter(f -> Files.isRegularFile(Paths.get(Constant.PROCESSING_FOLDER_PATH + f)))
                .map(s -> moveFiles(Constant.PROCESSING_FOLDER_PATH + s, Constant.DONE_FOLDER_PATH + s))
                .collect(toList());
        return getAllFilesFromPath(Constant.DONE_FOLDER_PATH);
    }

    public List<String> getFilesWithErrors() throws IOException {
        return getAllFilesFromPath(ERROR_FOLDER_PATH);
    }

    public List<String> getFilesInGarbage() throws IOException {
        return getAllFilesFromPath(Constant.GARBAGE_FOLDER_PATH);
    }
}
