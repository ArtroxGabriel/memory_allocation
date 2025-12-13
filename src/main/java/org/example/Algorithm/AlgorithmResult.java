package org.example.Algorithm;

public class AlgorithmResult {
    private long startAddress;

    private AlgorithmError error;

    private AlgorithmResult(long startAddress) {
        this.startAddress = startAddress;
    }

    private AlgorithmResult(AlgorithmError error) {
        this.error = error;
    }

    public static AlgorithmResult Success(long startAddress) {
        return new AlgorithmResult(startAddress);
    }

    public static AlgorithmResult Failure(String message) {
        return new AlgorithmResult(new AlgorithmError(message));
    }

    public boolean isSuccess() {
        return error == null;
    }

    public long getStartAddressOrElseThrow() throws IllegalStateException {
        if (!isSuccess()) {
            throw new IllegalStateException("Cannot get start address from a failed algorithm result.");
        }
        return startAddress;
    }

    public AlgorithmError getErrorOrElseThrow() throws IllegalStateException {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot get error data from a successful algorithm result.");
        }

        return error;
    }


    public record AlgorithmError(String message) {
    }
}
