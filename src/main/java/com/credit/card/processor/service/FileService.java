package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.constants.FileValidationType;
import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.FileValidationStrategy;
import com.credit.card.processor.validation.ValidationStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    public void copyFileToFolder(File file) throws IOException {
        if (isValid(file)) {
            copyFile(file.getPath(), Constant.PROCESSING_FOLDER_PATH + file.getName());
            file.setPath(Constant.PROCESSING_FOLDER_PATH + file.getName());
            file.setFolder(Constant.PROCESSING);
            //process(file);
        } else {
            getInvalidFileValidationTypes(file);
            copyFile(file.getPath(), Constant.GARBAGE_FOLDER_PATH + file.getName());
            file.setPath(Constant.GARBAGE_FOLDER_PATH + file.getName());
            file.setFolder(Constant.GARBAGE);
        }
    }

    public void copyFile(String from, String to) throws IOException {
        Path src = Paths.get(from);
        Path dest = Paths.get(to);
        Files.copy(src, dest);
    }

    public void process(File file) throws IOException {
        List<String> list = Files.lines(Paths.get(file.getPath())).collect(Collectors.toList());
    }

    public static void publishToKafka() {
    }
}
