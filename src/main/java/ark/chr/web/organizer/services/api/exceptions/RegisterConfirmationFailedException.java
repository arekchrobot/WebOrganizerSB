package ark.chr.web.organizer.services.api.exceptions;

/**
 *
 * @author Arek
 */
@SuppressWarnings("serial")
public class RegisterConfirmationFailedException extends Exception {
    
    public RegisterConfirmationFailedException() {
        super("Registration confirmation failed.");
    }

    public RegisterConfirmationFailedException(String msg) {
        super(msg);
    }

}
