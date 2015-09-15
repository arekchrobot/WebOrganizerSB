package ark.chr.web.organizer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ark.chr.web.organizer.domain.OrganizerEvent;
import ark.chr.web.organizer.domain.OrganizerUser;
import ark.chr.web.organizer.domain.Priority;
import ark.chr.web.organizer.services.api.IEventService;
import ark.chr.web.organizer.services.security.UserDetailsAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Arek on 2015-09-10.
 */
@RestController
@Scope("request")
public class Events {

    private static final Logger logger = LoggerFactory.getLogger(Events.class);
    private static final String SUCCESS_PAGE = "home.xhtml?faces-redirect=true";

    private OrganizerEvent event;

    @Autowired
    private IEventService eventService;

    public Events() {
        event = new OrganizerEvent();
    }

    public OrganizerEvent getEvent() {
        return event;
    }

    public void setEvent(OrganizerEvent event) {
        this.event = event;
    }

    public Priority[] getPriorities() {
        return Priority.values();
    }

    public String addNewEvent() {
        OrganizerUser user = ((UserDetailsAdapter)SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUser();
        event.setOwner(user);
        logger.info("Adding new event for user: " + user.getLogin());
        boolean success = eventService.addNewEvent(event);
        if (success) {
            logger.info("Successfully added new event for user: " + user.getLogin());
            return SUCCESS_PAGE;
        } else {
            logger.warn("Error adding new event for user: " + user.getLogin());
            return null;
        }
    }

    public String editEvent(OrganizerEvent event) {
        logger.info("Saving changes to event: " + event.getId());
        eventService.saveEventChanges(event);
        return SUCCESS_PAGE;
    }
}
