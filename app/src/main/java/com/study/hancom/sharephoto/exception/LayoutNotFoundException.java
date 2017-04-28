package com.study.hancom.sharephoto.exception;

import java.io.FileNotFoundException;

public class LayoutNotFoundException extends FileNotFoundException {
    public LayoutNotFoundException() {
        super();
    }
    public LayoutNotFoundException(String message) {
        super(message);
    }
}