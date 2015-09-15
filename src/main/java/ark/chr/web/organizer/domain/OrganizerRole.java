package ark.chr.web.organizer.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Arek
 */
@Entity
@Table(name = "organizer_role")
public class OrganizerRole extends BaseEntity implements Serializable, GrantedAuthority {

    @Column
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = {
                @JoinColumn(name = "role_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "permission_id")})
    private Set<Permission> permissions = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        GrantedAuthority ga = (GrantedAuthority) o;
        return (getAuthority().equals(ga.getAuthority()));
    }

    @Override
    public int hashCode() {
        return getAuthority().hashCode();
    }
}
