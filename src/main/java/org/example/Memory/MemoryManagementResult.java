package org.example.Memory;

import lombok.Getter;

@Getter
public class MemoryManagementResult {
    private final String message;
    private final boolean isSuccess;

    private MemoryManagementResult(String message, boolean success) {
        this.isSuccess = success;
        this.message = message;
    }


    public static MemoryManagementResult Success() {
        return new MemoryManagementResult("", true);
    }

    public static MemoryManagementResult Success(String message) {
        return new MemoryManagementResult(message, true);
    }

    public static MemoryManagementResult Failure(String errorMessage) {
        return new MemoryManagementResult(errorMessage, false);
    }


}
