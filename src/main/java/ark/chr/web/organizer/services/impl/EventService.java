package ark.chr.web.organizer.services.impl;

import ark.chr.web.organizer.dao.IOrganizerEventDao;
import ark.chr.web.organizer.domain.OrganizerEvent;
import ark.chr.web.organizer.domain.OrganizerUser;
import ark.chr.web.organizer.services.api.IEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arek on 2015-07-15.
 */
@Service
@Transactional
public class EventService implements IEventService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private static final int HIGH_PRIOR_DAYS_AMOUNT = 5;
    private static final int MEDIUM_PRIOR_DAYS_AMOUNT = 3;
    private static final int LOW_PRIOR_DAYS_AMOUNT = 1;

    @Autowired
    private IOrganizerEventDao eventDao;

    @Override
    public boolean addNewEvent(OrganizerEvent event) {
        if (event.getCustomReminder() <= 0) {
            logger.info("Setting auto reminder for event");
            switch (event.getPriority()) {
                case HIGH:
                    event.setCustomReminder(HIGH_PRIOR_DAYS_AMOUNT);
                    break;
                case MEDIUM:
                    event.setCustomReminder(MEDIUM_PRIOR_DAYS_AMOUNT);
                    break;
                case LOW:
                    event.setCustomReminder(LOW_PRIOR_DAYS_AMOUNT);
                    break;
            }
        }
        eventDao.save(event);
        return true;
    }

    @Override
    public List<OrganizerEvent> findAllEventsForUser(OrganizerUser user) {
        return eventDao.findAllEventsForUser(user);
    }

    @Override
    public OrganizerEvent findEventByNameStartDateAndUser(String name, Date startDate, OrganizerUser user) {
        return eventDao.findByNameStartDateAndUser(name, startDate, user);
    }

    @Override
    public void saveEventChanges(OrganizerEvent event) {
        eventDao.save(event);
    }

    @Override
    public List<OrganizerEvent> createNotificationsForEvents(OrganizerUser user) {
        List<OrganizerEvent> allEvents = eventDao.findAllEventsForUser(user);
        List<OrganizerEvent> result = new ArrayList<>();

        for (OrganizerEvent event : allEvents) {
            int daysDiff = getDaysDifferenceFromCurrentTime(event.getEventDateStart());
            logger.info("Days diff amount: " + daysDiff);
            switch (event.getPriority()) {
                case HIGH:
                    if (daysDiff == HIGH_PRIOR_DAYS_AMOUNT
                            || daysDiff == MEDIUM_PRIOR_DAYS_AMOUNT
                            || daysDiff == LOW_PRIOR_DAYS_AMOUNT) {
                        logger.info("Adding event notification for high priority for user: "
                                + user.getLogin());
                        result.add(event);
                    }
                    break;
                case MEDIUM:
                    if (daysDiff == MEDIUM_PRIOR_DAYS_AMOUNT
                            || daysDiff == LOW_PRIOR_DAYS_AMOUNT) {
                        logger.info("Adding event notification for medium priority for user: "
                                + user.getLogin());
                        result.add(event);
                    }
                    break;
                case LOW:
                    if (daysDiff == LOW_PRIOR_DAYS_AMOUNT) {
                        logger.info("Adding event notification for low priority for user: "
                                + user.getLogin());
                        result.add(event);
                    }
                    break;
            }
            if (daysDiff == event.getCustomReminder() && !result.contains(event)) {
                logger.info("Adding event notification custom reminder for user: "
                        + user.getLogin());
                result.add(event);
            }
        }

        return result;
    }

    @Override
    public int getDaysDifferenceFromCurrentTime(Date eventDateStart) {
        LocalDate calcDate = eventDateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate current = LocalDate.now(ZoneId.systemDefault());
        return (int) Duration.between(current.atTime(0,0), calcDate.atTime(0,0)).toDays();
    }
}
