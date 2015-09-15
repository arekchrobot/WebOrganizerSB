package ark.chr.web.organizer.services.api.exceptions;

/**
 *
 * @author Arek
 */
@SuppressWarnings("serial")
public class RegisterConfirmationExpiredException extends RegisterConfirmationFailedException {

    public RegisterConfirmationExpiredException() {
        super("Register confirmation expired.");
    }

    public RegisterConfirmationExpiredException(String msg) {
        super(msg);
    }
}
