package ark.chr.web.organizer.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Arek
 */
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity implements Serializable, GrantedAuthority {

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
    
    @Override
    public boolean equals(Object o) {
        GrantedAuthority ga = (GrantedAuthority) o;
        return (getAuthority().equals(ga.getAuthority()));
    }

    @Override
    public int hashCode() {
        return getAuthority().hashCode();
    }
}
