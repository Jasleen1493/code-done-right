package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.constants.FileValidationType;
import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.FileValidationStrategy;
import com.credit.card.processor.validation.ValidationStrategy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileService {

    private Set<ValidationStrategy> fileValidationStrategies;

    public FileService(Set<ValidationStrategy> fileValidationStrategies) throws IOException {
        this.fileValidationStrategies = fileValidationStrategies;
    }

    public boolean isValid(File file) {
        Boolean flag = false;
        ValidationStrategy fileValidation = FileValidationStrategy.UNSUPPORTED;
        for (Iterator<ValidationStrategy> iterator = fileValidationStrategies.iterator(); iterator.hasNext(); ) {
            fileValidation = iterator.next();
            if (fileValidation.validate(file)) {
                flag = true;
            } else {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public List<FileValidationType> getInvalidFileValidationTypes(File file) {
        ValidationStrategy fileValidation = FileValidationStrategy.UNSUPPORTED;
        List<FileValidationType> validatedTypes = new ArrayList<FileValidationType>();
        for (Iterator<ValidationStrategy> iterator = fileValidationStrategies.iterator(); iterator.hasNext(); ) {
            fileValidation = iterator.next();
            if (!fileValidation.validate(file)) {
                validatedTypes.add(fileValidation.getValidationType());
            }
        }
        return validatedTypes;
    }

    public File setFileAtDestPath(File file, String destPath, String destFolder) throws IOException {
        copyFile(file.getPath(), destPath + file.getName());
        file.setPath(destPath + file.getName());
        file.setFolder(destFolder);
        return file;
    }

    public void copyFile(String from, String to) throws IOException {
        Path src = Paths.get(from);
        Path dest = Paths.get(to);
        Files.copy(src, dest);
    }

    public List<File> fileValidation() throws IOException {
        List<File> files = new ArrayList<>();
        List<String> newFiles = getAllFilesFromPath(Constant.NEW_FOLDER_PATH);
        if (!newFiles.isEmpty()) {
            for (String newFile : newFiles) {
                File file = createFile(newFile);
                if (isValid(file)) {
                    files.add(setFileAtDestPath(file, Constant.PROCESSING_FOLDER_PATH, Constant.PROCESSING));
                    isProcessable(file);
                } else {
                    getInvalidFileValidationTypes(file);
                    files.add(setFileAtDestPath(file, Constant.GARBAGE_FOLDER_PATH, Constant.GARBAGE));
                }
            }
        }
        return files;
    }

    public File createFile(String path) {
        File file = null;
        String[] arr = path.split("/");
        for (int i = 0; i < arr.length; i++) {
            file = new File(arr[2], path, arr[1]);
        }
        return file;
    }

    public List<String> getAllFilesFromPath(String path) throws IOException {
        return Files.list(Paths.get(path))
                .filter(s -> s.toString().endsWith(".txt"))
                .sorted()
                .map(x -> x.toString()).collect(Collectors.toList());
    }

    public boolean isProcessable(File file) throws IOException {
        // processing logic
        return true;
    }

    public List<String> fileProcessor(File file) throws IOException {
        List<String> record = new ArrayList<>();
        if (isProcessable(file)) {
            record = Files.lines(Paths.get(file.getPath())).collect(Collectors.toList());
            publishToKafka(record);
            setFileAtDestPath(file, Constant.DONE_FOLDER_PATH, Constant.DONE);
        } else {
            setFileAtDestPath(file, Constant.ERROR_FOLDER_PATH, Constant.ERROR);
        }
        return record;
    }

    public void publishToKafka(List<String> record) {

    }
}
