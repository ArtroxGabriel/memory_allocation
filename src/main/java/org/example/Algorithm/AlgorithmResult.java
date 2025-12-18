package org.example.Algorithm;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlgorithmResult {
    private int startAddress;
    private int blockId;

    private AlgorithmError error;

    private AlgorithmResult(int startAddress, int blockId) {
        this.startAddress = startAddress;
        this.blockId = blockId;
    }

    private AlgorithmResult(AlgorithmError error) {
        this.error = error;
    }

    public static AlgorithmResult Success(int startAddress, int blockId) {
        return new AlgorithmResult(startAddress, blockId);
    }

    public static AlgorithmResult Failure(String message) {
        return new AlgorithmResult(new AlgorithmError(message));
    }

    public boolean isSuccess() {
        return error == null;
    }

    public AlgorithmData getResultOrElseThrow() throws IllegalStateException {
        if (!isSuccess()) {
            throw new IllegalStateException("Cannot get start address from a failed algorithm result.");
        }
        return new AlgorithmData(startAddress, blockId);
    }

    public AlgorithmError getErrorOrElseThrow() throws IllegalStateException {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot get error data from a successful algorithm result.");
        }

        return error;
    }


    public record AlgorithmData(int startAddress, int blockId) {
    }

    public record AlgorithmError(String message) {
    }
}
