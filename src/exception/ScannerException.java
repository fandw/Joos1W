package exception;

import common.Token;
import util.Location;

/**
 * Created by daiweifan on 2017-05-01.
 */
public class ScannerException extends CompilerException {
    private Location location;

    private void printLocation() {
        System.err.println("At " + location.getFilePath() + "(" +
                location.getLine() + ", " + location.getColumn() + ")");
    }
    public ScannerException(Location location, String message) {
        super(message);
        this.location = location;
    }

    public void printErrorMessage() {
        printLocation();
        super.printErrorMessage();
    }

}
