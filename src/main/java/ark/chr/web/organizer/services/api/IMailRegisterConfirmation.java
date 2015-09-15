package ark.chr.web.organizer.services.api;

import ark.chr.web.organizer.domain.OrganizerUser;
import ark.chr.web.organizer.services.api.exceptions.RegisterConfirmationFailedException;

import java.util.Locale;

/**
 * Created by Arek on 2015-07-15.
 */
public interface IMailRegisterConfirmation {

    void sendConfirmRegistrationEmail(OrganizerUser userToRegister, Locale locale);

    void confirmUserRegistration(Long userId, String digest) throws RegisterConfirmationFailedException;
}
