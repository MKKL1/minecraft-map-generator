package com.mkkl.io.parsing;

import java.io.IOException;

public class ParsingException extends IOException {
    public ParsingException(String message) {
        super("Parsing exception: " + message);
    }
}
