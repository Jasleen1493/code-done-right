package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.constants.FileValidationType;
import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.FileValidationStrategy;
import com.credit.card.processor.validation.ValidationStrategy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService{

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

    public List<File> readNewFiles() throws IOException {
        List<File> files = new ArrayList<>();
        List<String> fileNames = getAllFileNamesFromPath(Constant.NEW_FOLDER_PATH);
        if (CollectionUtils.isEmpty(fileNames)) {
            for (String fileName : fileNames) {
                File file = getFileFromFileName(fileName);
                String oldPath = file.getPath();
                if (isValid(file)) {
                    file.setFolder(Constant.PROCESSING);
                    file.setPath(Constant.PROCESSING_FOLDER_PATH + file.getName());
                } else {
                    file.setFolder(Constant.GARBAGE);
                    file.setPath(Constant.GARBAGE_FOLDER_PATH + file.getName());
                    getInvalidFileValidationTypes(file);
                }
                Files.move(Paths.get(oldPath), Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
                files.add(file);
            }
        }
        return files;
    }

    public File getFileFromFileName(String path) {
        String[] arr = path.split("/");
        return Stream.of(arr).map(f->new File(arr[2], path, arr[1])).collect(Collectors.toList()).get(0);
     }

    public List<String> getAllFileNamesFromPath(String path) throws IOException {
        return Files.list(Paths.get(path))
                .filter(s -> s.toString().endsWith(".txt"))
                .sorted()
                .map(x -> x.toString()).collect(Collectors.toList());
    }
}
