package ark.chr.web.organizer.dao;

import ark.chr.web.organizer.domain.OrganizerEvent;
import ark.chr.web.organizer.domain.OrganizerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Arek on 2015-07-15.
 */
public interface IOrganizerEventDao extends JpaRepository<OrganizerEvent, Long> {

    @Query("SELECT e FROM OrganizerEvent e WHERE e.owner = :user")
    List<OrganizerEvent> findAllEventsForUser(@Param("user")OrganizerUser user);

    @Query("SELECT e FROM OrganizerEvent e WHERE e.owner = :user AND e.eventDateStart = :startDate AND e.name = :name")
    OrganizerEvent findByNameStartDateAndUser(@Param("name")String name, @Param("startDate")Date startDate,
                                              @Param("user")OrganizerUser user);
}
