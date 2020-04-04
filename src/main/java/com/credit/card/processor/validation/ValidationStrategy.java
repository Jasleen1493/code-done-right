package com.credit.card.processor.validation;

import com.credit.card.processor.constants.FileValidationType;
import com.credit.card.processor.model.File;

public interface ValidationStrategy {
    <T extends File> boolean validate(File file);
    FileValidationType getValidationType();
}
