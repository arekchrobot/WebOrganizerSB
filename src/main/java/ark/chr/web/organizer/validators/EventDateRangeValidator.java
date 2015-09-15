package ark.chr.web.organizer.validators;

import java.util.Date;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Arek on 2015-09-10.
 */
@Component
@FacesValidator("eventDateRangeValidator")
public class EventDateRangeValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(EventDateRangeValidator.class);

    @Override
    public void validate(FacesContext facesContext, UIComponent uic, Object value) throws ValidatorException {
        if(value == null) {
            return;
        }

        Object startDateValue = uic.getAttributes().get("startDate");
        if(startDateValue == null) {
            return;
        }

        Date startDate = (Date) startDateValue;
        Date endDate = (Date) value;
        if(endDate.before(startDate)) {
            logger.info("End date of event set before start date.");
            ResourceBundle bundle
                    = facesContext.getApplication().getResourceBundle(
                    facesContext, "myMessages");
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    bundle.getString("eventDateRangeValidator.header"),
                    bundle.getString("eventDateRangeValidator.detail")));
        }
    }

}
