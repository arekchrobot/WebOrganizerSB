package ark.chr.web.organizer.dao;

import ark.chr.web.organizer.domain.OrganizerRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Arek on 2015-07-15.
 */
public interface IOrganizerRoleDao extends JpaRepository<OrganizerRole, Long> {

    OrganizerRole findByName(String name);
}
