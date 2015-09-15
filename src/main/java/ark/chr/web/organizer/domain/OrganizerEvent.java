package ark.chr.web.organizer.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Arek
 */
@Entity
@Table(name = "organizer_event")
public class OrganizerEvent extends BaseEntity implements Serializable {

    @Column
    @NotNull
    private String name;
    
    @Column
    private String description;
    
    @Column(name = "event_date_start")
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date eventDateStart;
    
    @Column(name = "event_date_end")
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date eventDateEnd;
    
    @Column
    private String address;
    
    @ManyToOne
    private OrganizerUser owner;
    
    @Column(name = "reminder")
    private int customReminder;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private Priority priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventDateStart() {
        return eventDateStart;
    }

    public void setEventDateStart(Date eventDateStart) {
        this.eventDateStart = eventDateStart;
    }

    public Date getEventDateEnd() {
        return eventDateEnd;
    }

    public void setEventDateEnd(Date eventDateEnd) {
        this.eventDateEnd = eventDateEnd;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrganizerUser getOwner() {
        return owner;
    }

    public void setOwner(OrganizerUser owner) {
        this.owner = owner;
    }

    public int getCustomReminder() {
        return customReminder;
    }

    public void setCustomReminder(int customReminder) {
        this.customReminder = customReminder;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
