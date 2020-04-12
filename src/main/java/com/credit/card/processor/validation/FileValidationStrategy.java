package com.credit.card.processor.validation;

import com.credit.card.processor.constants.Constant;
import com.credit.card.processor.constants.FileValidationType;
import com.credit.card.processor.model.File;

import java.util.regex.Pattern;

public enum FileValidationStrategy implements ValidationStrategy {
    NAME(FileValidationType.NAME) {
        public <T extends File> boolean validate(File file) {
            if (Pattern.matches(Constant.TRANSACTION_FILE_NAME_REGEX, file.getName())||
                    Pattern.matches(Constant.REFERENCE_FILE_NAME_REGEX,file.getName())) {
                return true;
            }
            return false;
        }

        @Override
        public FileValidationType getValidationType() {
            return FileValidationType.NAME;
        }
    },
    FOLDER(FileValidationType.FOLDER) {
        public <T extends File> boolean validate(File file) {
            if (file.getFolder().equals(Constant.NEW)) {
                return true;
            }
            return false;
        }

        @Override
        public FileValidationType getValidationType() {
            return FileValidationType.FOLDER;
        }
    },
    UNSUPPORTED(FileValidationType.UNSUPPORTED) {
        public <T extends File> boolean validate(File file) {
            return false;
        }

        @Override
        public FileValidationType getValidationType() {
            return FileValidationType.UNSUPPORTED;
        }
    };
    private FileValidationType fileValidationType;

    private FileValidationStrategy(FileValidationType fileValidationType) {
        this.fileValidationType = fileValidationType;
    }

    public FileValidationType getFileValidationType() {
        return fileValidationType;
    }
}
