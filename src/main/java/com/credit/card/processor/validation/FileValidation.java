package com.credit.card.processor.validation;

import com.credit.card.processor.model.File;

public interface FileValidation {
    <T extends File> boolean validate(File file);
}
