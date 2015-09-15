package ark.chr.web.organizer.services.api;

import ark.chr.web.organizer.domain.OrganizerEvent;
import ark.chr.web.organizer.domain.OrganizerUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Arek on 2015-07-15.
 */
public interface IEventService {

    boolean addNewEvent(OrganizerEvent event);

    List<OrganizerEvent> findAllEventsForUser(OrganizerUser user);

    OrganizerEvent findEventByNameStartDateAndUser(String name, Date startDate, OrganizerUser user);

    void saveEventChanges(OrganizerEvent event);

    List<OrganizerEvent> createNotificationsForEvents(OrganizerUser user);

    int getDaysDifferenceFromCurrentTime(Date eventDateStart);
}
