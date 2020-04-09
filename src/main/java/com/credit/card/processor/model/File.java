package com.credit.card.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class File {
    @NonNull
    private String name;
    @NonNull
    private String path;
    @NonNull
    private String folder;
}
