package com.devfay.jcodegenerator.util;

import lombok.Getter;

@Getter
public enum WorkFolder {

    CIFRADOS("TED_CIFRADOS"), COMPRIMIDOS("TED_COMPRIMIDOS"), FINALES("TED_FINALES"), TEMP("TED_TEMP");

    private final String value;

    WorkFolder(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
