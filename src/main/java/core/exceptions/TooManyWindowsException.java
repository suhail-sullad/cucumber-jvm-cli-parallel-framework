

package core.exceptions;

public class TooManyWindowsException extends RuntimeException {

    public TooManyWindowsException() {
        super();
    }

    public TooManyWindowsException(String s) {
        super(s);
    }
}
