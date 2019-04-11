package ch.zhaw.is;

final class OperationNotSupportedError extends RuntimeException {

    OperationNotSupportedError(String message) {
        super(message);
    }
}
