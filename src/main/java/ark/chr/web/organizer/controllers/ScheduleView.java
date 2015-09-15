package ark.chr.web.organizer.controllers;

import ark.chr.web.organizer.domain.OrganizerEvent;
import ark.chr.web.organizer.domain.OrganizerUser;
import ark.chr.web.organizer.services.api.IEventService;
import ark.chr.web.organizer.services.security.UserDetailsAdapter;
import ark.chr.web.organizer.utils.Notifications;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arek on 2015-09-10.
 */
@RestController
@Scope("view")
public class ScheduleView implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleView.class);

    private ScheduleModel schedule;

    private OrganizerEvent event;

    @Autowired
    private IEventService eventService;

    @Autowired
    private Notifications notifications;

    @PostConstruct
    public void init() {
        OrganizerUser user = ((UserDetailsAdapter) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUser();
        List<OrganizerEvent> allUserEvents = eventService.findAllEventsForUser(user);

        schedule = new DefaultScheduleModel();
        for (OrganizerEvent allUserEvent : allUserEvents) {
            schedule.addEvent(new DefaultScheduleEvent(allUserEvent.getName(),
                    allUserEvent.getEventDateStart(), allUserEvent.getEventDateEnd()));
        }

        generateGrowlsForUser(user);
    }

    public ScheduleModel getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleModel schedule) {
        this.schedule = schedule;
    }

    public OrganizerEvent getEvent() {
        return event;
    }

    public void setEvent(OrganizerEvent event) {
        this.event = event;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        ScheduleEvent selected = (ScheduleEvent) selectEvent.getObject();
        event = eventService.findEventByNameStartDateAndUser(selected.getTitle(),
                selected.getStartDate(), ((UserDetailsAdapter) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal()).getUser());
        logger.info("Showing details for event: " + event.getId());
    }

    private void generateGrowlsForUser(OrganizerUser user) {
        List<FacesMessage> growls = new ArrayList<>();
        try {
            growls = notifications.createNotificationMessagesForUser(user);
        } catch (ParseException ex) {
            logger.error("ParserException in ScheduleView init");
        }
        if (!growls.isEmpty()) {
            logger.info("Amount of growls: " + growls.size());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            for (FacesMessage growl : growls) {
                facesContext.addMessage(null, growl);
            }
        } else {
            logger.info("No growls created.");
        }
    }
}
