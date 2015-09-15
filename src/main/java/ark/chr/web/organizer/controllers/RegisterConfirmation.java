package ark.chr.web.organizer.controllers;

import ark.chr.web.organizer.services.api.IMailRegisterConfirmation;
import ark.chr.web.organizer.services.api.exceptions.RegisterConfirmationExpiredException;
import ark.chr.web.organizer.services.api.exceptions.RegisterConfirmationFailedException;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arek on 2015-09-10.
 */
@RestController
@Scope("request")
public class RegisterConfirmation {

    private static final Logger logger = LoggerFactory.getLogger(RegisterConfirmation.class);

    private boolean registerSuccess = false;
    private boolean registerExpired = false;
    private boolean registerFailed = false;

    private String userToConfirmId;
    private String digest;

    @Autowired
    private IMailRegisterConfirmation mailRegisterConfirmation;

    public RegisterConfirmation() {
        userToConfirmId = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("s");
        digest = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("d");
    }

    @PostConstruct
    private void initialize() {
        try {
            mailRegisterConfirmation.confirmUserRegistration(Long.parseLong(userToConfirmId), digest);
        } catch (RegisterConfirmationFailedException ex) {
            if(ex instanceof RegisterConfirmationExpiredException) {
                logger.info("Registration link expired for userId: " + userToConfirmId);
                registerExpired = true;
            } else {
                logger.warn("Registration failed to confirm for userId: " + userToConfirmId);
                registerFailed = true;
            }
        }
        logger.info("Registratrion confirm success for userId: " + userToConfirmId);
        registerSuccess = true;
    }

    public boolean isRegisterSuccess() {
        return registerSuccess;
    }

    public void setRegisterSuccess(boolean registerSuccess) {
        this.registerSuccess = registerSuccess;
    }

    public boolean isRegisterExpired() {
        return registerExpired;
    }

    public void setRegisterExpired(boolean registerExpired) {
        this.registerExpired = registerExpired;
    }

    public boolean isRegisterFailed() {
        return registerFailed;
    }

    public void setRegisterFailed(boolean registerFailed) {
        this.registerFailed = registerFailed;
    }
}
