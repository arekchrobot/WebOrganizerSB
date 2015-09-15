package ark.chr.web.organizer.controllers;

import ark.chr.web.organizer.domain.OrganizerUser;
import ark.chr.web.organizer.services.api.IMailRegisterConfirmation;
import ark.chr.web.organizer.services.api.IRegisterService;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
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
public class Register {

    private static final Logger logger = LoggerFactory.getLogger(Register.class);
    private static final String REGISTER_SUCCESS = "registerSuccess";

    @Autowired
    private IRegisterService registerService;
    @Autowired
    private IMailRegisterConfirmation mailRegisterConfirmation;
    @Autowired
    private LocaleManager localeManager;

    private OrganizerUser registerUser;

    public Register() {
        registerUser = new OrganizerUser();
    }

    public OrganizerUser getRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(OrganizerUser registerUser) {
        this.registerUser = registerUser;
    }

    public String registerNewUser() {
        logger.info("Registering new user: " + registerUser.getLogin());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (registerService.registerUser(registerUser)) {
            mailRegisterConfirmation.sendConfirmRegistrationEmail(registerUser,
                    localeManager.getLocale());
            return REGISTER_SUCCESS;
        }
        ResourceBundle bundle
                = facesContext.getApplication().getResourceBundle(
                facesContext, "myMessages");
        facesContext.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL,
                        bundle.getString("register.error.message.header"),
                        bundle.getString("register.error.message.detail")));
        return null;
    }
}
