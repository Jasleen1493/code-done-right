package com.credit.card.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NonNull
public class File {
    @NonNull
    private String name;
    @NonNull
    private String path;
    @NonNull
    private String folder;
}