package cz.muni.fi.pv168;

/**
 * Created by alexandra on 25.3.15.
 */
public class SecretAgencyException extends Exception{
    public SecretAgencyException(String msg) {
        super(msg);
    }

    public SecretAgencyException(Throwable cause) {
        super(cause);
    }

    public SecretAgencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
