package com.inusak.android.moviesapp.exception;

/**
 * All invalid states will be handled through this class.
 *
 * Created by Nilanka on 10/14/2017.
 */

public class InvalidStatusException extends Exception {

    private int code;

    private String message;

    public InvalidStatusException(int code, String cause) {
        super(cause);
        this.code = code;
    }

    public InvalidStatusException(int code, String cause, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }
}
