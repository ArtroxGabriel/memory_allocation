package org.example.Algorithm;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AlgorithmResult {
    private int startAddress;

    private AlgorithmError error;

    private AlgorithmResult(int startAddress) {
        this.startAddress = startAddress;
    }

    private AlgorithmResult(AlgorithmError error) {
        this.error = error;
    }

    public static AlgorithmResult Success(int startAddress) {
        return new AlgorithmResult(startAddress);
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
        return new AlgorithmData(startAddress);
    }

    public AlgorithmError getErrorOrElseThrow() throws IllegalStateException {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot get error data from a successful algorithm result.");
        }

        return error;
    }


    public record AlgorithmData(int startAddress) {
    }

    public record AlgorithmError(String message) {
    }
}
