package com.sp.lib.common.exception;


public class SlibInitialiseException extends RuntimeException {
    public SlibInitialiseException(String detailMessage) {
        super(detailMessage);
    }

    public SlibInitialiseException(Class cls) {
        super("please call " + cls.getName() + " init() method first!");
    }
}
