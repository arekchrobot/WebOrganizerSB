package ark.chr.web.organizer.controllers;


import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arek on 2015-09-10.
 */
@RestController
@Scope("session")
public class LocaleManager implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(LocaleManager.class);

    private Locale locale;

    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        logger.info("Changing locale to: " + language);
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
}
