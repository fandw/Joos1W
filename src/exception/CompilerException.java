package exception;

/**
 * Created by daiweifan on 2017-05-01.
 */
public abstract class CompilerException extends RuntimeException {
    private String message;

    public CompilerException(String message) {
        this.message = message;
    }

    public void printErrorMessage() {
        System.err.println(message);
    }
}
