package com.banco.account.api;

public class ApiException extends RuntimeException {
    private final int status;

    public ApiException(int s, String m) {
        super(m);
        status = s;
    }

    public int status() {
        return status;
    }
}
