package com.credit.card.processor.service;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.FileValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {

    private Set<FileValidation> fileValidationStrategies;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public FileService(Set<FileValidation> fileValidationStrategies) throws IOException {
        this.fileValidationStrategies = fileValidationStrategies;
    }

    public boolean isValid(File file) {
        List<FileValidation> validStrategies = fileValidationStrategies.stream().filter(fvs -> (fvs.validate(file))).collect(Collectors.toList());
        return (validStrategies.size() == fileValidationStrategies.size());
    }

    public List<FileValidation> getInvalidFileValidationTypes(File file) {
        return fileValidationStrategies.stream().filter(fvs -> (!fvs.validate(file))).collect(Collectors.toList());
    }

    public Map<Boolean, List<File>> getFilesWithValidStatus(List<File> files) throws IOException {
        Map<Boolean, List<File>> filesWithValidStatus = files.stream().collect(Collectors.partitioningBy(file -> isValid(file)));
        return filesWithValidStatus;
    }

    public long getMovedFiles(List<File> files, String destPath, String destFolder) throws IOException {
        return files.stream().map(fm -> {
            try {
                Files.move(Paths.get(fm.getPath()), Paths.get(destPath + fm.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
            }
            fm.setPath(destPath + fm.getName());
            fm.setFolder(destFolder);
            return fm;
        }).filter(f->f.getFolder().equals(destFolder)).count();
    }

    public List<File> getAllFiles(String path) throws IOException {
        List<String> fileNames = Files.list(Paths.get(path))
                .filter(s -> s.toString().endsWith(Constant.FILE_EXTENSION))
                .sorted()
                .map(x -> x.toString()).collect(Collectors.toList());
        return fileNames.stream().map(f -> getFileFromFileName(f)).collect(Collectors.toList());
    }

    public File getFileFromFileName(String fileName) {
        String[] arr = fileName.split("/");
        return Stream.of(arr).map(f -> new File(arr[2], fileName, arr[1])).collect(Collectors.toList()).get(0);
    }
}
