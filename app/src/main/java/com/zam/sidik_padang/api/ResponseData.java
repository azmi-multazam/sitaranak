package com.zam.sidik_padang.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ResponseData<T> {

    public final State state;
    @Nullable
    public final T data;
    @NonNull
    public final String message;
    public final boolean success;

    public ResponseData(State state, @Nullable T data, @NonNull String message, boolean success) {
        this.state = state;
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public static <T> ResponseData<T> success(@Nullable T data, String message, boolean success) {
        return new ResponseData<>(State.SUCCESS, data, message, success);
    }

    public static <T> ResponseData<T> error(String msg) {
        return new ResponseData<>(State.ERROR, null, msg, false);
    }

    public static <T> ResponseData<T> failure(String message) {
        return new ResponseData<>(State.FAILURE, null, message, false);
    }

    public static <T> ResponseData<T> loading(String message) {
        return new ResponseData<>(State.LOADING, null, message, false);
    }
}
