package exceptions;

public class SudokuWriteException extends Exception {

    public SudokuWriteException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
