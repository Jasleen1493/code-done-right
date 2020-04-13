package com.credit.card.processor.model;

import com.credit.card.processor.validation.FileValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
@AllArgsConstructor
public class File {
    @NonNull
    private String name;
    @NonNull
    private String path;
    @NonNull
    private String folder;
    @NonNull
    private Set<FileValidation> fileValidations;

    public File(@NonNull String name, @NonNull String path, @NonNull String folder) {
        this.name = name;
        this.path = path;
        this.folder = folder;
    }
}
