package ark.chr.web.organizer.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author Arek
 */
@Entity
@Table(name = "organizer_user")
public class OrganizerUser extends BaseEntity implements Serializable {
    
    @Column
    @Email
    @NotNull
    @Size(min = 6, max = 50)
    private String login;
    
    @Column(name = "pass")
    @NotNull
    private String password;

    @Column
    @NotNull
    private String name;
    
    @Column
    private boolean active;
    
    @Column(name = "date_created")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreated;
    
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {
                @JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Collection<OrganizerRole> roles = new HashSet<>();
    
    @OneToMany(mappedBy = "owner")
    private List<OrganizerEvent> events = new ArrayList<>();
    
    public OrganizerUser() {
        active = false;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Collection<OrganizerRole> getRoles() {
        return roles;
    }

    public List<OrganizerEvent> getEvents() {
        return events;
    }
}
