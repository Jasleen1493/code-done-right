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
import java.nio.file.StandardCopyOption;
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


    public void moveFile(String from, String to) throws IOException {
        Path src = Paths.get(from);
        Path dest = Paths.get(to);
        Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    public List<File> processNewFiles() throws IOException {
        List<File> files = new ArrayList<>();
        List<String> fileNames = getAllFileNamesFromPath(Constant.NEW_FOLDER_PATH);
        if (!fileNames.isEmpty()) {
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
                moveFile(oldPath, file.getPath());
                files.add(file);
            }
        }
        return files;
    }

    public File getFileFromFileName(String path) {
        File file = null;
        String[] arr = path.split("/");
        for (int i = 0; i < arr.length; i++) {
            file = new File(arr[2], path, arr[1]);
        }
        return file;
    }

    public List<String> getAllFileNamesFromPath(String path) throws IOException {
        return Files.list(Paths.get(path))
                .filter(s -> s.toString().endsWith(".txt"))
                .sorted()
                .map(x -> x.toString()).collect(Collectors.toList());
    }

    /*public boolean isProcessable(File file) throws IOException {
        // processing logic
        return true;
    }
*/
    /*public List<String> fileProcessor(File file) throws IOException {
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

    }*/
}
