package com.credit.card.processor.validation.impl;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.constants.FileValidationType;
import com.credit.card.processor.model.File;
import com.credit.card.processor.validation.FileValidation;

import java.util.regex.Pattern;

public enum FileValidationImpl implements FileValidation {
    NAME(FileValidationType.NAME) {
        public <T extends File> boolean validate(File file) {
            if (Pattern.matches(Constant.TRANSACTION_FILE_NAME_REGEX, file.getName())||
                    Pattern.matches(Constant.REFERENCE_FILE_NAME_REGEX,file.getName())) {
                return true;
            }
            return false;
        }

    },
    FOLDER(FileValidationType.FOLDER) {
        public <T extends File> boolean validate(File file) {
            if (file.getFolder().equals(Constant.NEW)) {
                return true;
            }
            return false;
        }
    },
    UNSUPPORTED(FileValidationType.UNSUPPORTED) {
        public <T extends File> boolean validate(File file) {
            return false;
        }
    };
    private FileValidationType fileValidationType;

    private FileValidationImpl(FileValidationType fileValidationType) {
        this.fileValidationType = fileValidationType;
    }

    public FileValidationType getFileValidationType() {
        return fileValidationType;
    }
}
