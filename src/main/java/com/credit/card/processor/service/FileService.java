package com.credit.card.processor.service;

import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.FileValidation;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.credit.card.processor.constants.Constant.FILE_EXTENSION;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

@Service
@Data
@Slf4j
public class FileService {

    public boolean isValid(File file) {
        List<FileValidation> validStrategies = file.getFileValidations().stream().filter(fvs -> (fvs.validate(file))).collect(toList());
        return (validStrategies.size() == file.getFileValidations().size());
    }

    public List<FileValidation> getInvalidFileValidationTypes(File file) {
        return file.getFileValidations().stream().filter(fvs -> (!fvs.validate(file))).collect(toList());
    }

    public Map<Boolean, List<File>> getFilesWithValidStatus(List<File> files) {
        return files.stream().collect(partitioningBy(this::isValid));
    }

    public long getMovedFiles(List<File> files, String destPath, String destFolder) {
        return files.stream().map(fm -> {
            moveFiles(destPath, fm);
            fm.setPath(destPath + fm.getName());
            fm.setFolder(destFolder);
            return fm;
        }).filter(f -> f.getFolder().equals(destFolder)).count();
    }

    @SneakyThrows
    private Path moveFiles(String destPath, File fm) {
        return Files.move(get(fm.getPath()), get(destPath + fm.getName()), REPLACE_EXISTING);
    }

    public List<File> getAllFiles(String path) throws IOException {
        List<String> fileNames = Files.list(get(path))
                .filter(s -> s.toString().endsWith(FILE_EXTENSION))
                .sorted()
                .map(Path::toString).collect(toList());
        return fileNames.stream().map(this::getFileFromFileName).collect(toList());
    }

    public File getFileFromFileName(String fileName) {
        String[] arr = fileName.split("/");
        return Stream.of(arr).map(f -> new File(arr[2], fileName, arr[1])).collect(toList()).get(0);
    }
}
