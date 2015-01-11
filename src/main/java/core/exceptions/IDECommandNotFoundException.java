

package core.exceptions;

public class IDECommandNotFoundException extends RuntimeException {

    public IDECommandNotFoundException() {
        super();
    }

    public IDECommandNotFoundException(String s) {
        super(s);
    }
}
